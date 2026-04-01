package com.sync.itk65.service;

import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.repository.ChiSoHangThangRepository;
import com.sync.itk65.repository.HoaDonRepository;
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

    // Lấy danh sách toàn bộ hóa đơn
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }

    public void taoHoaDonTuDong(HoaDon hoaDon) {
        Long canHoId = hoaDon.getCanHo().getId();

        // Nếu chưa nhập ngày phát hành, mặc định lấy ngày hôm nay
        LocalDate ngayPhatHanh = hoaDon.getNgayPhatHanh();
        if (ngayPhatHanh == null) {
            ngayPhatHanh = LocalDate.now();
            hoaDon.setNgayPhatHanh(ngayPhatHanh);
        }

        int thang = ngayPhatHanh.getMonthValue();
        int nam = ngayPhatHanh.getYear();

        // 1. Tìm chỉ số điện nước của căn hộ trong tháng đó
        ChiSoHangThang chiSo = chiSoHangThangRepository.findByCanHoAndThangNam(canHoId, thang, nam)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chỉ số điện nước của Căn hộ này trong tháng " + thang + "/" + nam + ". Vui lòng ghi nhận chỉ số trước khi tạo hóa đơn!"));

        // 2. Cấu hình đơn giá (sau này có thể đưa vào bảng DICH_VU)
        Double donGiaDien = 3500.0;
        Double donGiaNuoc = 15000.0;
        Double phiQuanLy = 200000.0;

        // 3. Tính tổng tiền tự động
        Double tongTien = (chiSo.getDienTieuThu() * donGiaDien) + (chiSo.getNuocTieuThu() * donGiaNuoc) + phiQuanLy;
        hoaDon.setTongTien(tongTien);

        // 4. Mặc định trạng thái
        if(hoaDon.getTrangThaiThanhToan() == null) {
            hoaDon.setTrangThaiThanhToan("Chưa đóng");
        }

        // 5. Lưu xuống Database
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

    public void xacNhanThanhToan(Long id) {
        // Tìm hóa đơn trong Database theo ID
        HoaDon hoaDon = hoaDonRepository.findById(id).orElse(null);

        if (hoaDon != null) {
            // Cập nhật trạng thái thành Đã đóng
            hoaDon.setTrangThaiThanhToan("Đã đóng");

            // Lưu lại vào Database
            hoaDonRepository.save(hoaDon);
        }
    }
}