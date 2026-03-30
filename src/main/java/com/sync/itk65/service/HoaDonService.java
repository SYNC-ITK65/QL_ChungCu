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

    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }

    // 1. Thêm logic tính toán tổng tiền
    public void luuHoaDonCoTinhToan(HoaDon hoaDon, Double soDien, Double soNuoc) {
        // Giả sử logic tính tiền của chung cư (có thể lấy từ DB bảng DichVu, nhưng làm cứng tạm cho dễ hiểu)
        Double donGiaDien = 3500.0;  // 3500 VNĐ / 1 kWh
        Double donGiaNuoc = 15000.0; // 15000 VNĐ / 1 khối
        Double phiQuanLy = 200000.0; // Phí cố định hàng tháng

        // Tính tổng tiền
        Double tongTien = (soDien * donGiaDien) + (soNuoc * donGiaNuoc) + phiQuanLy;

        hoaDon.setTongTien(tongTien);

        if(hoaDon.getTrangThaiThanhToan() == null) {
            hoaDon.setTrangThaiThanhToan("Chưa đóng");
        }
        hoaDonRepository.save(hoaDon);
    }
    // 2. Thêm logic chuyển trạng thái thanh toán
    public void danhDauDaThanhToan(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElse(null);
        if (hoaDon != null) {
            hoaDon.setTrangThaiThanhToan("Đã đóng");
            hoaDonRepository.save(hoaDon);
        }
    }
}