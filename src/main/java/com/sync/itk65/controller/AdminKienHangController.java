package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.KienHang;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.KienHangService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
@RequestMapping("/admin/kien-hang")
public class AdminKienHangController {

    @Autowired
    private KienHangService kienHangService;

    @Autowired
    private CanHoService canHoService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("")
    public String danhSach(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String maCanHo,
                           @RequestParam(required = false) String trangThai) {
        Page<KienHang> trangDuLieu = kienHangService.searchKienHang(maCanHo, trangThai, page, size);
        model.addAttribute("danhSachKienHang", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("maCanHo", maCanHo);
        model.addAttribute("trangThai", trangThai);
        return "admin/kien_hang_list";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        KienHang kienHang = new KienHang();
        kienHang.setCanHo(new CanHo()); // Initialize to avoid NPE during binding
        model.addAttribute("kienHang", kienHang);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/kien_hang_form";
    }

    // Form sửa kiện hàng - tái sử dụng kien_hang_form.html
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        KienHang kienHang = kienHangService.layTheoId(id);
        if (kienHang == null) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("kh.error.notFound", new Object[]{id}, "Không tìm thấy đơn thư với ID: " + id, locale));
            return "redirect:/admin/kien-hang";
        }
        if (kienHang.getCanHo() == null) {
            kienHang.setCanHo(new CanHo()); // Tránh NPE khi binding Thymeleaf
        }
        model.addAttribute("kienHang", kienHang);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/kien_hang_form";
    }

    @PostMapping("/luu")
    public String luuKienHang(@ModelAttribute("kienHang") KienHang kienHang, RedirectAttributes ra) {
        kienHangService.luuKienHang(kienHang);
        Locale locale = LocaleContextHolder.getLocale();
        String msg = kienHang.getId() != null ? messageSource.getMessage("kh.success.update", null, "Cập nhật đơn thư thành công!", locale) : messageSource.getMessage("kh.success.add", null, "Thêm đơn thư mới thành công!", locale);
        ra.addFlashAttribute("thongBaoThanhCong", msg);
        return "redirect:/admin/kien-hang";
    }

    // Xóa kiện hàng
    @GetMapping("/xoa/{id}")
    public String xoaKienHang(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            kienHangService.xoaKienHang(id);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("kh.success.delete", null, "Đã xóa đơn thư thành công!", locale));
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("kh.error.deleteEx", new Object[]{e.getMessage()}, "Lỗi khi xóa đơn thư: " + e.getMessage(), locale));
        }
        return "redirect:/admin/kien-hang";
    }

    @GetMapping("/xac-nhan/{id}")
    public String xacNhanDaNhan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            kienHangService.xacNhanDaNhan(id);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("kh.success.confirm", null, "Đã xác nhận cư dân nhận đơn thư!", locale));
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("kh.error.confirmEx", new Object[]{e.getMessage()}, "Lỗi: " + e.getMessage(), locale));
        }
        return "redirect:/admin/kien-hang";
    }
}
