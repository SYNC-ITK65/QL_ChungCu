package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.KienHang;
import com.sync.itk65.entity.ThongBao;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.KienHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KienHangService {

    @Autowired
    private KienHangRepository kienHangRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private ThongBaoService thongBaoService;

    public List<KienHang> layTatCaKienHang() {
        return kienHangRepository.findAll();
    }

    public Page<KienHang> layTatCaKienHang(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return kienHangRepository.findAll(pageable);
    }

    public Page<KienHang> searchKienHang(String maCanHo, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return kienHangRepository.searchKienHang(maCanHo, trangThai, pageable);
    }

    public List<KienHang> layKienHangTheoCanHo(Long canHoId) {
        return kienHangRepository.findByCanHoId(canHoId);
    }

    public KienHang luuKienHang(KienHang kienHang) {
        // Kiểm tra xem đây là tạo mới hay cập nhật
        boolean laTaoMoi = (kienHang.getId() == null);

        if (kienHang.getNgayNhan() == null) {
            kienHang.setNgayNhan(LocalDate.now());
        }
        if (kienHang.getTrangThai() == null) {
            kienHang.setTrangThai("Chờ nhận");
        }

        KienHang savedKienHang = kienHangRepository.save(kienHang);

        // Tự động tạo thông báo khi có kiện hàng MỚI
        if (laTaoMoi) {
            taoThongBaoKienHangMoi(savedKienHang);
        }

        return savedKienHang;
    }

    public void xacNhanDaNhan(Long id) {
        KienHang kienHang = kienHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kiện hàng!"));
        kienHang.setTrangThai("Đã nhận");
        kienHangRepository.save(kienHang);
    }

    public KienHang layTheoId(Long id) {
        return kienHangRepository.findById(id).orElse(null);
    }

    public void xoaKienHang(Long id) {
        kienHangRepository.deleteById(id);
    }

    /**
     * Tự động tạo thông báo cho cư dân khi có kiện hàng mới tại lễ tân.
     * Thông báo sẽ được gửi đến hộ gia đình liên quan.
     */
    private void taoThongBaoKienHangMoi(KienHang kienHang) {
        try {
            ThongBao thongBao = new ThongBao();
            thongBao.setTieuDe("📦 Bạn có bưu phẩm/kiện hàng mới tại Quầy Lễ Tân");

            String noiDung = "Có một kiện hàng từ " + (kienHang.getNguoiGui() != null ? kienHang.getNguoiGui() : "N/A")
                    + " gửi cho " + (kienHang.getNguoiNhan() != null ? kienHang.getNguoiNhan() : "N/A")
                    + ". Vui lòng mang theo giấy tờ xuống sảnh nhận hàng.";

            thongBao.setNoiDung(noiDung);
            thongBao.setLoai(1); // Bảng tin
            thongBao.setDoiTuongGui("HO_GIA_DINH");

            // Lấy thông tin Căn hộ đầy đủ từ DB (form chỉ bind canHo.id, getMaCanHo() sẽ null)
            String maCanHo = "";
            if (kienHang.getCanHo() != null && kienHang.getCanHo().getId() != null) {
                CanHo canHoFull = canHoRepository.findById(kienHang.getCanHo().getId()).orElse(null);
                maCanHo = (canHoFull != null) ? canHoFull.getMaCanHo() : "";
            }
            thongBao.setGiaTriDoiTuong(maCanHo);

            thongBaoService.saveThongBao(thongBao);
        } catch (Exception e) {
            // Không throw exception để không ảnh hưởng đến việc lưu kiện hàng
            System.err.println("Lỗi khi tạo thông báo kiện hàng: " + e.getMessage());
        }
    }
}
