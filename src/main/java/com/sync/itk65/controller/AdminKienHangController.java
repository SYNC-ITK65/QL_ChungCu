package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.KienHang;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.KienHangService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/kien-hang")
public class AdminKienHangController {

    @Autowired
    private KienHangService kienHangService;

    @Autowired
    private CanHoService canHoService;

    // @GetMapping("") có nghĩa là gì?
    // Nó có nghĩa là khi user gõ /admin/kien-hang thì sẽ vào hàm danhSach()
    @GetMapping("")
    public String danhSach(Model model,
            @RequestParam(defaultValue = "0") int page, // @RequestParam là tham số được truyền từ URL có thể thay đổi
            @RequestParam(defaultValue = "10") int size) { // ví dụ: ?page=1&size=10
        Page<KienHang> trangDuLieu = kienHangService.layTatCaKienHang(page, size); // page là số trang , size là số
                                                                                   // lượng
        model.addAttribute("danhSachKienHang", trangDuLieu.getContent()); // getContent() là lấy danh sách các phần tử
                                                                          // trong trang hiện tại
        model.addAttribute("currentPage", page); // currentPage là trang hiện tại
        model.addAttribute("totalPages", trangDuLieu.getTotalPages()); // totalPages là tổng số trang
        model.addAttribute("size", size); // size là số lượng phần tử trên mỗi trang
        return "admin/kien_hang_list";
    }

    @GetMapping("/them") // Khi user gõ /admin/kien-hang/them thì sẽ vào hàm hienThiFormThem()
    public String hienThiFormThem(Model model) {
        KienHang kienHang = new KienHang(); // Tạo ra 1 KienHang trống để
        kienHang.setCanHo(new CanHo()); // khởi tạo CanHo bên trong KienHang để tránh lỗi
        model.addAttribute("kienHang", kienHang); // Đưa KienHang trống vào Model để Thymeleaf có thể binding vào form
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo()); // Đưa danh sách CanHo vào Model để hiển thị
        return "admin/kien_hang_form"; // Trả về template admin/kien_hang_form.html
    }

    @PostMapping("/luu") // Khi user submit form /admin/kien-hang/luu thì sẽ vào hàm luuKienHang()
    public String luuKienHang(@ModelAttribute("kienHang") KienHang kienHang, RedirectAttributes ra) {
        kienHangService.luuKienHang(kienHang); // Gọi service để lưu KienHang
        ra.addFlashAttribute("thongBaoThanhCong", "Thêm kiện hàng mới thành công!"); // Thêm thông báo thành công
        return "redirect:/admin/kien-hang";
    }

    @GetMapping("/xac-nhan/{id}") // Khi user gõ /admin/kien-hang/xac-nhan/{id} thì sẽ vào hàm xacNhanDaNhan()
    public String xacNhanDaNhan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            kienHangService.xacNhanDaNhan(id); // Gọi service để xác nhận cư dân nhận hàng
            ra.addFlashAttribute("thongBaoThanhCong", "Đã xác nhận cư dân nhận hàng!"); // Thêm thông báo thành công
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/kien-hang";
    }
}
