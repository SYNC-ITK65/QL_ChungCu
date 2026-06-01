package com.sync.itk65.interceptor;

import com.sync.itk65.entity.NguoiDung;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");

        // Nếu chưa đăng nhập
        if (user == null) {
            response.sendRedirect("/");
            return false;
        }

        // Nếu cố tình truy cập vào trang admin nhưng không phải là admin, quản lý, lễ tân, bảo vệ
        if (uri.startsWith("/admin")) {
            int role = user.getVaiTro();
            if (role != 1 && role != 2 && role != 4 && role != 5) {
                response.sendRedirect("/");
                return false;
            }
            // Quản lý (2), Lễ tân (4), Bảo vệ (5) không được truy cập Quản lý người dùng
            if (uri.startsWith("/admin/nguoi-dung") && (role == 2 || role == 4 || role == 5)) {
                response.sendRedirect("/admin/dashboard");
                return false;
            }
        }
        // Nếu cố tình truy cập vào trang cư dân nhưng là admin, quản lý, lễ tân, bảo vệ
        else if (uri.startsWith("/cudan")) {
            int role = user.getVaiTro();
            if (role == 1 || role == 2 || role == 4 || role == 5) {
                response.sendRedirect("/");
                return false;
            }
        }

        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return true;
    }
}
