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

        // Nếu cố tình truy cập vào trang admin nhưng không phải là admin (vai trò != 1)
        if (uri.startsWith("/admin")) {
            if (user.getVaiTro() != 1) {
                response.sendRedirect("/");
                return false;
            }
        }
        // Nếu cố tình truy cập vào trang cư dân nhưng là admin
        else if (uri.startsWith("/cudan")) {
            if (user.getVaiTro() == 1) {
                response.sendRedirect("/");
                return false;
            }
        }

        return true;
    }
}
