package com.sync.itk65.controller;

import com.sync.itk65.entity.*;
import com.sync.itk65.repository.*;
import com.sync.itk65.service.KhaoSatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin/khao-sat")
public class AdminKhaoSatController {

    @Autowired private KhaoSatService khaoSatService;
    @Autowired private CuDanRepository cuDanRepo;
    @Autowired private LichSuVoteRepository lichSuVoteRepo;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size) {
        Page<KhaoSat> trangKhaoSat = khaoSatService.layTatCa(page, size);
        model.addAttribute("listKhaoSat", trangKhaoSat.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangKhaoSat.getTotalPages());
        model.addAttribute("size", size);
        return "admin/khao_sat_list";
    }

    @GetMapping("/tao-moi")
    public String showFormCreate(Model model) {
        KhaoSat ks = new KhaoSat();
        // Khởi tạo sẵn 2 ô rỗng cho giao diện dễ nhập
        ks.setDanhSachLuaChon(new ArrayList<>());
        ks.getDanhSachLuaChon().add(new LuaChonKhaoSat());
        ks.getDanhSachLuaChon().add(new LuaChonKhaoSat());
        model.addAttribute("khaoSat", ks);
        return "admin/khao_sat_form";
    }

    @GetMapping("/sua/{id}")
    public String showFormEdit(@PathVariable("id") Long id, Model model) {
        KhaoSat ks = khaoSatService.findById(id);
        model.addAttribute("khaoSat", ks);
        return "admin/khao_sat_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("khaoSat") KhaoSat khaoSat, RedirectAttributes ra) {
        // Kiểm tra an toàn trước khi xử lý list
        if (khaoSat.getDanhSachLuaChon() != null) {
            // Thêm điều kiện "lc == null" để lọc bỏ các index bị đứt đoạn do nút Xóa trên HTML
            khaoSat.getDanhSachLuaChon().removeIf(lc ->
                    lc == null ||
                            lc.getNoiDungLuaChon() == null ||
                            lc.getNoiDungLuaChon().trim().isEmpty()
            );
        }
        khaoSatService.luuKhaoSat(khaoSat);
        ra.addFlashAttribute("msg", "Lưu khảo sát thành công!");
        return "redirect:/admin/khao-sat";
    }

    @GetMapping("/xoa/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        String rs = khaoSatService.xoaKhaoSat(id);
        if(rs.equals("SUCCESS")) ra.addFlashAttribute("msg", "Đã xóa thành công!");
        else ra.addFlashAttribute("err", rs);
        return "redirect:/admin/khao-sat";
    }

    @GetMapping("/ket-thuc/{id}")
    public String stop(@PathVariable("id") Long id) {
        khaoSatService.ketThucSom(id);
        return "redirect:/admin/khao-sat";
    }

    @GetMapping("/chi-tiet/{id}")
    public String detailAdmin(@PathVariable("id") Long id, Model model) {
        model.addAttribute("khaoSat", khaoSatService.findById(id));
        model.addAttribute("tatCaCuDan", cuDanRepo.findAll());
        model.addAttribute("lichSuList", lichSuVoteRepo.findByKhaoSatIdOrderByThoiGianVoteDesc(id));
        return "admin/khao_sat_detail";
    }
}