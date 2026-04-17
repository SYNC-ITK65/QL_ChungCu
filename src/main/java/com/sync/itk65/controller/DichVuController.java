package com.sync.itk65.controller;

import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.entity.DichVu;
import com.sync.itk65.service.DatDichVuService;
import com.sync.itk65.service.DichVuService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/dich-vu")
public class DichVuController {

    @Autowired
    private DichVuService dichVuService;
    @Autowired
    private DatDichVuService datDichVuService;

    // 1. Hiển thị danh sách dịch vụ
    // Phục vụ cho menu: <a href="/admin/dich-vu" class="nav-link text-white">🛠️ Quản lý Dịch vụ</a>
    @GetMapping
    public String danhSachDichVu(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("danhSachDichVu", dichVuService.layTatCaDichVu());
        Page<DatDichVu> trangDuLieu = datDichVuService.layTatCaDonDatDichVu(page, size);
        model.addAttribute("danhSachDonDat", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        return "admin/dich_vu_list"; // File view: templates/admin/dich_vu_list.html
    }

    @GetMapping("/don-dat/duyet/{id}")
    public String duyetDonDatDichVu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            datDichVuService.duyetDonDatDichVu(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã duyệt đăng ký dịch vụ thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi duyệt đăng ký dịch vụ: " + e.getMessage());
        }
        return "redirect:/admin/dich-vu";
    }

    @GetMapping("/don-dat/tu-choi/{id}")
    public String tuChoiDonDatDichVu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            datDichVuService.tuChoiDonDatDichVu(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã từ chối đăng ký dịch vụ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi từ chối đăng ký dịch vụ: " + e.getMessage());
        }
        return "redirect:/admin/dich-vu";
    }

    @GetMapping("/don-dat/huy-duyet/{id}")
    public String huyDuyetDonDatDichVu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            datDichVuService.huyDuyetDonDatDichVu(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã hủy duyệt đăng ký dịch vụ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi hủy duyệt đăng ký dịch vụ: " + e.getMessage());
        }
        return "redirect:/admin/dich-vu";
    }

    @GetMapping("/don-dat/sua/{id}")
    public String suaLaiTrangThaiChoDuyet(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            datDichVuService.suaLaiTrangThaiChoDuyet(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã chuyển đơn về trạng thái Chờ duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi sửa trạng thái đăng ký dịch vụ: " + e.getMessage());
        }
        return "redirect:/admin/dich-vu";
    }

    // 2. Hiển thị form tạo mới dịch vụ
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        model.addAttribute("dichVu", new DichVu());
        return "admin/dich_vu_form"; // File view: templates/admin/dich_vu_form.html
    }

    // 3. Xử lý lưu dịch vụ (Dùng chung cho cả Thêm mới và Sửa)
    @PostMapping("/luu")
    public String luuDichVu(@ModelAttribute("dichVu") DichVu dichVu) {
        dichVuService.luuDichVu(dichVu);
        return "redirect:/admin/dich-vu"; // Lưu xong quay về trang danh sách
    }

    // 4. Hiển thị form cập nhật (sửa) dịch vụ
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        DichVu dichVu = dichVuService.layDichVuTheoId(id);
        if (dichVu == null) {
            return "redirect:/admin/dich-vu"; // Nếu nhập ID bậy trên URL thì quay về list
        }
        model.addAttribute("dichVu", dichVu);
        // Tái sử dụng lại form tạo mới để làm form sửa
        return "admin/dich_vu_form";
    }

    // 5. Xử lý xóa dịch vụ
    @GetMapping("/xoa/{id}")
    public String xoaDichVu(@PathVariable("id") Long id) {
        dichVuService.xoaDichVu(id);
        return "redirect:/admin/dich-vu";
    }
}