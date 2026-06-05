package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.KienHang;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.KienHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cudan/kien-hang")
public class CuDanKienHangController {

    @Autowired
    private KienHangService kienHangService;

    @Autowired
    private CuDanService cuDanService;

    @GetMapping("")
    public String danhSach(HttpSession session, Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/login";
        }

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        if (cuDan != null && cuDan.getCanHo() != null) {
            Page<KienHang> trangDuLieu = kienHangService.layKienHangTheoCanHo(cuDan.getCanHo().getId(), page, size);
            model.addAttribute("danhSachKienHang", trangDuLieu.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", trangDuLieu.getTotalPages());
            model.addAttribute("size", size);
        } else {
            model.addAttribute("danhSachKienHang", new ArrayList<>());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
            model.addAttribute("size", size);
        }

        return "cudan/kien_hang";
    }
}
