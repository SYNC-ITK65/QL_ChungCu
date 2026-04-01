package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.service.ChiSoHangThangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/chi-so")
public class ChiSoHangThangController {

    @Autowired
    private ChiSoHangThangService chiSoHangThangService;

    @Autowired
    private CanHoRepository canHoRepository;

    // Hiển thị danh sách chỉ số
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("danhSachChiSo", chiSoHangThangService.layTatCaChiSo());
        return "admin/chi_so_list"; // Cần tạo file view chi_so_list.html
    }

    // Hiển thị form thêm mới chỉ số
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        model.addAttribute("chiSo", new ChiSoHangThang());
        // Load danh sách căn hộ để chọn trong dropdown
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());
        return "admin/chi_so_form"; // Cần tạo file view chi_so_form.html
    }

    // Xử lý lưu thông tin chỉ số
    @PostMapping("/luu")
    public String luuChiSoHangThang(@ModelAttribute("chiSoHangThang") ChiSoHangThang chiSoHangThang,
                                    @RequestParam("canHoId") Long canHoId) {
        // Gán căn hộ vào chỉ số dựa trên ID được chọn từ form
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