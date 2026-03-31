package com.sync.itk65.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.NguoiDungService;

@Controller
@RequestMapping("/admin/nguoi-dung") // Đường dẫn trên web: localhost:8080/admin/nguoi-dung

public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    // Hàm hiển thị danh sách người dùng
    @GetMapping
    public String hienThiDanhSach(Model model) {
        // Nhờ Service lấy dữ liệu từ Database
        model.addAttribute("danhSachNguoiDung", nguoiDungService.layTatCaNguoiDung());

        // Trả về tên file HTML giao diện (sẽ tạo sau trong thư mục resources/templates)
        return "admin/nguoi_dung_list";
    }

    // Hàm hiển thị form tạo mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        NguoiDung nguoiDung = new NguoiDung();
        model.addAttribute("nguoiDung", nguoiDung);
        return "admin/nguoi_dung_form";
    }

    // Hàm hiển thị form cập nhật (Sửa)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        NguoiDung nguoiDung = nguoiDungService.layNguoiDungTheoId(id);
        model.addAttribute("nguoiDung", nguoiDung);
        return "admin/nguoi_dung_form";
    }

    // Hàm xử lý lưu dữ liệu từ form
    @PostMapping("/luu")
    public String luuNguoiDung(@ModelAttribute("nguoiDung") NguoiDung nguoiDung) {
        nguoiDungService.luuNguoiDung(nguoiDung);
        return "redirect:/admin/nguoi-dung"; // Quay về trang danh sách sau khi lưu
    }

}
