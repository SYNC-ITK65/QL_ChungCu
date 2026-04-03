package com.sync.itk65.controller;

import com.sync.itk65.service.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cudan/thong-bao")
public class CuDanThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @GetMapping("/bang-tin")
    public String hienThiBangTin(Model model) {
        model.addAttribute("danhSach", thongBaoService.getThongBaoByLoai(1));
        model.addAttribute("tieuDeTrang", "Bảng tin chung");
        return "cudan/bang_tin";
    }

    @GetMapping("/cam-nang")
    public String hienThiCamNang(Model model) {
        model.addAttribute("danhSach", thongBaoService.getThongBaoByLoai(2));
        model.addAttribute("tieuDeTrang", "Cẩm nang cư dân");
        return "cudan/cam_nang";
    }

    @GetMapping("/chi-tiet/{id}")
    public String hienThiChiTiet(@PathVariable("id") Long id, Model model) {
        model.addAttribute("thongBao", thongBaoService.getThongBaoById(id));
        return "cudan/chi_tiet_thong_bao";
    }
}
