package com.sync.itk65.service;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.ThanhToan;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThanhToanService {

    @Autowired
    private ThanhToanRepository thanhToanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // Lấy tất cả lịch sử thanh toán
    public List<ThanhToan> layTatCaThanhToan() {
        return thanhToanRepository.findAll();
    }

    // Lấy lịch sử thanh toán theo ID hóa đơn
    public List<ThanhToan> layThanhToanTheoHoaDon(Long hoaDonId) {
        return thanhToanRepository.findByHoaDonId(hoaDonId);
    }

    // Xử lý thanh toán mới
    public ThanhToan thucHienThanhToan(ThanhToan thanhToan) {
        // 1. Tự động gán ngày thanh toán nếu chưa có
        if (thanhToan.getNgayThanhToan() == null) {
            thanhToan.setNgayThanhToan(LocalDate.now());
        }

        // 2. Lưu giao dịch thanh toán vào DB
        ThanhToan savedThanhToan = thanhToanRepository.save(thanhToan);

        // 3. Cập nhật trạng thái Hóa Đơn thành "Đã đóng"
        if (thanhToan.getHoaDon() != null && thanhToan.getHoaDon().getId() != null) {
            HoaDon hoaDon = hoaDonRepository.findById(thanhToan.getHoaDon().getId()).orElse(null);
            if (hoaDon != null) {
                hoaDon.setTrangThaiThanhToan("Đã đóng");
                hoaDonRepository.save(hoaDon);
            }
        }

        return savedThanhToan;
    }
}