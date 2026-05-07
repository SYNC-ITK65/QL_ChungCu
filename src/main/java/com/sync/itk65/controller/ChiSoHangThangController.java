package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.service.ChiSoHangThangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/chi-so")
public class ChiSoHangThangController {

    @Autowired
    private ChiSoHangThangService chiSoHangThangService;

    @Autowired
    private CanHoRepository canHoRepository;

    // Hiển thị danh sách chỉ số (có lọc, sắp xếp ngày mới nhất)
    @GetMapping
    public String hienThiDanhSach(Model model,
                                  @RequestParam(required = false) String maCanHo,
                                  @RequestParam(required = false) Long canHoId,
                                  @RequestParam(required = false) Integer thang,
                                  @RequestParam(required = false) Integer nam) {
        List<ChiSoHangThang> danhSachChiSo;

        String maCanHoFilter = (maCanHo != null && !maCanHo.trim().isEmpty()) ? maCanHo.trim() : null;
        boolean coBoLoc = maCanHoFilter != null || canHoId != null || thang != null || nam != null;

        if (coBoLoc) {
            danhSachChiSo = chiSoHangThangService.timKiemChiSo(maCanHoFilter, canHoId, thang, nam);
        } else {
            danhSachChiSo = chiSoHangThangService.layTatCaChiSo();
        }

        model.addAttribute("danhSachChiSo", danhSachChiSo);
        model.addAttribute("maCanHo", maCanHo != null ? maCanHo : "");
        model.addAttribute("canHoId", canHoId);
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());
        return "admin/chi_so_list";
    }

    // Hiển thị form thêm mới chỉ số
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        model.addAttribute("chiSo", new ChiSoHangThang());
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());
        return "admin/chi_so_form";
    }

    // Xử lý lưu thông tin chỉ số
    @PostMapping("/luu")
    public String luuChiSoHangThang(@ModelAttribute("chiSo") ChiSoHangThang chiSoHangThang,
                                    @RequestParam("canHoId") Long canHoId) {
        if (chiSoHangThang.getNgayGhiNhan() == null) {
            chiSoHangThang.setNgayGhiNhan(java.time.LocalDate.now());
        }

        CanHo canHo = new CanHo();
        canHo.setId(canHoId);
        chiSoHangThang.setCanHo(canHo);

        chiSoHangThangService.luuChiSo(chiSoHangThang);
        return "redirect:/admin/chi-so";
    }

    // Xóa chỉ số
    @GetMapping("/xoa/{id}")
    public String xoaChiSo(@PathVariable("id") Long id) {
        chiSoHangThangService.xoaChiSo(id);
        return "redirect:/admin/chi-so";
    }
}