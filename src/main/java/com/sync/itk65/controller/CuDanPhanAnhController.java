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
    public String savePhanAnh(@ModelAttribute("phanAnh") PhanAnh phanAnh, HttpSession session, RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) return "redirect:/login";

        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        // Bắt lỗi khi lưu nếu không có căn hộ
        if (cuDan == null || cuDan.getCanHo() == null) {
            ra.addFlashAttribute("error", "Lỗi hệ thống: Không xác định được Căn hộ của bạn.");
            return "redirect:/cudan/phan-anh";
        }

        // Thực hiện lưu
        phanAnh.setCanHo(cuDan.getCanHo());
        phanAnh.setNgayGui(LocalDateTime.now());
        phanAnh.setTrangThai("Chờ xử lý");
        phanAnhService.save(phanAnh);

        // Gửi thông báo thành công
        ra.addFlashAttribute("success", "🚀 Đã gửi phản ánh thành công! BQL sẽ sớm tiếp nhận.");
        return "redirect:/cudan/phan-anh";
    }
}