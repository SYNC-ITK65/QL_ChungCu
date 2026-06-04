package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.PhanAnhService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/cudan/phan-anh")
public class CuDanPhanAnhController {

    @Autowired
    private PhanAnhService phanAnhService;

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listPhanAnh(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) return "redirect:/login";

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        if (cuDan != null && cuDan.getCanHo() != null) {
            List<PhanAnh> phanAnhs = phanAnhService.findByCanHoId(cuDan.getCanHo().getId());
            model.addAttribute("phanAnhs", phanAnhs);
        }

        return "cudan/phan_anh_list";
    }

    @GetMapping("/gui-moi")
    public String showAddForm(HttpSession session, Model model, RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) return "redirect:/login";

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        // Chặn không cho vào form nếu chưa có căn hộ
        if (cuDan == null || cuDan.getCanHo() == null) {
            ra.addFlashAttribute("error", "Lỗi: Bạn phải được BQL gán vào Căn hộ trước khi gửi phản ánh!");
            return "redirect:/cudan/phan-anh";
        }

        model.addAttribute("phanAnh", new PhanAnh());
        return "cudan/phan_anh_form";
    }

    // FIX: Xử lý GET /luu khi người dùng đổi ngôn ngữ trên trang hiển thị lỗi validation
    @GetMapping("/luu")
    public String handleGetLuu() {
        return "redirect:/cudan/phan-anh/gui-moi";
    }

    @PostMapping("/luu")
    public String savePhanAnh(@ModelAttribute("phanAnh") PhanAnh phanAnh,
                              BindingResult result,
                              HttpSession session,
                              RedirectAttributes ra) {

        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) return "redirect:/login";

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null || cuDan.getCanHo() == null) {
            ra.addFlashAttribute("error", "Lỗi hệ thống: Không xác định được Căn hộ của bạn.");
            return "redirect:/cudan/phan-anh";
        }

        // 1. Chống Spam 30s
        Long lastTime = (Long) session.getAttribute("lastPhanAnhTime");
        long currentTime = System.currentTimeMillis();
        if (lastTime != null && (currentTime - lastTime) < 30000) {
            ra.addFlashAttribute("error", "Bạn đang thao tác quá nhanh, vui lòng đợi 30 giây!");
            return "redirect:/cudan/phan-anh/gui-moi";
        }

        // 2. Lọc HTML chống XSS
        if (phanAnh.getTieuDe() != null) phanAnh.setTieuDe(phanAnh.getTieuDe().replaceAll("<", "&lt;").replaceAll(">", "&gt;").trim());
        if (phanAnh.getNoiDung() != null) phanAnh.setNoiDung(phanAnh.getNoiDung().replaceAll("<", "&lt;").replaceAll(">", "&gt;").trim());

        // 3. Gọi hàm Check lỗi
        validatePhanAnh(phanAnh, result);
        if (result.hasErrors()) return "cudan/phan_anh_form";

        // 4. Lưu
        phanAnh.setCanHo(cuDan.getCanHo());
        phanAnh.setNgayGui(LocalDateTime.now());
        phanAnh.setTrangThai("Chờ xử lý");
        phanAnhService.save(phanAnh);

        session.setAttribute("lastPhanAnhTime", currentTime);
        ra.addFlashAttribute("success", "🚀 Đã gửi phản ánh thành công! BQL sẽ sớm tiếp nhận.");
        return "redirect:/cudan/phan-anh";
    }
    // Helper method to resolve i18n messages
    private String msg(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, code, locale);
    }

    // HÀM CHECK LỖI
    private void validatePhanAnh(PhanAnh pa, BindingResult result) {
        String t = pa.getTieuDe() != null ? pa.getTieuDe().trim() : "";

        // 1. KIỂM TRA TIÊU ĐỀ
        if (t.isBlank()) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.blank", msg("error.pa.tieuDe.blank"));
        }
        else if (t.length() < 5 || t.length() > 100) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.length", msg("error.pa.tieuDe.length"));
        }
        // Bẫy 1: Không có bất kỳ chữ cái nào (Toàn số, dấu chấm, icon: "123 456 !!!")
        else if (!t.matches(".*\\p{L}.*")) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.noLetter", msg("error.pa.tieuDe.noLetter"));
        }
        // Bẫy 2: Lặp 3 chữ cái y hệt nhau liên tiếp (Vd: aaaa, bbb, eee) - Tiếng Việt/Anh không có từ nào như vậy
        else if (t.matches(".*([a-zA-Z])\\1{2,}.*")) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.repeatChar", msg("error.pa.tieuDe.repeatChar"));
        }
        // Bẫy 3: Có 5 phụ âm đứng liền nhau (Chắc chắn là vuốt bừa bàn phím: zxcvb, lkjhg, dfghj)
        else if (t.toLowerCase().matches(".*[bcdfghjklmnpqrstvwxz]{5,}.*")) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.consonants", msg("error.pa.tieuDe.consonants"));
        }
        // Bẫy 4: Gõ không dấu (ASCII) nhưng lại KHÔNG có nguyên âm (a,e,i,o,u,y) -> (Vd: qwrrt, hklm)
        else if (t.matches("^[\\x00-\\x7F]+$") && !t.toLowerCase().matches(".*[aeiouy].*")) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.noVowel", msg("error.pa.tieuDe.noVowel"));
        }
        // Bẫy 5: Chặn từ khóa rác kinh điển
        else if (t.toLowerCase().matches(".*(test|asdf|qwer|zxcv|1234|abcd).*")) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.spam", msg("error.pa.tieuDe.spam"));
        }
        // Bẫy 6: Dài hơn 15 ký tự mà không thèm gõ dấu cách
        else if (!t.contains(" ") && t.length() >= 15) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.noSpace", msg("error.pa.tieuDe.noSpace"));
        }
        else if (t.equals(t.toUpperCase()) && t.matches(".*[a-zA-Z].*")) {
            result.rejectValue("tieuDe", "error.pa.tieuDe.allCaps", msg("error.pa.tieuDe.allCaps"));
        }


        // 2. KIỂM TRA NỘI DUNG (Áp dụng tương tự Tiêu đề nhưng nới lỏng hơn xíu)
        String n = pa.getNoiDung() != null ? pa.getNoiDung().trim() : "";

        if (n.isBlank()) {
            result.rejectValue("noiDung", "error.pa.noiDung.blank", msg("error.pa.noiDung.blank"));
        }
        else if (n.length() < 20 || n.length() > 5000) {
            result.rejectValue("noiDung", "error.pa.noiDung.length", msg("error.pa.noiDung.length"));
        }
        else if (!n.matches(".*\\p{L}.*")) {
            result.rejectValue("noiDung", "error.pa.noiDung.noLetter", msg("error.pa.noiDung.noLetter"));
        }
        // Cấm lặp 4 chữ cái liên tiếp
        else if (n.matches(".*([a-zA-Z])\\1{3,}.*")) {
            result.rejectValue("noiDung", "error.pa.noiDung.repeatChar", msg("error.pa.noiDung.repeatChar"));
        }
        // Cấm 6 phụ âm liên tiếp
        else if (n.toLowerCase().matches(".*[bcdfghjklmnpqrstvwxz]{6,}.*")) {
            result.rejectValue("noiDung", "error.pa.noiDung.consonants", msg("error.pa.noiDung.consonants"));
        }
        // Cấm gõ không dấu + không nguyên âm
        else if (n.matches("^[\\x00-\\x7F]+$") && !n.toLowerCase().matches(".*[aeiouy].*")) {
            result.rejectValue("noiDung", "error.pa.noiDung.noVowel", msg("error.pa.noiDung.noVowel"));
        }
        else if (n.toLowerCase().matches(".*(test|asdf|qwer|zxcv|1234|abcd).*")) {
            result.rejectValue("noiDung", "error.pa.noiDung.spam", msg("error.pa.noiDung.spam"));
        }
        // Cấm dán một cục dính liền
        else if (!n.contains(" ") && n.length() >= 20) {
            result.rejectValue("noiDung", "error.pa.noiDung.noSpace", msg("error.pa.noiDung.noSpace"));
        }
        else if (n.equalsIgnoreCase(t)) {
            result.rejectValue("noiDung", "error.pa.noiDung.copyTitle", msg("error.pa.noiDung.copyTitle"));
        }
        else if (n.toLowerCase().contains("xem file đính kèm") && n.length() < 50) {
            result.rejectValue("noiDung", "error.pa.noiDung.tooShort", msg("error.pa.noiDung.tooShort"));
        }
    }
}