package com.sync.itk65.controller;
import com.sync.itk65.entity.*;
import com.sync.itk65.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/cudan/khach-tham")
public class CuDanKhachThamController {
    @Autowired private CuDanService cuDanService;
    @Autowired private DangKyKhachThamService khachThamService;

    @GetMapping
    public String trangKhachTham(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) return "redirect:/login";
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        model.addAttribute("lichSuKhach", khachThamService.layLichSuCuaCuDan(cuDan.getId()));
        return "cudan/khach_tham";
    }

    @PostMapping("/luu")
    public String luuKhachTham(HttpSession session, @RequestParam String tenKhach,
                               @RequestParam String cmnd, @RequestParam String bienSoXe,
                               @RequestParam LocalDateTime thoiGianDuKien, RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        DangKyKhachTham khach = new DangKyKhachTham();
        khach.setCuDan(cuDanService.layCuDanTheoId(user.getId()));
        khach.setTenKhach(tenKhach); khach.setCmnd(cmnd);
        khach.setBienSoXe(bienSoXe); khach.setThoiGianDuKien(thoiGianDuKien);
        khach.setTrangThai("Chờ duyệt");
        khachThamService.luu(khach);
        ra.addFlashAttribute("thongBaoThanhCong", "Đăng ký khách thăm thành công!");
        return "redirect:/cudan/khach-tham";
    }
    // Xử lý Cư dân tự Hủy (Xóa) đăng ký khách thăm
    @GetMapping("/xoa/{id}")
    public String huyKhachTham(@PathVariable Long id, RedirectAttributes ra) {
        DangKyKhachTham khach = khachThamService.timTheoId(id);
        // Chỉ cho phép xóa khi còn ở trạng thái "Chờ duyệt"
        if (khach != null && "Chờ duyệt".equals(khach.getTrangThai())) {
            khachThamService.xoa(id);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã hủy đăng ký khách thăm thành công.");
        } else {
            ra.addFlashAttribute("thongBaoLoi", "Không thể hủy vì yêu cầu đã được Lễ tân xử lý!");
        }
        return "redirect:/cudan/khach-tham";
    }

    // Xử lý Cư dân tự Sửa thông tin khách thăm
    @PostMapping("/sua")
    public String suaKhachTham(@RequestParam Long id,
                               @RequestParam String tenKhach,
                               @RequestParam String cmnd,
                               @RequestParam String bienSoXe,
                               @RequestParam LocalDateTime thoiGianDuKien,
                               RedirectAttributes ra) {
        DangKyKhachTham khach = khachThamService.timTheoId(id);
        // Chỉ cho phép sửa khi còn ở trạng thái "Chờ duyệt"
        if (khach != null && "Chờ duyệt".equals(khach.getTrangThai())) {
            khach.setTenKhach(tenKhach);
            khach.setCmnd(cmnd);
            khach.setBienSoXe(bienSoXe);
            khach.setThoiGianDuKien(thoiGianDuKien);
            khachThamService.luu(khach); // Lưu lại thông tin mới
            ra.addFlashAttribute("thongBaoThanhCong", "Đã cập nhật thông tin khách thăm thành công.");
        } else {
            ra.addFlashAttribute("thongBaoLoi", "Không thể sửa vì yêu cầu đã được Lễ tân xử lý!");
        }
        return "redirect:/cudan/khach-tham";
    }
}