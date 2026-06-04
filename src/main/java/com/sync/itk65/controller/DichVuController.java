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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/dich-vu")
public class DichVuController {

    @Autowired
    private DichVuService dichVuService;

    // 1. Hiển thị danh sách dịch vụ
    // Phục vụ cho menu: <a href="/admin/dich-vu" class="nav-link text-white">🛠️ Quản lý Dịch vụ</a>
    @GetMapping
    public String danhSachDichVu(Model model) {
        model.addAttribute("danhSachDichVu", dichVuService.layTatCaDichVu());
        return "admin/dich_vu_list"; // File view: templates/admin/dich_vu_list.html
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