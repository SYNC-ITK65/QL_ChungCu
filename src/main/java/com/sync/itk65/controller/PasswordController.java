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

@Controller
public class PasswordController {

    @Autowired
    private NguoiDungService nguoiDungService;

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
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác!");
            return "redirect:/user/doi-mat-khau";
        }

        // 2. Kiểm tra mật khẩu mới và xác nhận
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận không khớp!");
            return "redirect:/user/doi-mat-khau";
        }

        // 3. Cập nhật mật khẩu
        nguoiDungService.doiMatKhau(user.getId(), newPassword);
        
        // 4. Cập nhật lại user trong session
        user.setMatKhauMaHoa(newPassword);
        session.setAttribute("nguoiDungDangNhap", user);

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/user/doi-mat-khau";
    }
}
