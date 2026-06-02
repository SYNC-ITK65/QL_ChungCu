package com.sync.itk65.controller;

import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CloudinaryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sync.itk65.entity.CanHo;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/can-ho") // Đường dẫn trên web: localhost:8080/admin/can-ho
public class CanHoController {

    @Autowired
    private CanHoService canHoService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // Hàm hiển thị danh sách căn hộ (Có phân trang dữ liệu, và các tiêu chí tìm kiếm cơ bản)
    @GetMapping
    public String hienThiDanhSach(
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Double dienTich,
            @RequestParam(required = false) Integer tang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        
        // Gọi service xử lý tìm kiếm và phân trang, nhận về đối tượng Page chứa dữ liệu căn hộ
        org.springframework.data.domain.Page<CanHo> trangDuLieuCanHo = canHoService.timKiemCanHo(trangThai, dienTich, tang, page, size);
        
        // Đưa danh sách căn hộ của trang hiện tại lên giao diện
        model.addAttribute("danhSachCanHo", trangDuLieuCanHo.getContent());
        // Truyền thông tin phân trang (trang hiện tại và tổng số trang) xuống view xử lý nút bấm
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieuCanHo.getTotalPages());
        
        // Giữ lại tham số tìm kiếm cũ trên giao diện phân trang (ngăn chặn mất tham số khi qua trang khác)
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("dienTich", dienTich);
        model.addAttribute("tang", tang);
        model.addAttribute("size", size);

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

    // Hàm xử lý lưu dữ liệu từ form và bắt lỗi Validation bằng Spring BindingResult
    @PostMapping("/luu")
    public String luuCanHo(@Valid @ModelAttribute("canHo") CanHo canHo, BindingResult bindingResult,
                           @RequestParam("fileImage") MultipartFile multipartFile,
                           Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "admin/can_ho_form";
        }
        try {
            // Xử lý upload hình ảnh lên Cloudinary
            if (!multipartFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(multipartFile);
                canHo.setHinhAnh(imageUrl);
            } else {
                // Nếu không chọn file mới khi sửa, giữ lại ảnh cũ
                if (canHo.getId() != null) {
                    CanHo canHoCu = canHoService.layCanHoTheoId(canHo.getId());
                    if (canHoCu != null) {
                        canHo.setHinhAnh(canHoCu.getHinhAnh());
                    }
                }
            }

            canHoService.luuCanHo(canHo);
            ra.addFlashAttribute("thongBaoThanhCong", "Lưu căn hộ thành công!");
            return "redirect:/admin/can-ho"; // Quay về trang danh sách sau khi lưu thành công
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/can_ho_form";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Lỗi khi upload hình ảnh: " + e.getMessage());
            return "admin/can_ho_form";
        }
    }

    // Xóa căn hộ
    @GetMapping("/xoa/{id}")
    public String xoaCanHo(@PathVariable("id") Long id, RedirectAttributes ra) {
        canHoService.xoaCanHo(id);
        ra.addFlashAttribute("thongBaoThanhCong", "Xóa căn hộ thành công!");
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

    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("thongBaoLoi", "Vui lòng chọn file Excel để import!");
            return "redirect:/admin/can-ho";
        }
        try {
            String ketQua = canHoService.importExcelCanHo(file);
            ra.addFlashAttribute("thongBaoThanhCong", ketQua);
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/admin/can-ho";
    }

}