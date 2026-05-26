package com.sync.itk65.controller;

import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.service.PhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/phuong-tien") // Dùng chung 1 đường dẫn như CanHoController
public class PhuongTienController {

    @Autowired
    private PhuongTienService phuongTienService;

    // 2. Hàm lưu dữ liệu từ form gửi lên (Không dùng JS)
    @PostMapping("/luu")
    public String luuPhuongTien(@ModelAttribute("xeMoi") PhuongTien xe, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.dangKyXe(xe);
            // Nếu thành công, gửi một lời nhắn màu xanh sang trang web
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Thêm xe thành công!");
        } catch (Exception e) {
            // Nếu thất bại (Lỗi khóa ngoại Căn hộ hoặc trùng Biển số), gửi lời nhắn màu đỏ
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Thêm thất bại! Vui lòng kiểm tra lại Mã Căn Hộ có tồn tại không, hoặc Biển số xe đã bị trùng.");
        }
        return "redirect:/admin/phuong-tien";
    }

    // 3. Hàm xóa xe (Chỉ cần bấm link là xóa)
    @GetMapping("/xoa/{id}")
    public String xoaPhuongTien(@PathVariable("id") Long id) {
        phuongTienService.huyGuiXe(id);
        return "redirect:/admin/phuong-tien"; // Tự động load lại trang sau khi xóa
    }
    @GetMapping("/duyet/{id}")
    public String duyetXe(@PathVariable("id") Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            // Giả định bạn đã thêm hàm duyetXe vào PhuongTienService
            phuongTienService.duyetXe(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã duyệt phương tiện thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi duyệt xe: " + e.getMessage());
        }
        return "redirect:/admin/phuong-tien";
    }

    @GetMapping("/tu-choi/{id}")
    public String tuChoiXe(@PathVariable("id") Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.tuChoiXe(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã từ chối đăng ký phương tiện.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi từ chối xe: " + e.getMessage());
        }
        return "redirect:/admin/phuong-tien";
    }

    @GetMapping("/huy-duyet/{id}")
    public String huyDuyetXe(@PathVariable("id") Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.huyDuyetXe(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã hủy duyệt phương tiện.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi hủy duyệt xe: " + e.getMessage());
        }
        return "redirect:/admin/phuong-tien";
    }

    @GetMapping("/sua/{id}")
    public String suaLaiTrangThaiChoDuyet(@PathVariable("id") Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            phuongTienService.suaLaiTrangThaiChoDuyet(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã chuyển lại trạng thái Chờ duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi sửa trạng thái xe: " + e.getMessage());
        }
        return "redirect:/admin/phuong-tien";
    }
    // Tiếp nhận tham số tìm kiếm, gọi Service tương ứng và trả dữ liệu kèm từ khóa về giao diện
    @GetMapping
    public String hienThiTrang(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String tuKhoa,
                               @RequestParam(required = false) String trangThai,
                               @RequestParam(required = false) String loaiXe) {

        Page<PhuongTien> trangDuLieu;

        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) ||
                (trangThai != null && !trangThai.trim().isEmpty()) ||
                (loaiXe != null && !loaiXe.trim().isEmpty())) {
            trangDuLieu = phuongTienService.timKiemPhuongTien(tuKhoa, trangThai, loaiXe, page, size);
        } else {
            trangDuLieu = phuongTienService.danhSachXe(page, size);
        }

        model.addAttribute("danhSachXe", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);

        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("loaiXe", loaiXe);

        model.addAttribute("xeMoi", new PhuongTien());

        return "admin/phuong-tien";
    }
}