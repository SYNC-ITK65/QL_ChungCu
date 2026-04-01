package com.sync.itk65.controller;
import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/hoa-don")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    // Hiển thị danh sách
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("danhSachHoaDon", hoaDonService.layTatCaHoaDon());
        return "admin/hoa_don_list";
    }

    // 1. API Hiển thị form tạo mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        HoaDon hoaDon = new HoaDon();

        hoaDon.setCanHo(new CanHo());

        model.addAttribute("hoaDon", hoaDon);
        return "admin/hoa_don_form";
    }

    // 2. API Xử lý lưu hóa đơn
    @PostMapping("/luu")
    public String luuHoaDon(@ModelAttribute("hoaDon") HoaDon hoaDon,
                            @RequestParam("soDien") Double soDien,
                            @RequestParam("soNuoc") Double soNuoc) {
        // Gọi Service có logic tính toán
        hoaDonService.luuHoaDonCoTinhToan(hoaDon, soDien, soNuoc);
        return "redirect:/admin/hoa-don"; // Quay về trang danh sách sau khi lưu
    }

    // 3. API Xử lý nút Đánh dấu thanh toán
    @GetMapping("/thanh-toan/{id}")
    public String thanhToan(@PathVariable("id") Long id) {
        hoaDonService.danhDauDaThanhToan(id);
        return "redirect:/admin/hoa-don"; // Quay về trang danh sách
    }
}