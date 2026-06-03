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
    public String luuKhachTham(HttpSession session,
                               @RequestParam String tenKhach,
                               @RequestParam String cmnd,
                               @RequestParam(required = false, defaultValue = "") String bienSoXe,
                               @RequestParam LocalDateTime thoiGianDuKien,
                               @RequestParam LocalDateTime ngayDi,
                               RedirectAttributes ra) {

        String thongBaoLoi = kiemTraLoiKhachTham(tenKhach, cmnd, bienSoXe, thoiGianDuKien, ngayDi);
        if (thongBaoLoi != null) {
            ra.addFlashAttribute("thongBaoLoi", thongBaoLoi);
            ra.addFlashAttribute("oldTenKhach", tenKhach);
            ra.addFlashAttribute("oldCmnd", cmnd);
            ra.addFlashAttribute("oldBienSo", bienSoXe);
            ra.addFlashAttribute("oldThoiGian", thoiGianDuKien);
            ra.addFlashAttribute("oldNgayDi", ngayDi);
            return "redirect:/cudan/khach-tham";
        }

        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        DangKyKhachTham khach = new DangKyKhachTham();
        khach.setCuDan(cuDanService.layCuDanTheoId(user.getId()));
        khach.setTenKhach(tenKhach.trim());
        khach.setCmnd(cmnd.trim());
        khach.setBienSoXe(bienSoXe.trim());
        khach.setThoiGianDuKien(thoiGianDuKien);
        khach.setNgayDi(ngayDi);
        khach.setTrangThai("Chờ duyệt");

        khachThamService.luu(khach);
        ra.addFlashAttribute("thongBaoThanhCong", "Đăng ký khách thăm thành công!");
        return "redirect:/cudan/khach-tham";
    }
    // --- HÀM KIỂM TRA LỖI DÙNG CHUNG THÊM VÀO ĐÂY ---
    private String kiemTraLoiKhachTham(String tenKhach, String cmnd, String bienSoXe, LocalDateTime thoiGianDuKien, LocalDateTime ngayDi) {
        // Regex Tên: Chỉ chữ cái và khoảng trắng, 2-50 ký tự
        if (tenKhach == null || !tenKhach.trim().matches("^[a-zA-ZÀ-ỹ][a-zA-ZÀ-ỹ\\s]{1,49}$")) {
            return "Lỗi: Tên khách chỉ được chứa chữ cái, tối thiểu 2 ký tự (Vd: Lê A).";
        }
        // Regex CMND/CCCD: Đúng 9 hoặc 12 số
        if (cmnd == null || !cmnd.trim().matches("^(\\d{9}|\\d{12})$")) {
            return "Lỗi: CMND/CCCD phải bao gồm chính xác 9 hoặc 12 chữ số viết liền.";
        }
        // Thời gian >= hiện tại (Trễ 5 phút)
        if (thoiGianDuKien == null || thoiGianDuKien.isBefore(LocalDateTime.now().minusMinutes(5))) {
            return "Lỗi: Thời gian dự kiến đến không được là thời gian trong quá khứ.";
        }
        // Kiểm tra ngày đi
        if (ngayDi == null) {
            return "Lỗi: Vui lòng chọn ngày dự kiến đi.";
        }
        if (ngayDi.isBefore(thoiGianDuKien)) {
            return "Lỗi: Ngày dự kiến đi phải diễn ra sau thời gian dự kiến đến.";
        }
        // Regex Biển số xe (Nếu có nhập)
        if (bienSoXe != null && !bienSoXe.trim().isEmpty()) {
            String bs = bienSoXe.trim();
            if (!bs.matches("^\\d{2}[A-Za-z]\\s*-\\s*\\d{3}\\.\\d{2}$") &&
                    !bs.matches("^\\d{2}\\s*-\\s*[A-Za-z][A-Za-z0-9]\\s+\\d{3}\\.\\d{2}$")) {
                return "Lỗi: Biển số xe sai định dạng! (Vd Ô tô: 51A-123.45 | Xe máy: 51-AA 123.45)";
            }
        }
        return null;
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
                               @RequestParam LocalDateTime ngayDi,
                               RedirectAttributes ra) {
        DangKyKhachTham khach = khachThamService.timTheoId(id);
        
        String thongBaoLoi = kiemTraLoiKhachTham(tenKhach, cmnd, bienSoXe, thoiGianDuKien, ngayDi);
        if (thongBaoLoi != null) {
            ra.addFlashAttribute("thongBaoLoi", thongBaoLoi);
            return "redirect:/cudan/khach-tham";
        }
        
        // Chỉ cho phép sửa khi còn ở trạng thái "Chờ duyệt"
        if (khach != null && "Chờ duyệt".equals(khach.getTrangThai())) {
            khach.setTenKhach(tenKhach);
            khach.setCmnd(cmnd);
            khach.setBienSoXe(bienSoXe);
            khach.setThoiGianDuKien(thoiGianDuKien);
            khach.setNgayDi(ngayDi);
            khachThamService.luu(khach); // Lưu lại thông tin mới
            ra.addFlashAttribute("thongBaoThanhCong", "Đã cập nhật thông tin khách thăm thành công.");
        } else {
            ra.addFlashAttribute("thongBaoLoi", "Không thể sửa vì yêu cầu đã được Lễ tân xử lý!");
        }
        return "redirect:/cudan/khach-tham";
    }
}