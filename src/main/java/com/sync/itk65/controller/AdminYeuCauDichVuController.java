package com.sync.itk65.controller;

import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.service.DatDichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/yeu-cau-dich-vu")
public class AdminYeuCauDichVuController {

    @Autowired
    private DatDichVuService datDichVuService;

    @Autowired
    private org.springframework.context.MessageSource messageSource;

    @GetMapping
    public String danhSachYeuCauDichVu(Model model,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) String tuKhoa,
                                       @RequestParam(required = false) String trangThai) {
        Page<DatDichVu> trangDuLieu;
        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) || (trangThai != null && !trangThai.trim().isEmpty())) {
            trangDuLieu = datDichVuService.timKiemDonDatDichVu(tuKhoa, trangThai, page, size);
        } else {
            trangDuLieu = datDichVuService.layTatCaDonDatDichVu(page, size);
        }

        model.addAttribute("danhSachDonDat", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);

        return "admin/yeu_cau_dich_vu_list";
    }

    @GetMapping("/don-dat/duyet/{id}")
    public String duyetDonDatDichVu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, java.util.Locale locale) {
        try {
            datDichVuService.duyetDonDatDichVu(id);
            String msg = messageSource.getMessage("dv.msg.duyet_thanh_cong", null, "Đã duyệt đăng ký dịch vụ thành công.", locale);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", msg);
        } catch (Exception e) {
            String errorPrefix = messageSource.getMessage("dv.msg.duyet_loi", null, "Lỗi duyệt đăng ký dịch vụ: ", locale);
            redirectAttributes.addFlashAttribute("thongBaoLoi", errorPrefix + e.getMessage());
        }
        return "redirect:/admin/yeu-cau-dich-vu";
    }

    @GetMapping("/don-dat/tu-choi/{id}")
    public String tuChoiDonDatDichVu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, java.util.Locale locale) {
        try {
            datDichVuService.tuChoiDonDatDichVu(id);
            String msg = messageSource.getMessage("dv.msg.tu_choi_thanh_cong", null, "Đã từ chối đăng ký dịch vụ.", locale);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", msg);
        } catch (Exception e) {
            String errorPrefix = messageSource.getMessage("dv.msg.tu_choi_loi", null, "Lỗi từ chối đăng ký dịch vụ: ", locale);
            redirectAttributes.addFlashAttribute("thongBaoLoi", errorPrefix + e.getMessage());
        }
        return "redirect:/admin/yeu-cau-dich-vu";
    }



    @GetMapping("/don-dat/sua/{id}")
    public String suaLaiTrangThaiChoDuyet(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, java.util.Locale locale) {
        try {
            datDichVuService.suaLaiTrangThaiChoDuyet(id);
            String msg = messageSource.getMessage("dv.msg.sua_thanh_cong", null, "Đã chuyển đơn về trạng thái Chờ duyệt.", locale);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", msg);
        } catch (Exception e) {
            String errorPrefix = messageSource.getMessage("dv.msg.sua_loi", null, "Lỗi sửa trạng thái đăng ký dịch vụ: ", locale);
            redirectAttributes.addFlashAttribute("thongBaoLoi", errorPrefix + e.getMessage());
        }
        return "redirect:/admin/yeu-cau-dich-vu";
    }

    @GetMapping("/don-dat/xuat-excel")
    public ResponseEntity<byte[]> xuatExcelDonDat(@RequestParam(required = false) String tuKhoa,
                                                  @RequestParam(required = false) String trangThai) {
        byte[] bytes = datDichVuService.xuatExcelDonDatDichVu(tuKhoa, trangThai);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_dang_ky_dich_vu_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}
