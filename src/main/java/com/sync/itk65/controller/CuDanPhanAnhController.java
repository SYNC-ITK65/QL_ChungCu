package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.PhanAnhService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/cudan/phan-anh")
public class CuDanPhanAnhController {

    @Autowired
    private PhanAnhService phanAnhService;

    @Autowired
    private CuDanService cuDanService;

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
    // HÀM CHECK LỖI
    private void validatePhanAnh(PhanAnh pa, BindingResult result) {
        String t = pa.getTieuDe() != null ? pa.getTieuDe().trim() : "";

        // 1. KIỂM TRA TIÊU ĐỀ
        if (t.isBlank()) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề không được để trống");
        }
        else if (t.length() < 5 || t.length() > 100) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề từ 5-100 ký tự");
        }
        // Bẫy 1: Không có bất kỳ chữ cái nào (Toàn số, dấu chấm, icon: "123 456 !!!")
        else if (!t.matches(".*\\p{L}.*")) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề phải chứa ít nhất một chữ cái");
        }
        // Bẫy 2: Lặp 3 chữ cái y hệt nhau liên tiếp (Vd: aaaa, bbb, eee) - Tiếng Việt/Anh không có từ nào như vậy
        else if (t.matches(".*([a-zA-Z])\\1{2,}.*")) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề chứa chữ cái lặp lại vô nghĩa");
        }
        // Bẫy 3: Có 5 phụ âm đứng liền nhau (Chắc chắn là vuốt bừa bàn phím: zxcvb, lkjhg, dfghj)
        else if (t.toLowerCase().matches(".*[bcdfghjklmnpqrstvwxz]{5,}.*")) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề chứa chuỗi phụ âm vô nghĩa");
        }
        // Bẫy 4: Gõ không dấu (ASCII) nhưng lại KHÔNG có nguyên âm (a,e,i,o,u,y) -> (Vd: qwrrt, hklm)
        else if (t.matches("^[\\x00-\\x7F]+$") && !t.toLowerCase().matches(".*[aeiouy].*")) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề gõ bừa, không chứa nguyên âm nào");
        }
        // Bẫy 5: Chặn từ khóa rác kinh điển
        else if (t.toLowerCase().matches(".*(test|asdf|qwer|zxcv|1234|abcd).*")) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề chứa chuỗi ký tự thử nghiệm/vô nghĩa");
        }
        // Bẫy 6: Dài hơn 15 ký tự mà không thèm gõ dấu cách
        else if (!t.contains(" ") && t.length() >= 15) {
            result.rejectValue("tieuDe", "error.pa", "Tiêu đề phải là câu có ý nghĩa (cần có khoảng trắng)");
        }
        else if (t.equals(t.toUpperCase()) && t.matches(".*[a-zA-Z].*")) {
            result.rejectValue("tieuDe", "error.pa", "Không được viết hoa toàn bộ tiêu đề");
        }


        // 2. KIỂM TRA NỘI DUNG (Áp dụng tương tự Tiêu đề nhưng nới lỏng hơn xíu)
        String n = pa.getNoiDung() != null ? pa.getNoiDung().trim() : "";

        if (n.isBlank()) {
            result.rejectValue("noiDung", "error.pa", "Nội dung không được để trống");
        }
        else if (n.length() < 20 || n.length() > 5000) {
            result.rejectValue("noiDung", "error.pa", "Nội dung từ 20-5000 ký tự");
        }
        else if (!n.matches(".*\\p{L}.*")) {
            result.rejectValue("noiDung", "error.pa", "Nội dung phải chứa chữ cái, không được chỉ nhập số/ký tự đặc biệt");
        }
        // Cấm lặp 4 chữ cái liên tiếp
        else if (n.matches(".*([a-zA-Z])\\1{3,}.*")) {
            result.rejectValue("noiDung", "error.pa", "Nội dung chứa chữ cái lặp lại vô nghĩa");
        }
        // Cấm 6 phụ âm liên tiếp
        else if (n.toLowerCase().matches(".*[bcdfghjklmnpqrstvwxz]{6,}.*")) {
            result.rejectValue("noiDung", "error.pa", "Nội dung chứa chuỗi phụ âm vô nghĩa");
        }
        // Cấm gõ không dấu + không nguyên âm
        else if (n.matches("^[\\x00-\\x7F]+$") && !n.toLowerCase().matches(".*[aeiouy].*")) {
            result.rejectValue("noiDung", "error.pa", "Nội dung vô nghĩa (không chứa nguyên âm nào)");
        }
        else if (n.toLowerCase().matches(".*(test|asdf|qwer|zxcv|1234|abcd).*")) {
            result.rejectValue("noiDung", "error.pa", "Nội dung chứa cụm từ thử nghiệm/vô nghĩa");
        }
        // Cấm dán một cục dính liền
        else if (!n.contains(" ") && n.length() >= 20) {
            result.rejectValue("noiDung", "error.pa", "Nội dung phải có khoảng trắng (chuỗi dính liền không hợp lệ)");
        }
        else if (n.equalsIgnoreCase(t)) {
            result.rejectValue("noiDung", "error.pa", "Nội dung không được copy y chang tiêu đề");
        }
        else if (n.toLowerCase().contains("xem file đính kèm") && n.length() < 50) {
            result.rejectValue("noiDung", "error.pa", "Vui lòng mô tả chi tiết hơn nội dung");
        }
    }
}