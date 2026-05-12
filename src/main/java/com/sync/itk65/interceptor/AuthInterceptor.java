package com.sync.itk65.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sync.itk65.entity.NguoiDung;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


// Class này sẽ được đăng ký trong WebConfig để kiểm tra quyền truy cập của người dùng trước khi vào controller

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

        // Nếu cố tình truy cập vào trang admin nhưng không phải là admin hoặc staff (vai trò != 1 && vai trò != 2)
        if (uri.startsWith("/admin")) {
            if (user.getVaiTro() != 1 && user.getVaiTro() != 2) {
                response.sendRedirect("/");
                return false;
            }
            // Nhân viên (vai trò = 2) không được truy cập Quản lý người dùng
            if (uri.startsWith("/admin/nguoi-dung") && user.getVaiTro() == 2) {
                response.sendRedirect("/admin/dashboard");
                return false;
            }
        }
        // Nếu cố tình truy cập vào trang cư dân nhưng là admin/staff
        else if (uri.startsWith("/cudan")) {
            if (user.getVaiTro() == 1 || user.getVaiTro() == 2) {
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
