package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.entity.TamTruTamVang;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.TamTruTamVangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cudan/tamtru-tamvang")
public class CuDanTamTruTamVangController {
    @Autowired private TamTruTamVangService service;
    @Autowired private CuDanService cuDanService;

    private CuDan getCuDan(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        return user != null ? cuDanService.layCuDanTheoId(user.getId()) : null;
    }
    @GetMapping
    public String list(Model model, HttpSession session) {
        CuDan cuDan = getCuDan(session);
        if (cuDan == null) return "redirect:/login";
        model.addAttribute("listTamTruTamVang", service.getByCuDan(cuDan));
        return "cudan/tamtru_tamvang_list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute TamTruTamVang don, HttpSession session, RedirectAttributes ra) {
        try {
            don.setCuDan(getCuDan(session));
            if (don.getId() == null) service.create(don);
            else service.update(don.getId(), don, getCuDan(session));
            ra.addFlashAttribute("successMessage", "Lưu thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cudan/tamtru-tamvang";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        try { service.delete(id, getCuDan(session)); ra.addFlashAttribute("successMessage", "Đã hủy đơn!"); }
        catch (Exception e) { ra.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/cudan/tamtru-tamvang";
    }

    @PostMapping("/ket-thuc/{id}")
    public String ketThucSom(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        try { service.ketThucSom(id, getCuDan(session)); ra.addFlashAttribute("successMessage", "Đã cập nhật hoàn thành!"); }
        catch (Exception e) { ra.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/cudan/tamtru-tamvang";
    }
    // Hiển thị trang Form để đăng ký mới
    @GetMapping("/form")
    public String showAddForm(Model model) {
        model.addAttribute("don", new TamTruTamVang());
        model.addAttribute("title", "Đăng ký Tạm trú / Tạm vắng");
        return "cudan/tamtru_tamvang_form";
    }

    // Hiển thị trang Form để sửa đơn (Chỉ khi đang Chờ duyệt)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes ra) {
        try {
            TamTruTamVang don = service.getById(id);
            if (!don.getTrangThaiDuyet().equals("Chờ duyệt")) {
                ra.addFlashAttribute("errorMessage", "Chỉ có thể sửa đơn ở trạng thái Chờ duyệt.");
                return "redirect:/cudan/tamtru-tamvang";
            }
            model.addAttribute("don", don);
            model.addAttribute("title", "Cập nhật thông tin đăng ký");
            return "cudan/tamtru_tamvang_form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy thông tin đơn.");
            return "redirect:/cudan/tamtru-tamvang";
        }
    }
}