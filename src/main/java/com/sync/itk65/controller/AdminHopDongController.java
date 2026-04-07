package com.sync.itk65.controller;

import com.sync.itk65.entity.HopDong;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.HopDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String hienThiDanhSach(Model model) {
        model.addAttribute("danhSachHopDong", hopDongService.layTatCaHopDong());
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
}
