package com.sync.itk65.controller;

import com.sync.itk65.entity.*;
import com.sync.itk65.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/duyet-yeu-cau")
public class AdminLeTanController {

    @Autowired
    private DangKyKhachThamService khachThamService;

    @GetMapping
    public String hienThiTrangDuyet(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Page<DangKyKhachTham> trangDuLieu = khachThamService.layTatCa(page, size);
        model.addAttribute("listKhach", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);

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