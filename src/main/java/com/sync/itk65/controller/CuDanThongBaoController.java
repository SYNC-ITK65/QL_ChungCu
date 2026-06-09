package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.entity.ThongBao;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.ThongBaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
@RequestMapping("/cudan/thong-bao")
public class CuDanThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/bang-tin")
    public String hienThiBangTin(HttpSession session, Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "6") int size) {
        // Lấy thông tin cư dân đang đăng nhập
        String maCanHo = null;
        String tang = null;

        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user != null) {
            CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
            if (cuDan != null && cuDan.getCanHo() != null) {
                maCanHo = cuDan.getCanHo().getMaCanHo();
                tang = cuDan.getCanHo().getTang() != null ? String.valueOf(cuDan.getCanHo().getTang()) : null;
            }
        }

        // Lọc thông báo loại 1 (Bảng tin) theo đối tượng cư dân, có phân trang
        Page<ThongBao> trangDuLieu = thongBaoService.locThongBaoTheoCuDan(1, maCanHo, tang, page, size);

        model.addAttribute("danhSach", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("tieuDeTrang", messageSource.getMessage("tb.loai.tin_tuc", null, "Bảng tin chung", locale));
        model.addAttribute("tieuDeTrangKey", "tb.loai.tin_tuc");
        return "cudan/bang_tin";
    }

    @GetMapping("/cam-nang")
    public String hienThiCamNang(HttpSession session, Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "6") int size) {
        // Lấy thông tin cư dân đang đăng nhập
        String maCanHo = null;
        String tang = null;

        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user != null) {
            CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
            if (cuDan != null && cuDan.getCanHo() != null) {
                maCanHo = cuDan.getCanHo().getMaCanHo();
                tang = cuDan.getCanHo().getTang() != null ? String.valueOf(cuDan.getCanHo().getTang()) : null;
            }
        }

        // Lọc thông báo loại 2 (Cẩm nang) theo đối tượng cư dân, có phân trang
        Page<ThongBao> trangDuLieu = thongBaoService.locThongBaoTheoCuDan(2, maCanHo, tang, page, size);

        model.addAttribute("danhSach", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("tieuDeTrang", messageSource.getMessage("tb.ds.cam_nang_cu_dan", null, "Cẩm nang cư dân", locale));
        model.addAttribute("tieuDeTrangKey", "tb.ds.cam_nang_cu_dan");
        return "cudan/cam_nang";
    }

    @GetMapping("/chi-tiet/{id}")
    public String hienThiChiTiet(@PathVariable("id") Long id, Model model) {
        model.addAttribute("thongBao", thongBaoService.getThongBaoById(id));
        return "cudan/chi_tiet_thong_bao";
    }
}
