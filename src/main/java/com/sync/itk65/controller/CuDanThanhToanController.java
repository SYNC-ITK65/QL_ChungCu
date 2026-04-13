package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.service.ThanhToanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cudan/thanh-toan")
public class CuDanThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    @GetMapping("/lich-su")
    public String xemLichSuThanhToan(HttpSession session, Model model) {
        CuDan cuDan = (CuDan) session.getAttribute("nguoiDungDangNhap");
        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }

        model.addAttribute(
                "danhSachThanhToan",
                thanhToanService.layLichSuTheoCanHo(cuDan.getCanHo().getId())
        );
        return "cudan/thanh_toan_lich_su";
    }
}
