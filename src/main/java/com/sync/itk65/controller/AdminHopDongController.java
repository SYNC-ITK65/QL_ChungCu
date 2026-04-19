package com.sync.itk65.controller;

import com.sync.itk65.entity.HopDong;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.HopDongService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/hop-dong")
public class AdminHopDongController {

    @Autowired
    private HopDongService hopDongService;

    @Autowired
    private CanHoService canHoService;

    @Autowired
    private CuDanService cuDanService;

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
        model.addAttribute("hopDong", new HopDong());
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        model.addAttribute("danhSachCuDan", cuDanService.layTatCaCuDan());
        return "admin/hop_dong_form";
    }

    @PostMapping("/luu")
    public String luuHopDong(@ModelAttribute("hopDong") HopDong hopDong,
                             @RequestParam("canHoId") Long canHoId,
                             @RequestParam("cuDanId") Long cuDanId,
                             RedirectAttributes ra) {
        try {
            hopDongService.taoHopDong(hopDong, canHoId, cuDanId);
            ra.addFlashAttribute("thongBaoThanhCong", "Tạo hợp đồng thành công.");
            return "redirect:/admin/hop-dong";
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
            return "redirect:/admin/hop-dong/tao-moi";
        }
    }

    @GetMapping("/cap-nhat-trang-thai/{id}")
    public String capNhatTrangThai(@PathVariable("id") Long id,
                                   @RequestParam("trangThai") String trangThai,
                                   RedirectAttributes ra) {
        try {
            hopDongService.capNhatTrangThai(id, trangThai);
            ra.addFlashAttribute("thongBaoThanhCong", "Cập nhật trạng thái hợp đồng thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
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
}
