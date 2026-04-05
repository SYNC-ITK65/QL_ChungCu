package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.entity.DatDichVu; // Import Entity DatDichVu
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.ChiSoHangThangRepository;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.PhuongTienRepository;
import com.sync.itk65.repository.DatDichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiSoHangThangRepository chiSoHangThangRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private PhuongTienRepository phuongTienRepository;

    @Autowired
    private DatDichVuRepository datDichVuRepository;

    // Lấy danh sách toàn bộ hóa đơn
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }

    public void taoHoaDonTuDong(HoaDon hoaDon) {
        Long canHoId = hoaDon.getCanHo().getId();

        // 1. Xác định kỳ hóa đơn (Tháng/Năm)
        LocalDate ngayPhatHanh = hoaDon.getNgayPhatHanh();
        if (ngayPhatHanh == null) {
            ngayPhatHanh = LocalDate.now();
            hoaDon.setNgayPhatHanh(ngayPhatHanh);
        }
        int thang = ngayPhatHanh.getMonthValue();
        int nam = ngayPhatHanh.getYear();

        Double tongTien = 0.0;

        // 2. Tính tiền Điện Nước
        ChiSoHangThang chiSo = chiSoHangThangRepository.findByCanHoAndThangNam(canHoId, thang, nam)
                .orElseThrow(() -> new RuntimeException("Chưa ghi nhận chỉ số điện nước tháng " + thang + "/" + nam));

        Double donGiaDien = 3500.0; // Nên đưa vào cấu hình DB sau
        Double donGiaNuoc = 15000.0;
        tongTien += (chiSo.getDienTieuThu() * donGiaDien) + (chiSo.getNuocTieuThu() * donGiaNuoc);

        // 3. Tính phí Quản lý chung cư (diện tích * đơn giá)
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Căn hộ"));
        Double donGiaQuanLy = 12000.0; // 12k/m2
        tongTien += (canHo.getDienTich() != null ? canHo.getDienTich() : 0.0) * donGiaQuanLy;

        // 4. Tính phí gửi xe
        List<PhuongTien> danhSachXe = phuongTienRepository.findByCanHoId(canHoId);
        for (PhuongTien xe : danhSachXe) {
            // Chỉ tính tiền nếu admin đã duyệt cho phép gửi xe
            if ("Đã duyệt".equalsIgnoreCase(xe.getTrangThai())) {
                if ("Oto".equalsIgnoreCase(xe.getLoaiXe())) {
                    tongTien += 1200000.0;
                } else if ("XeMay".equalsIgnoreCase(xe.getLoaiXe())) {
                    tongTien += 150000.0;
                }
            }
        }
        // 5. Tính phí Dịch vụ phát sinh (Hồ bơi, BBQ...)
        List<DatDichVu> dichVuDaDung = datDichVuRepository.findDichVuCuaCanHoTrongThang(canHoId, thang, nam);
        for (DatDichVu dv : dichVuDaDung) {
            // Chỉ tính tiền nếu admin đã xác nhận cư dân sử dụng dịch vụ này
            if ("Đã duyệt".equalsIgnoreCase(dv.getTrangThai()) || "Đã hoàn thành".equalsIgnoreCase(dv.getTrangThai())) {
                if(dv.getDichVu() != null && dv.getDichVu().getDonGia() != null) {
                    tongTien += dv.getDichVu().getDonGia();
                }
            }
        }

        // 6. Gán tổng tiền và lưu database
        hoaDon.setTongTien(tongTien);

        if(hoaDon.getTrangThaiThanhToan() == null) {
            hoaDon.setTrangThaiThanhToan("Chưa đóng");
        }

        hoaDonRepository.save(hoaDon);
    }

    // Đánh dấu đã thanh toán
    public void danhDauDaThanhToan(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElse(null);
        if (hoaDon != null) {
            hoaDon.setTrangThaiThanhToan("Đã đóng");
            hoaDonRepository.save(hoaDon);
        }
    }

}