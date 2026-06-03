package com.sync.itk65.controller;

import com.sync.itk65.entity.*;
import com.sync.itk65.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/duyet-yeu-cau")
public class AdminKhachThamController {

    @Autowired
    private DangKyKhachThamService khachThamService;

    @GetMapping
    public String hienThiTrangDuyet(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String tuKhoa,
                                    @RequestParam(required = false) String trangThai) {

        Page<DangKyKhachTham> trangDuLieu;

        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) || (trangThai != null && !trangThai.trim().isEmpty())) {
            trangDuLieu = khachThamService.timKiemKhachTham(tuKhoa, trangThai, page, size);
        } else {
            trangDuLieu = khachThamService.layTatCa(page, size);
        }

        model.addAttribute("listKhach", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);

        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);

        StringBuilder queryString = new StringBuilder();
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) queryString.append("tuKhoa=").append(tuKhoa).append("&");
        if (trangThai != null && !trangThai.trim().isEmpty()) queryString.append("trangThai=").append(trangThai).append("&");

        String finalQuery = queryString.toString();
        if (finalQuery.endsWith("&")) {
            finalQuery = finalQuery.substring(0, finalQuery.length() - 1);
        }
        model.addAttribute("queryString", finalQuery);

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

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel(@RequestParam(required = false) String tuKhoa,
                                            @RequestParam(required = false) String trangThai) {
        byte[] bytes = khachThamService.xuatExcelKhachTham(tuKhoa, trangThai);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_khach_tham_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}