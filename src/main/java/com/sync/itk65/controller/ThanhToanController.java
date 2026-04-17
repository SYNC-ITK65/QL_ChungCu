package com.sync.itk65.controller;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.ThanhToan;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.service.ThanhToanService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class ThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // 1. Xem danh sách Lịch sử thanh toán
    // Đường dẫn: http://localhost:8080/admin/thanh-toan/lich-su
    @GetMapping("/thanh-toan/lich-su")
    public String danhSachThanhToan(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Page<ThanhToan> trangDuLieu = thanhToanService.layTatCaThanhToan(page, size);
        model.addAttribute("danhSachThanhToan", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        return "admin/thanh_toan_list"; // Đã thêm admin/
    }

    // 2. Mở Form xác nhận thu tiền
    // Đường dẫn: http://localhost:8080/admin/thanh-toan/xac-nhan/{id}
    @GetMapping("/thanh-toan/xac-nhan/{id}")
    public String hienThiFormThanhToan(@PathVariable("id") Long hoaDonId, Model model) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa Đơn với ID: " + hoaDonId));

        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setSoTien(hoaDon.getTongTien());
        thanhToan.setNgayThanhToan(LocalDate.now());

        model.addAttribute("thanhToan", thanhToan);
        model.addAttribute("hoaDon", hoaDon);
        return "admin/thanh_toan_form"; // Đã thêm admin/
    }

    // 3. Xử lý lưu thanh toán
    @PostMapping("/thanh-toan/luu")
    public String luuThanhToan(@ModelAttribute("thanhToan") ThanhToan thanhToan, RedirectAttributes ra) {
        try {
            thanhToanService.thucHienThanhToan(thanhToan);
            ra.addFlashAttribute("thongBaoThanhCong", "Xác nhận thanh toán thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/admin/hoa-don";
    }
}