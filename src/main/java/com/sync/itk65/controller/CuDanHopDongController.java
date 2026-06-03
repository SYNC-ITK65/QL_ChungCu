package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.HopDong;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.HopDongService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cudan/hop-dong")
public class CuDanHopDongController {

    @Autowired
    private HopDongService hopDongService;

    @Autowired
    private CuDanService cuDanService;

    @GetMapping("/cua-toi")
    public String xemHopDongCuaToi(HttpSession session, RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null) {
            return "redirect:/";
        }

        List<HopDong> hopDongs = hopDongService.layHopDongCuaCuDan(cuDan.getId());
        if (hopDongs.isEmpty()) {
            return "cudan/hop_dong_chi_tiet";
        }

        return "redirect:/cudan/hop-dong/chi-tiet/" + hopDongs.get(0).getId();
    }

    @GetMapping("/chi-tiet/{id}")
    public String xemChiTietHopDong(@PathVariable("id") Long id, HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null) {
            return "redirect:/";
        }

        HopDong hopDong = hopDongService.layHopDongTheoIdVaCuDan(id, cuDan.getId()).orElse(null);
        if (hopDong == null) {
            return "cudan/hop_dong_chi_tiet";
        }

        model.addAttribute("hopDong", hopDong);
        model.addAttribute("cuDan", cuDan);
        return "cudan/hop_dong_chi_tiet";
    }
}
