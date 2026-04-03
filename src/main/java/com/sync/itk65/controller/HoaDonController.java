package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.service.HoaDonService;
import com.sync.itk65.repository.CanHoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/hoa-don")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private CanHoRepository canHoRepository;

    // Hiển thị danh sách
    @GetMapping
    public String hienThiDanhSach(Model model) {
        model.addAttribute("danhSachHoaDon", hoaDonService.layTatCaHoaDon());
        return "admin/hoa_don_list";
    }

    // Mở trang Form tạo mới
    // 1. API Hiển thị form tạo mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        model.addAttribute("hoaDon", new HoaDon());

        // Gửi danh sách căn hộ sang Form để làm menu xổ xuống
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());

        return "admin/hoa_don_form";
    }

    // Xử lý lưu hóa đơn khi bấm nút Lưu
    @PostMapping("/luu")
    public String luuHoaDon(@ModelAttribute("hoaDon") HoaDon hoaDon,
                            @RequestParam("canHoId") Long canHoId) {
        try {
            CanHo canHo = new CanHo();
            canHo.setId(canHoId);
            hoaDon.setCanHo(canHo);

            // Trigger tính tiền tự động toàn bộ
            hoaDonService.taoHoaDonTuDong(hoaDon);

            return "redirect:/admin/hoa-don";

        } catch (RuntimeException e) {
            // e.getMessage() sẽ trả về "Chưa ghi nhận chỉ số điện nước..."
            return "redirect:/admin/hoa-don/tao-moi?error=" + e.getMessage();
        }
    }

    // API Xử lý khi người dùng bấm nút "Thanh toán"
//    @GetMapping("/thanh-toan/{id}")
//    public String thanhToan(@PathVariable("id") Long id) {
//        hoaDonService.danhDauDaThanhToan(id);
//        return "redirect:/admin/hoa-don"; // Quay về trang danh sách
//    }
}