package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CuDanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cudan")
public class CuDanPortalController {

    @Autowired
    private CuDanService cuDanService;

    @GetMapping("/thong-tin")
    public String xemThongTin(HttpSession session, Model model) {
        // 1. Kiểm tra xem đã đăng nhập chưa
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null || user.getVaiTro() == 1) { // Chưa đăng nhập hoặc là Admin thì đuổi ra login
            return "redirect:/";
        }

        // 2. Ép kiểu hoặc gọi DB lấy thông tin chi tiết Cư Dân
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        model.addAttribute("cuDan", cuDan);

        return "cudan/thong_tin";
    }
}