package com.sync.itk65.controller;

import com.sync.itk65.service.CanHoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sync.itk65.entity.CanHo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/can-ho") // Đường dẫn trên web: localhost:8080/admin/can-ho
public class CanHoController {

    @Autowired
    private CanHoService canHoService;

    // Hàm hiển thị danh sách căn hộ (Có phân trang, tìm kiếm)
    @GetMapping
    public String hienThiDanhSach(
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Double dienTich,
            @RequestParam(required = false) Integer tang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        
        org.springframework.data.domain.Page<CanHo> pageCanHo = canHoService.timKiemCanHo(trangThai, dienTich, tang, page, size);
        
        model.addAttribute("danhSachCanHo", pageCanHo.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageCanHo.getTotalPages());
        // Giữ lại tham số tìm kiếm để bind lại form
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("dienTich", dienTich);
        model.addAttribute("tang", tang);

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

    // Xóa căn hộ
    @GetMapping("/xoa/{id}")
    public String xoaCanHo(@PathVariable("id") Long id) {
        canHoService.xoaCanHo(id);
        return "redirect:/admin/can-ho";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel() {
        byte[] bytes = canHoService.xuatExcelDanhSachCanHo();

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_can_ho_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }

}