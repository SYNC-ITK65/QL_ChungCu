package com.sync.itk65.controller;

import com.sync.itk65.entity.*;
import com.sync.itk65.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/duyet-yeu-cau")
public class Admin_LeTanController {

    @Autowired
    private DangKyKhachThamService khachThamService;

    @GetMapping
    public String hienThiTrangDuyet(Model model) {
        // Gửi toàn bộ danh sách khách thăm sang giao diện trong 1 biến duy nhất
        List<DangKyKhachTham> listKhach = khachThamService.layTatCa();
        model.addAttribute("listKhach", listKhach);

        return "admin/duyet_yeu_cau";
    }

    @GetMapping("/khach-tham/{id}/{trangThai}")
    public String duyetKhachTham(@PathVariable Long id, @PathVariable String trangThai) {
        DangKyKhachTham khach = khachThamService.timTheoId(id);
        if (khach != null) {
            if ("cho-duyet".equals(trangThai)) {
                // Tính năng Hoàn tác: Đưa về lại trạng thái Chờ duyệt và xóa thời gian duyệt
                khach.setTrangThai("Chờ duyệt");
                khach.setThoiGianDuyet(null);
            } else {
                // Xử lý Duyệt hoặc Không duyệt bình thường
                khach.setTrangThai(trangThai.equals("duyet") ? "Đã duyệt" : "Không duyệt");
                khach.setThoiGianDuyet(LocalDateTime.now());
            }
            khachThamService.luu(khach);
        }
        return "redirect:/admin/duyet-yeu-cau";
    }
}