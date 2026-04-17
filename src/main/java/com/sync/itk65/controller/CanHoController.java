package com.sync.itk65.controller;

import com.sync.itk65.service.CanHoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sync.itk65.entity.CanHo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/can-ho") // Đường dẫn trên web: localhost:8080/admin/can-ho
public class CanHoController {

    @Autowired
    private CanHoService canHoService;

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

    // Hàm xử lý lưu dữ liệu từ form và bắt lỗi Validation bằng try-catch
    @PostMapping("/luu")
    public String luuCanHo(@ModelAttribute("canHo") CanHo canHo, RedirectAttributes ra) {
        try {
            canHoService.luuCanHo(canHo);
            return "redirect:/admin/can-ho"; // Quay về trang danh sách sau khi lưu thành công
        } catch (IllegalArgumentException e) {
            // Ném lỗi Validation về giao diện
            ra.addFlashAttribute("errorMessage", e.getMessage());
            // Quay lại trang Cập nhật hoặc Thêm mới
            if (canHo.getId() != null) {
                return "redirect:/admin/can-ho/sua/" + canHo.getId();
            } else {
                return "redirect:/admin/can-ho/tao-moi";
            }
        }
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