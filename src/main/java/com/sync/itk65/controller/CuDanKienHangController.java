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
    public String danhSach(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/login";
        }

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        List<KienHang> danhSach = new ArrayList<>();

        if (cuDan != null && cuDan.getCanHo() != null) {
            danhSach = kienHangService.layKienHangTheoCanHo(cuDan.getCanHo().getId());
        }

        model.addAttribute("danhSachKienHang", danhSach);
        return "cudan/kien_hang";
    }
}
