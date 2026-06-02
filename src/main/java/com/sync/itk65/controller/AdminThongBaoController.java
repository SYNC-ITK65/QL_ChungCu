package com.sync.itk65.controller;

import com.sync.itk65.entity.ThongBao;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.ThongBaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/thong-bao")
public class AdminThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private CanHoService canHoService;

    @GetMapping
    public String hienThiDanhSach(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String tuKhoa,
                                  @RequestParam(required = false) Integer loai) {
        Page<ThongBao> trangDuLieu = thongBaoService.searchThongBao(tuKhoa, loai, page, size);
        model.addAttribute("danhSachThongBao", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("loai", loai);
        return "admin/thong_bao_list";
    }

    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        model.addAttribute("thongBao", new ThongBao());
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/thong_bao_form";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        ThongBao thongBao = thongBaoService.getThongBaoById(id);
        if (thongBao != null) {
            model.addAttribute("thongBao", thongBao);
        } else {
            return "redirect:/admin/thong-bao";
        }
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/thong_bao_form";
    }

    @PostMapping("/luu")
    public String luuThongBao(@Valid @ModelAttribute("thongBao") ThongBao thongBao, BindingResult bindingResult, Model model) {
        if (thongBao.getId() != null) {
            ThongBao existing = thongBaoService.getThongBaoById(thongBao.getId());
            if (existing != null) {
                thongBao.setNgayDang(existing.getNgayDang());
            }
        } else {
            thongBao.setNgayDang(java.time.LocalDateTime.now());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
            return "admin/thong_bao_form";
        }

        try {
            thongBaoService.saveThongBao(thongBao);
            return "redirect:/admin/thong-bao";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
            return "admin/thong_bao_form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi lưu thông báo!");
            model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
            return "admin/thong_bao_form";
        }
    }

    @GetMapping("/xoa/{id}")
    public String xoaThongBao(@PathVariable("id") Long id) {
        thongBaoService.deleteThongBao(id);
        return "redirect:/admin/thong-bao";
    }
}
