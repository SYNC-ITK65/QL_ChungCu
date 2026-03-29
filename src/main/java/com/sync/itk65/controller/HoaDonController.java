package com.sync.itk65.controller;

import com.sync.itk65.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/hoa-don") // Đường dẫn web
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("danhSachHoaDon", hoaDonService.layTatCaHoaDon());
        return "admin/hoa_don_list"; // File HTML bạn sẽ code và xem lại thiết kế trên Figma để làm cho đẹp
    }
}