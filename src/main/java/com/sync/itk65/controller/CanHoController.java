package com.sync.itk65.controller;

import com.sync.itk65.service.CanHoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sync.itk65.entity.CanHo;

@Controller
@RequestMapping("/admin/can-ho") // Đường dẫn trên web: localhost:8080/admin/can-ho
public class CanHoController {

    @Autowired
    private CanHoService canHoService;

    // Hàm hiển thị danh sách căn hộ
    @GetMapping
    public String hienThiDanhSach(Model model) {
        // Nhờ Service lấy dữ liệu từ Database
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        // Trả về tên file HTML giao diện (sẽ tạo sau trong thư mục resources/templates)
        return "admin/can_ho_list";
    }

    // Hàm hiển thị form tạo mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        CanHo canHo = new CanHo();
        model.addAttribute("canHo", canHo);
        return "admin/can_ho_form";
    }

    // Hàm hiển thị form cập nhật (Sửa)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        CanHo canHo = canHoService.layCanHoTheoId(id);
        model.addAttribute("canHo", canHo);
        return "admin/can_ho_form";
    }

    // Hàm xử lý lưu dữ liệu từ form
    @PostMapping("/luu")
    public String luuCanHo(@ModelAttribute("canHo") CanHo canHo) {
        canHoService.luuCanHo(canHo);
        return "redirect:/admin/can-ho"; // Quay về trang danh sách sau khi lưu
    }

}