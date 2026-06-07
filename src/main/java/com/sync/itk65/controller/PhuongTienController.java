package com.sync.itk65.controller;

import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.service.PhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/phuong-tien") // Dùng chung 1 đường dẫn như CanHoController
public class PhuongTienController {

    @Autowired
    private PhuongTienService phuongTienService;

    @Autowired
    private org.springframework.context.MessageSource messageSource;

    // 2. Hàm lưu dữ liệu từ form gửi lên (Không dùng JS)
    @PostMapping("/luu")
    public String luuPhuongTien(@ModelAttribute("xeMoi") PhuongTien xe, 
                                java.util.Locale locale, 
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.dangKyXe(xe);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", 
                    messageSource.getMessage("pt.msg.them_thanh_cong", null, "Thêm xe thành công!", locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", 
                    messageSource.getMessage("pt.msg.them_loi", null, "Thêm thất bại! Vui lòng kiểm tra lại Mã Căn Hộ có tồn tại không, hoặc Biển số xe đã bị trùng.", locale));
        }
        return "redirect:/admin/phuong-tien";
    }

    // 3. Hàm xóa xe (Chỉ cần bấm link là xóa)
    @GetMapping("/xoa/{id}")
    public String xoaPhuongTien(@PathVariable("id") Long id, 
                                java.util.Locale locale, 
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.huyGuiXe(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", 
                    messageSource.getMessage("pt.msg.xoa_thanh_cong", null, "Đã xóa phương tiện thành công.", locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", 
                    messageSource.getMessage("pt.msg.xoa_loi", new Object[]{e.getMessage()}, "Lỗi khi xóa: " + e.getMessage(), locale));
        }
        return "redirect:/admin/phuong-tien";
    }

    @GetMapping("/duyet/{id}")
    public String duyetXe(@PathVariable("id") Long id, 
                          java.util.Locale locale, 
                          org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.duyetXe(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", 
                    messageSource.getMessage("pt.msg.duyet_thanh_cong", null, "Đã duyệt phương tiện thành công!", locale));
        } catch (Exception e) {
            String errorMsg;
            if (e.getMessage() != null && e.getMessage().contains("Hầm xe đã đầy")) {
                errorMsg = messageSource.getMessage("pt.msg.duyet_loi_day", null, e.getMessage(), locale);
            } else {
                errorMsg = messageSource.getMessage("pt.msg.duyet_loi", new Object[]{e.getMessage()}, "Lỗi khi duyệt xe: " + e.getMessage(), locale);
            }
            redirectAttributes.addFlashAttribute("thongBaoLoi", errorMsg);
        }
        return "redirect:/admin/phuong-tien";
    }

    @GetMapping("/tu-choi/{id}")
    public String tuChoiXe(@PathVariable("id") Long id, 
                           java.util.Locale locale, 
                           org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.tuChoiXe(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", 
                    messageSource.getMessage("pt.msg.tu_choi_thanh_cong", null, "Đã từ chối đăng ký phương tiện.", locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", 
                    messageSource.getMessage("pt.msg.tu_choi_loi", new Object[]{e.getMessage()}, "Lỗi khi từ chối xe: " + e.getMessage(), locale));
        }
        return "redirect:/admin/phuong-tien";
    }

    @GetMapping("/sua/{id}")
    public String suaLaiTrangThaiChoDuyet(@PathVariable("id") Long id, 
                                         java.util.Locale locale, 
                                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.suaLaiTrangThaiChoDuyet(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", 
                    messageSource.getMessage("pt.msg.hoan_tac_thanh_cong", null, "Đã hoàn tác: Chuyển xe về lại trạng thái Chờ duyệt.", locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", 
                    messageSource.getMessage("pt.msg.hoan_tac_loi", new Object[]{e.getMessage()}, "Lỗi: " + e.getMessage(), locale));
        }
        return "redirect:/admin/phuong-tien";
    }
    // Tiếp nhận tham số tìm kiếm, gọi Service tương ứng và trả dữ liệu kèm từ khóa về giao diện
    @GetMapping
    public String hienThiTrang(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String tuKhoa,
                               @RequestParam(required = false) String trangThai,
                               @RequestParam(required = false) String loaiXe) {

        Page<PhuongTien> trangDuLieu;

        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) ||
                (trangThai != null && !trangThai.trim().isEmpty()) ||
                (loaiXe != null && !loaiXe.trim().isEmpty())) {
            trangDuLieu = phuongTienService.timKiemPhuongTien(tuKhoa, trangThai, loaiXe, page, size);
        } else {
            trangDuLieu = phuongTienService.danhSachXe(page, size);
        }

        model.addAttribute("danhSachXe", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);

        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("loaiXe", loaiXe);

        model.addAttribute("xeMoi", new PhuongTien());

        model.addAttribute("soLuongXeDaDuyet", phuongTienService.getSoLuongXeDaDuyet());
        model.addAttribute("soLuongXeChoDuyet", phuongTienService.getSoLuongXeChoDuyet());
        model.addAttribute("tongSoXe", phuongTienService.getTongSoXe());

        return "admin/phuong-tien";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel(@RequestParam(required = false) String tuKhoa,
                                            @RequestParam(required = false) String trangThai,
                                            @RequestParam(required = false) String loaiXe) {
        byte[] bytes = phuongTienService.xuatExcelDanhSachPhuongTien(tuKhoa, trangThai, loaiXe);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_phuong_tien_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}