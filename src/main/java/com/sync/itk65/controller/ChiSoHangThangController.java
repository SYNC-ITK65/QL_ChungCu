package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.service.ChiSoHangThangService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/chi-so")
public class ChiSoHangThangController {

    @Autowired
    private ChiSoHangThangService chiSoHangThangService;

    @Autowired
    private CanHoRepository canHoRepository;

    // Hiển thị danh sách chỉ số (có lọc, sắp xếp ngày mới nhất)
    @GetMapping
    public String hienThiDanhSach(Model model,
                                  @RequestParam(required = false) String maCanHo,
                                  @RequestParam(required = false) Long canHoId,
                                  @RequestParam(required = false) Integer thang,
                                  @RequestParam(required = false) Integer nam,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Page<ChiSoHangThang> trangDuLieuChiSo;

        String maCanHoFilter = (maCanHo != null && !maCanHo.trim().isEmpty()) ? maCanHo.trim() : null;
        boolean coBoLoc = maCanHoFilter != null || canHoId != null || thang != null || nam != null;

        if (coBoLoc) {
            trangDuLieuChiSo = chiSoHangThangService.timKiemChiSo(maCanHoFilter, canHoId, thang, nam, page, size);
        } else {
            trangDuLieuChiSo = chiSoHangThangService.layTatCaChiSo(page, size);
        }

        model.addAttribute("danhSachChiSo", trangDuLieuChiSo.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieuChiSo.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("maCanHo", maCanHo != null ? maCanHo : "");
        model.addAttribute("canHoId", canHoId);
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());
        return "admin/chi_so_list";
    }

    // Hiển thị form thêm mới chỉ số
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        model.addAttribute("chiSo", new ChiSoHangThang());
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());
        return "admin/chi_so_form";
    }

    // Xử lý lưu thông tin chỉ số
    @PostMapping("/luu")
    public String luuChiSoHangThang(@ModelAttribute("chiSo") ChiSoHangThang chiSoHangThang,
                                    @RequestParam("canHoId") Long canHoId) {
        if (chiSoHangThang.getNgayGhiNhan() == null) {
            chiSoHangThang.setNgayGhiNhan(java.time.LocalDate.now());
        }

        CanHo canHo = new CanHo();
        canHo.setId(canHoId);
        chiSoHangThang.setCanHo(canHo);

        chiSoHangThangService.luuChiSo(chiSoHangThang);
        return "redirect:/admin/chi-so";
    }

    // Xóa chỉ số
    @GetMapping("/xoa/{id}")
    public String xoaChiSo(@PathVariable("id") Long id) {
        chiSoHangThangService.xoaChiSo(id);
        return "redirect:/admin/chi-so";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel() {
        byte[] bytes = chiSoHangThangService.xuatExcelDanhSachChiSo();
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_chi_so_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }

    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("thongBaoLoi", "Vui lòng chọn file Excel để import!");
            return "redirect:/admin/chi-so";
        }
        try {
            String ketQua = chiSoHangThangService.importExcelChiSo(file);
            ra.addFlashAttribute("thongBaoThanhCong", ketQua);
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/admin/chi-so";
    }
}