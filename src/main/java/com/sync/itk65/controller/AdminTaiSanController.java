package com.sync.itk65.controller;

import com.sync.itk65.entity.LichSuBaoTri;
import com.sync.itk65.entity.TaiSan;
import com.sync.itk65.service.LichSuBaoTriService;
import com.sync.itk65.service.TaiSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/tai-san")
public class AdminTaiSanController {

    @Autowired
    private TaiSanService taiSanService;

    @Autowired
    private LichSuBaoTriService lichSuBaoTriService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("listTaiSan", taiSanService.getAllTaiSan());
        return "admin/tai_san_list";
    }

    @GetMapping("/canh-bao")
    public String alerts(Model model) {
        model.addAttribute("listTaiSan", taiSanService.getAlertAssets());
        model.addAttribute("isAlert", true);
        return "admin/tai_san_list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("taiSan", new TaiSan());
        return "admin/tai_san_form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        taiSanService.getTaiSanById(id).ifPresent(t -> model.addAttribute("taiSan", t));
        return "admin/tai_san_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute TaiSan taiSan) {
        taiSanService.saveTaiSan(taiSan);
        return "redirect:/admin/tai-san";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taiSanService.deleteTaiSan(id);
        return "redirect:/admin/tai-san";
    }

    @GetMapping("/{id}/bao-tri")
    public String history(@PathVariable Long id, Model model) {
        taiSanService.getTaiSanById(id).ifPresent(t -> {
            model.addAttribute("taiSan", t);
            model.addAttribute("listBaoTri", lichSuBaoTriService.getHistoryByTaiSanId(id));
        });
        return "admin/bao_tri_list";
    }

    @GetMapping("/{id}/bao-tri/add")
    public String showHistoryForm(@PathVariable Long id, Model model) {
        taiSanService.getTaiSanById(id).ifPresent(t -> {
            LichSuBaoTri history = new LichSuBaoTri();
            history.setTaiSan(t);
            model.addAttribute("history", history);
            model.addAttribute("taiSan", t);
        });
        return "admin/bao_tri_form";
    }

    @PostMapping("/{id}/bao-tri/save")
    public String saveHistory(@PathVariable Long id, @ModelAttribute LichSuBaoTri history) {
        taiSanService.getTaiSanById(id).ifPresent(t -> {
            history.setTaiSan(t);
            lichSuBaoTriService.saveHistory(history);
        });
        return "redirect:/admin/tai-san/" + id + "/bao-tri";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel(@RequestParam(required = false, defaultValue = "false") boolean canhBao) {
        byte[] bytes = taiSanService.xuatExcelDanhSachTaiSan(canhBao);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = canhBao ? ("tai_san_canh_bao_" + ts + ".xlsx") : ("danh_sach_tai_san_" + ts + ".xlsx");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}
