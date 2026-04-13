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

    public List<ThanhToan> layLichSuTheoCanHo(Long canHoId) {
        return thanhToanRepository.findByHoaDonCanHoIdOrderByNgayThanhToanDescIdDesc(canHoId);
    }

    // Xử lý thanh toán mới
    public ThanhToan thucHienThanhToan(ThanhToan thanhToan) {
        if (thanhToan.getHoaDon() == null || thanhToan.getHoaDon().getId() == null) {
            throw new RuntimeException("Thiếu thông tin hóa đơn cần thanh toán.");
        }
        HoaDon hoaDon = hoaDonRepository.findById(thanhToan.getHoaDon().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn."));
        if ("Đã đóng".equalsIgnoreCase(hoaDon.getTrangThaiThanhToan())) {
            throw new RuntimeException("Hóa đơn đã được thanh toán trước đó.");
        }
        if (thanhToan.getSoTien() == null || thanhToan.getSoTien() <= 0) {
            throw new RuntimeException("Số tiền thanh toán không hợp lệ.");
        }
        if (hoaDon.getTongTien() != null && Math.abs(thanhToan.getSoTien() - hoaDon.getTongTien()) > 0.01) {
            throw new RuntimeException("Số tiền thanh toán phải đúng bằng tổng tiền hóa đơn.");
        }
        if (thanhToan.getPhuongThuc() == null || thanhToan.getPhuongThuc().trim().isEmpty()) {
            throw new RuntimeException("Phương thức thanh toán không được để trống.");
        }

        // 1. Tự động gán ngày thanh toán nếu chưa có
        if (thanhToan.getNgayThanhToan() == null) {
            thanhToan.setNgayThanhToan(LocalDate.now());
        }
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setPhuongThuc(thanhToan.getPhuongThuc().trim());

        // 2. Lưu giao dịch thanh toán vào DB
        ThanhToan savedThanhToan = thanhToanRepository.save(thanhToan);

        // 3. Cập nhật trạng thái Hóa Đơn thành "Đã đóng"
        hoaDon.setTrangThaiThanhToan("Đã đóng");
        hoaDonRepository.save(hoaDon);

        return savedThanhToan;
    }
}