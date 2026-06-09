package com.sync.itk65.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sync.itk65.entity.HopDong;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.HopDongService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
@RequestMapping("/admin/hop-dong")
public class AdminHopDongController {

    @Autowired
    private HopDongService hopDongService;

    @Autowired
    private CanHoService canHoService;

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String hienThiDanhSach(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String maCanHo,
                                  @RequestParam(required = false) String loaiHopDong,
                                  @RequestParam(required = false) String trangThai) {
        Page<HopDong> trangDuLieu = hopDongService.timKiemHopDong(maCanHo, loaiHopDong, trangThai, page, size);
        model.addAttribute("danhSachHopDong", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("maCanHo", maCanHo);
        model.addAttribute("loaiHopDong", loaiHopDong);
        model.addAttribute("trangThai", trangThai);
        return "admin/hop_dong_list";
    }

    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        HopDong hopDong = new HopDong();
        hopDong.setBenChoThue("Công ty SYNC");
        model.addAttribute("hopDong", hopDong);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        model.addAttribute("danhSachCuDan", cuDanService.layTatCaCuDan());
        return "admin/hop_dong_form";
    }

    @PostMapping("/luu")
    public String luuHopDong(@ModelAttribute("hopDong") HopDong hopDong,
                             @RequestParam("canHoId") Long canHoId,
                             @RequestParam("cuDanId") Long cuDanId,
                             @RequestParam(value = "thoiHan", defaultValue = "12") int thoiHan,
                             RedirectAttributes ra) {
        try {
            hopDongService.taoHopDong(hopDong, canHoId, cuDanId, thoiHan);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("hd.success.create", null, "Tạo hợp đồng thành công.", locale));
            return "redirect:/admin/hop-dong";
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("hd.error.createEx", new Object[]{e.getMessage()}, e.getMessage(), locale));
            return "redirect:/admin/hop-dong/tao-moi";
        }
    }

    @GetMapping("/cap-nhat-trang-thai/{id}")
    public String capNhatTrangThai(@PathVariable("id") Long id,
                                   @RequestParam("trangThai") String trangThai,
                                   RedirectAttributes ra) {
        try {
            hopDongService.capNhatTrangThai(id, trangThai);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("hd.success.updateStatus", null, "Cập nhật trạng thái hợp đồng thành công.", locale));
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("hd.error.updateStatusEx", new Object[]{e.getMessage()}, e.getMessage(), locale));
        }
        return "redirect:/admin/hop-dong";
    }

    @GetMapping("/xuat-pdf/{id}")
    public ResponseEntity<byte[]> xuatPdfHopDong(@PathVariable("id") Long id) {
        byte[] pdfData = hopDongService.xuatHopDongPdf(id);
        String fileName = "hop-dong-" + id + ".pdf";

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }

    @GetMapping("/chi-tiet/{id}")
    public String xemChiTietHopDong(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        HopDong hopDong = hopDongService.layHopDongTheoId(id);
        if (hopDong == null) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("hd.error.notFound", null, "Không tìm thấy hợp đồng.", locale));
            return "redirect:/admin/hop-dong";
        }
        model.addAttribute("hopDong", hopDong);
        return "admin/hop_dong_detail";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSuaHopDong(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        HopDong hopDong = hopDongService.layHopDongTheoId(id);
        if (hopDong == null) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("hd.error.notFound", null, "Không tìm thấy hợp đồng.", locale));
            return "redirect:/admin/hop-dong";
        }
        model.addAttribute("hopDong", hopDong);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        model.addAttribute("danhSachCuDan", cuDanService.layTatCaCuDan());
        model.addAttribute("isEdit", true);
        return "admin/hop_dong_form";
    }

    @PostMapping("/cap-nhat/{id}")
    public String capNhatHopDong(@PathVariable("id") Long id,
                                  @ModelAttribute("hopDong") HopDong hopDong,
                                  @RequestParam("canHoId") Long canHoId,
                                  @RequestParam("cuDanId") Long cuDanId,
                                  @RequestParam(value = "thoiHan", defaultValue = "12") int thoiHan,
                                  RedirectAttributes ra) {
        try {
            hopDongService.capNhatHopDong(id, hopDong, canHoId, cuDanId, thoiHan);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("hd.success.update", null, "Cập nhật hợp đồng thành công.", locale));
            return "redirect:/admin/hop-dong";
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("hd.error.updateEx", new Object[]{e.getMessage()}, e.getMessage(), locale));
            return "redirect:/admin/hop-dong/sua/" + id;
        }
    }

    @GetMapping("/xoa/{id}")
    public String xoaHopDong(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            hopDongService.xoaHopDong(id);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("hd.success.delete", null, "Xóa hợp đồng thành công.", locale));
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("hd.error.deleteEx", new Object[]{e.getMessage()}, "Không thể xóa hợp đồng: " + e.getMessage(), locale));
        }
        return "redirect:/admin/hop-dong";
    }
}
