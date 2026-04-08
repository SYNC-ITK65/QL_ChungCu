package com.sync.itk65.controller;
import com.sync.itk65.entity.*;
import com.sync.itk65.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/duyet-yeu-cau")
public class Admin_LeTanController {
    @Autowired private DangKyKhachThamService khachThamService;
    @Autowired private DatDichVuService datDichVuService;

    @GetMapping
    public String hienThiTrangDuyet(Model model) {
        List<DangKyKhachTham> allKhach = khachThamService.layTatCa();
        List<DatDichVu> allDichVu = datDichVuService.layTatCaDonDatDichVu();

        model.addAttribute("listKhachCho", allKhach.stream().filter(k -> "Chờ duyệt".equals(k.getTrangThai())).collect(Collectors.toList()));
        model.addAttribute("listDichVuCho", allDichVu.stream().filter(d -> "Chờ duyệt".equals(d.getTrangThai())).collect(Collectors.toList()));
        model.addAttribute("listKhachLS", allKhach.stream().filter(k -> !"Chờ duyệt".equals(k.getTrangThai())).collect(Collectors.toList()));
        model.addAttribute("listDichVuLS", allDichVu.stream().filter(d -> !"Chờ duyệt".equals(d.getTrangThai())).collect(Collectors.toList()));
        return "admin/duyet_yeu_cau";
    }

    @GetMapping("/khach-tham/{id}/{trangThai}")
    public String duyetKhachTham(@PathVariable Long id, @PathVariable String trangThai) {
        DangKyKhachTham khach = khachThamService.timTheoId(id);
        if (khach != null) {
            khach.setTrangThai(trangThai.equals("duyet") ? "Đã duyệt" : "Không duyệt");
            khach.setThoiGianDuyet(LocalDateTime.now());
            khachThamService.luu(khach);
        }
        return "redirect:/admin/duyet-yeu-cau";
    }

    @GetMapping("/dich-vu/{id}/{trangThai}")
    public String duyetDichVu(@PathVariable Long id, @PathVariable String trangThai) {
        DatDichVu dv = datDichVuService.findById(id);
        if (dv != null) {
            dv.setTrangThai(trangThai.equals("duyet") ? "Đã duyệt" : "Không duyệt");
            dv.setThoiGianDuyet(LocalDateTime.now());
            datDichVuService.luu(dv);
        }
        return "redirect:/admin/duyet-yeu-cau";
    }
}