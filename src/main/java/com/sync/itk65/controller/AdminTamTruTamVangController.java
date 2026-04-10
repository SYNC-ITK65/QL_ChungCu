package com.sync.itk65.controller;

import com.sync.itk65.service.TamTruTamVangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tamtru-tamvang")
public class AdminTamTruTamVangController {
    @Autowired private TamTruTamVangService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", service.getAll());
        return "admin/tamtru_tamvang_list";
    }

    @PostMapping("/process/{id}")
    public String process(@PathVariable Long id, @RequestParam String action,
                          @RequestParam(required = false) String lyDoTuChoi, RedirectAttributes ra) {
        try { service.processByAdmin(id, action, lyDoTuChoi); ra.addFlashAttribute("success", "Xử lý thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/admin/tamtru-tamvang";
    }

    @PostMapping("/undo/{id}")
    public String undo(@PathVariable Long id, RedirectAttributes ra) {
        try { service.undoByAdmin(id); ra.addFlashAttribute("success", "Đã hoàn tác!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/admin/tamtru-tamvang";
    }
}