package com.sync.itk65.controller;

import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.service.PhanAnhService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/phan-anh")
public class PhanAnhController {

    @Autowired
    private PhanAnhService phanAnhService;

    @GetMapping
    public String listPhanAnh(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String tuKhoa,
                              @RequestParam(required = false) String trangThai) {

        Page<PhanAnh> trangDuLieu;

        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) ||
                (trangThai != null && !trangThai.trim().isEmpty())) {
            trangDuLieu = phanAnhService.timKiemPhanAnh(tuKhoa, trangThai, page, size);
        } else {
            trangDuLieu = phanAnhService.findAll(page, size);
        }

        model.addAttribute("phanAnhs", trangDuLieu.getContent());
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

        return "admin/phan_anh_list";
    }

    // 2. Mở form xem chi tiết và cập nhật trạng thái
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        PhanAnh phanAnh = phanAnhService.findById(id);
        if (phanAnh != null) {
            model.addAttribute("phanAnh", phanAnh);
            return "admin/phan_anh_form";
        }
        return "redirect:/admin/phan-anh";
    }

    // 3. Lưu lại trạng thái mới (Ví dụ: Đã sửa xong)
    @PostMapping("/save")
    public String savePhanAnh(@ModelAttribute("phanAnh") PhanAnh phanAnhForm) {
        // Lấy bản ghi gốc dưới database lên
        PhanAnh phanAnhGoc = phanAnhService.findById(phanAnhForm.getId());

        if (phanAnhGoc != null) {
            // ADMIN CẬP NHẬT TRẠNG THÁI VÀ PHẢN HỒI
            phanAnhGoc.setTrangThai(phanAnhForm.getTrangThai());
            phanAnhGoc.setPhanHoi(phanAnhForm.getPhanHoi());
            
            // Nếu có phản hồi mới thì set ngày phản hồi
            if (phanAnhForm.getPhanHoi() != null && !phanAnhForm.getPhanHoi().trim().isEmpty()) {
                if (phanAnhGoc.getNgayPhanHoi() == null) {
                    phanAnhGoc.setNgayPhanHoi(java.time.LocalDateTime.now());
                }
            }
            phanAnhService.save(phanAnhGoc);
        }
        return "redirect:/admin/phan-anh";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel(@RequestParam(required = false) String tuKhoa,
                                            @RequestParam(required = false) String trangThai) {
        byte[] bytes = phanAnhService.xuatExcelPhanAnh(tuKhoa, trangThai);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_phan_anh_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}