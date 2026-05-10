package com.sync.itk65.service;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.ThanhToan;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.ThanhToanRepository;
import com.sync.itk65.service.LichSuThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThanhToanService {

    @Autowired
    private ThanhToanRepository thanhToanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private LichSuThanhToanService lichSuThanhToanService;

    // Lấy tất cả lịch sử thanh toán
    public List<ThanhToan> layTatCaThanhToan() {
        return thanhToanRepository.findAll();
    }

    public Page<ThanhToan> layTatCaThanhToan(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return thanhToanRepository.findAll(pageable);
    }

    // Lấy lịch sử thanh toán theo ID hóa đơn
    public List<ThanhToan> layThanhToanTheoHoaDon(Long hoaDonId) {
        return thanhToanRepository.findByHoaDonId(hoaDonId);
    }

    public List<ThanhToan> layLichSuTheoCanHo(Long canHoId) {
        return thanhToanRepository.findByHoaDonCanHoIdOrderByNgayThanhToanDescIdDesc(canHoId);
    }

    // Xử lý thanh toán mới với transaction đảm bảo tính toàn vẹn
    // synchronized để chống double-click/race condition
    @Transactional(rollbackFor = Exception.class)
    public synchronized ThanhToan thucHienThanhToan(ThanhToan thanhToan) {
        // VALIDATION: Kiểm tra thông tin hóa đơn
        if (thanhToan.getHoaDon() == null || thanhToan.getHoaDon().getId() == null) {
            throw new RuntimeException("Thiếu thông tin hóa đơn cần thanh toán.");
        }
        
        HoaDon hoaDon = hoaDonRepository.findById(thanhToan.getHoaDon().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID: " + thanhToan.getHoaDon().getId()));
        
        // VALIDATION: Kiểm tra trạng thái hóa đơn
        if ("Đã đóng".equalsIgnoreCase(hoaDon.getTrangThaiThanhToan())) {
            throw new RuntimeException("Hóa đơn #" + hoaDon.getId() + " đã được thanh toán trước đó.");
        }

        // VALIDATION: Kiểm tra xem hóa đơn đã có giao dịch trong bảng thanh_toan chưa
        List<ThanhToan> existingPayments = thanhToanRepository.findByHoaDonId(hoaDon.getId());
        if (!existingPayments.isEmpty()) {
            throw new RuntimeException("Hóa đơn #" + hoaDon.getId() + " đã có giao dịch thanh toán trong hệ thống. Không thể tạo thêm.");
        }
        
        // VALIDATION: Kiểm tra số tiền
        if (thanhToan.getSoTien() == null || thanhToan.getSoTien() <= 0) {
            throw new RuntimeException("Số tiền thanh toán không hợp lệ: " + thanhToan.getSoTien());
        }
        
        if (hoaDon.getTongTien() != null && Math.abs(thanhToan.getSoTien() - hoaDon.getTongTien()) > 0.01) {
            throw new RuntimeException(String.format("Số tiền thanh toán (%.2f) phải đúng bằng tổng tiền hóa đơn (%.2f).", 
                    thanhToan.getSoTien(), hoaDon.getTongTien()));
        }
        
        // VALIDATION: Kiểm tra phương thức thanh toán
        if (thanhToan.getPhuongThuc() == null || thanhToan.getPhuongThuc().trim().isEmpty()) {
            throw new RuntimeException("Phương thức thanh toán không được để trống.");
        }

        // 1. Chuẩn bị thông tin thanh toán
        if (thanhToan.getNgayThanhToan() == null) {
            thanhToan.setNgayThanhToan(LocalDate.now());
        }
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setPhuongThuc(thanhToan.getPhuongThuc().trim());

        // 2. Lưu giao dịch thanh toán vào DB
        ThanhToan savedThanhToan = thanhToanRepository.save(thanhToan);

        // 3. Cập nhật trạng thái Hóa Đơn thành "Đã đóng" ngay lập tức
        hoaDon.setTrangThaiThanhToan("Đã đóng");
        hoaDonRepository.save(hoaDon);

        // 4. Thêm vào lịch sử thanh toán
        try {
            lichSuThanhToanService.themLichSuThanhToan(
                hoaDon,
                thanhToan.getPhuongThuc(),
                "Admin",
                "Thanh toán hóa đơn #" + hoaDon.getId()
            );
        } catch (Exception e) {
            // Nếu có lỗi khi thêm lịch sử, vẫn cho thanh toán thành công nhưng log lỗi
            System.err.println("Lỗi khi thêm lịch sử thanh toán: " + e.getMessage());
        }

        return savedThanhToan;
    }
}