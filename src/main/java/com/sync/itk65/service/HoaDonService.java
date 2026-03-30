package com.sync.itk65.service;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // Lấy danh sách toàn bộ hóa đơn
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }

    // Tính toán tổng tiền và lưu
    public void luuHoaDonCoTinhToan(HoaDon hoaDon, Double soDien, Double soNuoc) {
        // Cấu hình đơn giá (Tạm thời fix cứng để demo)
        Double donGiaDien = 3500.0;  // 3500 VNĐ / 1 kWh
        Double donGiaNuoc = 15000.0; // 15000 VNĐ / 1 khối
        Double phiQuanLy = 200000.0; // Phí quản lý cố định

        // Tính tổng tiền
        Double tongTien = (soDien * donGiaDien) + (soNuoc * donGiaNuoc) + phiQuanLy;
        hoaDon.setTongTien(tongTien);

        // Mặc định tạo mới là Chưa đóng
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