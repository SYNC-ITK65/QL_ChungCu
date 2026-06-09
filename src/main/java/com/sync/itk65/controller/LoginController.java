package com.sync.itk65.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.NguoiDungService;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
public class LoginController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private MessageSource messageSource;

    // Hiển thị trang chủ landing page
    @GetMapping("/")
    public String hienThiTrangChu() {
        return "index";
    }

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String hienThiTrangDangNhap() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String xuLyDangNhap(@RequestParam("tenDangNhap") String tenDangNhap,
            @RequestParam("matKhau") String matKhau,
            HttpSession session, Model model) {

        NguoiDung user = nguoiDungService.timTheoTenDangNhap(tenDangNhap);

        if (user != null && user.getMatKhauMaHoa().equals(matKhau)) {
            // Lưu thông tin vào session
            session.setAttribute("nguoiDungDangNhap", user);
            session.setAttribute("vaiTro", user.getVaiTro());

            // Phân luồng: Admin/Staff/Lễ tân/Bảo vệ vào trang quản lý, Cư dân vào trang cá nhân
            if (user.getVaiTro() == 3) {
                return "redirect:/cudan/dashboard"; // Cư dân (Chủ hộ hoặc Người thuê)
            } else {
                return "redirect:/admin/dashboard"; // Admin (1), Quản lý (2), Lễ tân (4), Bảo vệ (5)
            }
        } else {
            Locale locale = LocaleContextHolder.getLocale();
            model.addAttribute("error", messageSource.getMessage("login.error.wrongCredentials", null, "Sai tên đăng nhập hoặc mật khẩu!", locale));
            return "login";
        }
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String dangXuat(HttpSession session) {
        session.invalidate(); // Xóa session
        return "redirect:/login";
    }
}