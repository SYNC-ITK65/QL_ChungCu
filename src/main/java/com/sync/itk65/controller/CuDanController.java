package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CuDanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/cu-dan")
public class CuDanController {
    @Autowired
    private CuDanService cuDanService;
    @Autowired
    private CanHoService canHoService;

    // Xem danh sách cư dân (Có thể lọc theo căn hộ)
    @GetMapping
    public String hienThiDanhSach(@RequestParam(required = false) Long canHoId, Model model) {
        List<CuDan> danhSach;
        if (canHoId != null) {
            danhSach = cuDanService.timTheoCanHo(canHoId);
            model.addAttribute("canHoHienTai", canHoService.layCanHoTheoId(canHoId));
        } else {
            danhSach = cuDanService.layTatCaCuDan();
        }
        model.addAttribute("danhSachCuDan", danhSach);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/cu_dan_list";
    }

    // Form thêm mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(@RequestParam(required = false) Long canHoId, Model model) {
        CuDan cuDan = new CuDan();
        if (canHoId != null) {
            cuDan.setCanHo(canHoService.layCanHoTheoId(canHoId));
        } else {
            cuDan.setCanHo(new com.sync.itk65.entity.CanHo()); // Đảm bảo CanHo không null để tránh lỗi Thymeleaf
        }
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/cu_dan_form";
    }

    // Form sửa cư dân
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        CuDan cuDan = cuDanService.layCuDanTheoId(id);
        if (cuDan == null) return "redirect:/admin/cu-dan";
        if (cuDan.getCanHo() == null) cuDan.setCanHo(new com.sync.itk65.entity.CanHo());
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/cu_dan_form";
    }

    // Xử lý lưu (Lưu chung cả user info và Resident info nhờ kế thừa)
    @PostMapping("/luu")
    public String luuCuDan(@ModelAttribute("cuDan") CuDan cuDan) {
        // Xử lý lỗi Căn hộ rỗng -> Hibernate không lưu được đối tượng có ID null
        if (cuDan.getCanHo() != null && cuDan.getCanHo().getId() == null) {
            cuDan.setCanHo(null);
        }

        // Tự động gán vai trò dựa vào mối quan hệ (Ví dụ)
        if ("Chủ Hộ".equals(cuDan.getMoiQuanHe())) {
            cuDan.setVaiTro(2);
        } else {
            cuDan.setVaiTro(3);
        }

        // Giữ lại mật khẩu cũ khi sửa, đặt mặc định khi tạo mới
        if (cuDan.getId() != null) {
            CuDan cuDanCu = cuDanService.layCuDanTheoId(cuDan.getId());
            if (cuDanCu != null) {
                cuDan.setMatKhauMaHoa(cuDanCu.getMatKhauMaHoa());
            }
        } else if (cuDan.getMatKhauMaHoa() == null || cuDan.getMatKhauMaHoa().isEmpty()) {
            cuDan.setMatKhauMaHoa("123456");
        }

        cuDanService.luuCuDan(cuDan);
        return "redirect:/admin/cu-dan";
    }

    // Xóa cư dân chuyển đi
    @GetMapping("/xoa/{id}")
    public String xoaCuDan(@PathVariable("id") Long id) {
        cuDanService.xoaCuDan(id);
        return "redirect:/admin/cu-dan";
    }
}