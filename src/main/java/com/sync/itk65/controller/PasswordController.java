package com.sync.itk65.controller;

import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.NguoiDungService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.MessageSource;
import java.util.Locale;

@Controller
public class PasswordController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/user/doi-mat-khau")
    public String hienThiTrangDoiMatKhau(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "doi-mat-khau";
    }

    @PostMapping("/user/doi-mat-khau")
    public String xuLyDoiMatKhau(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }

        // 1. Kiểm tra mật khẩu cũ (So sánh trực tiếp theo logic hiện tại)
        if (!user.getMatKhauMaHoa().equals(oldPassword)) {
            Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale();
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("password.error.oldMismatch", null, "Mật khẩu cũ không chính xác!", locale));
            return "redirect:/user/doi-mat-khau";
        }

        // 2. Kiểm tra mật khẩu mới và xác nhận
        if (!newPassword.equals(confirmPassword)) {
            Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale();
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("password.error.newMismatch", null, "Mật khẩu mới và xác nhận không khớp!", locale));
            return "redirect:/user/doi-mat-khau";
        }

        // 3. Cập nhật mật khẩu
        nguoiDungService.doiMatKhau(user.getId(), newPassword);
        
        // 4. Cập nhật lại user trong session
        user.setMatKhauMaHoa(newPassword);
        session.setAttribute("nguoiDungDangNhap", user);

        Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale();
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("password.success.changed", null, "Đổi mật khẩu thành công!", locale));
        return "redirect:/user/doi-mat-khau";
    }
}
