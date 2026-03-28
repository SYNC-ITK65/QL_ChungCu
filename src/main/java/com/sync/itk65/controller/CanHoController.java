package com.sync.itk65.controller;

import com.sync.itk65.service.CanHoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/can-ho") // Đường dẫn trên web: localhost:8080/admin/can-ho
public class CanHoController {

    @Autowired
    private CanHoService canHoService;

    // Khi người dùng gõ localhost:8080/admin/can-ho lên trình duyệt, hàm này sẽ chạy
    @GetMapping
    public String hienThiDanhSach(Model model) {
        // Nhờ Service lấy dữ liệu từ Database
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());

        // Trả về tên file HTML giao diện (sẽ tạo sau trong thư mục resources/templates)
        return "admin/can_ho_list";
    }
}