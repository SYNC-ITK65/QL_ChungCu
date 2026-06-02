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

@Controller
@RequestMapping("/admin/kien-hang")
public class AdminKienHangController {

    @Autowired
    private KienHangService kienHangService;

    @Autowired
    private CanHoService canHoService;

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
            ra.addFlashAttribute("thongBaoLoi", "Không tìm thấy đơn thư với ID: " + id);
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
        String msg = kienHang.getId() != null ? "Cập nhật đơn thư thành công!" : "Thêm đơn thư mới thành công!";
        ra.addFlashAttribute("thongBaoThanhCong", msg);
        return "redirect:/admin/kien-hang";
    }

    // Xóa kiện hàng
    @GetMapping("/xoa/{id}")
    public String xoaKienHang(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            kienHangService.xoaKienHang(id);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã xóa đơn thư thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi khi xóa đơn thư: " + e.getMessage());
        }
        return "redirect:/admin/kien-hang";
    }

    @GetMapping("/xac-nhan/{id}")
    public String xacNhanDaNhan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            kienHangService.xacNhanDaNhan(id);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã xác nhận cư dân nhận đơn thư!");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/kien-hang";
    }
}
