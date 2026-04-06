package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.KienHang;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.KienHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/kien-hang")
public class AdminKienHangController {

    @Autowired
    private KienHangService kienHangService;

    @Autowired
    private CanHoService canHoService;

    @GetMapping("")
    public String danhSach(Model model) {
        model.addAttribute("danhSachKienHang", kienHangService.layTatCaKienHang());
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

    @PostMapping("/luu")
    public String luuKienHang(@ModelAttribute("kienHang") KienHang kienHang, RedirectAttributes ra) {
        kienHangService.luuKienHang(kienHang);
        ra.addFlashAttribute("thongBaoThanhCong", "Thêm kiện hàng mới thành công!");
        return "redirect:/admin/kien-hang";
    }

    @GetMapping("/xac-nhan/{id}")
    public String xacNhanDaNhan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            kienHangService.xacNhanDaNhan(id);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã xác nhận cư dân nhận hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/kien-hang";
    }
}
