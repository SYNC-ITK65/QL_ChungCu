package com.sync.itk65.service;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.LichSuThanhToan;
import com.sync.itk65.entity.CanHo;
import com.sync.itk65.repository.LichSuThanhToanRepository;
import com.sync.itk65.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LichSuThanhToanService {

    @Autowired
    private LichSuThanhToanRepository lichSuThanhToanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // Lý danh sách toàn b l ch s thanh toán
    public List<LichSuThanhToan> layTatCaLichSu() {
        return lichSuThanhToanRepository.findAllByOrderByNgayThanhToanDesc();
    }

    // Lý l ch s thanh toán theo c n h
    public List<LichSuThanhToan> layLichSuTheoCanHo(Long canHoId) {
        return lichSuThanhToanRepository.findByCanHoIdOrderByNgayThanhToanDesc(canHoId);
    }

    // Tìm l ch s theo ID hóa ð n
    public Optional<LichSuThanhToan> timLichSuTheoHoaDonId(Long hoaDonId) {
        return lichSuThanhToanRepository.findByHoaDonId(hoaDonId);
    }

    // Ki m tra hóa ð n ñã thanh toán ch a
    public boolean kiemTraDaThanhToan(Long hoaDonId) {
        return lichSuThanhToanRepository.existsByHoaDonId(hoaDonId);
    }

    // Thêm l ch s thanh toán khi hóa ð n ñã thanh toán
    // synchronized để chống race condition khi admin double-click
    public synchronized void themLichSuThanhToan(HoaDon hoaDon, String phuongThuc, String nguoiThanhToan,
            String ghiChu) {
        // Kiểm tra hóa đơn đã có trong lịch sử chưa (check 1)
        if (kiemTraDaThanhToan(hoaDon.getId())) {
            throw new RuntimeException("Hóa đơn này đã có trong lịch sử thanh toán");
        }

        LichSuThanhToan lichSu = new LichSuThanhToan();
        lichSu.setHoaDonId(hoaDon.getId());
        // Tạo mã hóa đơn có thêm thông tin để dễ phân biệt
        lichSu.setMaHoaDon("HD-" + hoaDon.getId() + "-" + hoaDon.getCanHo().getMaCanHo());
        lichSu.setNgayThanhToan(LocalDateTime.now());
        lichSu.setSoTienThanhToan(hoaDon.getTongTien());
        lichSu.setPhuongThucThanhToan(phuongThuc != null ? phuongThuc : "Tiền mặt");
        lichSu.setNguoiThanhToan(nguoiThanhToan);
        lichSu.setGhiChu(ghiChu);
        lichSu.setCanHo(hoaDon.getCanHo());
        lichSu.setTrangThaiSua("Bình thường");

        // Re-validation trước khi save (check 2 - chống race condition)
        if (kiemTraDaThanhToan(hoaDon.getId())) {
            throw new RuntimeException("Hóa đơn này vừa được thêm vào lịch sử bởi request khác");
        }

        lichSuThanhToanRepository.save(lichSu);
    }

    // C p nh t l ch s thanh toán
    public void capNhatLichSu(LichSuThanhToan lichSu) {
        lichSuThanhToanRepository.save(lichSu);
    }

    // Xóa l ch s thanh toán (khi c n undo thanh toán)
    public void xoaLichSu(Long hoaDonId) {
        Optional<LichSuThanhToan> lichSu = timLichSuTheoHoaDonId(hoaDonId);
        if (lichSu.isPresent()) {
            lichSuThanhToanRepository.delete(lichSu.get());
        }
    }

    // Undo thanh toán - chuy n hóa ð n v trang thái "Ch a ð ng"
    public void undoThanhToan(Long hoaDonId) {
        // Xóa l ch s thanh toán
        xoaLichSu(hoaDonId);

        // C p nh t hóa ð n v trang thái "Ch a ð ng"
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm th y hóa ð n"));
        hoaDon.setTrangThaiThanhToan("Ch a ð ng");
        hoaDonRepository.save(hoaDon);
    }

    // Tìm ki m l ch s
    public List<LichSuThanhToan> timKiemLichSu(String keyword) {
        return lichSuThanhToanRepository.searchByKeyword(keyword);
    }

    // Lý l ch s theo kho ng th i gian
    public List<LichSuThanhToan> layLichSuTheoKhoangThoiGian(LocalDateTime startDate, LocalDateTime endDate) {
        return lichSuThanhToanRepository.findByNgayThanhToanBetween(startDate, endDate);
    }

    // Lý l ch s theo ph ng th c thanh toán
    public List<LichSuThanhToan> layLichSuTheoPhuongThuc(String phuongThuc) {
        return lichSuThanhToanRepository.findByPhuongThucThanhToanOrderByNgayThanhToanDesc(phuongThuc);
    }
}
