package com.sync.itk65.controller;

import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.service.PhanAnhService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/phan-anh")
public class PhanAnhController {

    @Autowired
    private PhanAnhService phanAnhService;

    // 1. Xem danh sách phàn nàn của cư dân
    @GetMapping // Khi user gõ /admin/phan-anh thì sẽ vào hàm listPhanAnh()
    public String listPhanAnh(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PhanAnh> trangDuLieu = phanAnhService.findAll(page, size);
        model.addAttribute("phanAnhs", trangDuLieu.getContent()); // Đưa danh sách PhanAnh vào Model để Thymeleaf có thể
                                                                  // binding vào form
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);

        return "admin/phan_anh_list";
    }

    // 2. Mở form xem chi tiết và cập nhật trạng thái
    @GetMapping("/edit/{id}") // Khi user gõ /admin/phan-anh/edit/{id} thì sẽ vào hàm showEditForm()
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        PhanAnh phanAnh = phanAnhService.findById(id);
        if (phanAnh != null) {
            model.addAttribute("phanAnh", phanAnh);
            return "admin/phan_anh_form";
        }
        return "redirect:/admin/phan-anh";
    }

    // 3. Lưu lại trạng thái mới (Ví dụ: Đã sửa xong)
    @PostMapping("/save")
    public String savePhanAnh(@ModelAttribute("phanAnh") PhanAnh phanAnhForm) {
        // Lấy bản ghi gốc dưới database lên
        PhanAnh phanAnhGoc = phanAnhService.findById(phanAnhForm.getId());

        if (phanAnhGoc != null) {
            // Cập nhật trạng thái và phản hồi
            phanAnhGoc.setTrangThai(phanAnhForm.getTrangThai());
            phanAnhGoc.setPhanHoi(phanAnhForm.getPhanHoi());

            // Nếu có phản hồi mới thì set ngày phản hồi
            if (phanAnhForm.getPhanHoi() != null && !phanAnhForm.getPhanHoi().trim().isEmpty()) {
                if (phanAnhGoc.getNgayPhanHoi() == null) {
                    phanAnhGoc.setNgayPhanHoi(java.time.LocalDateTime.now()); // Sử dụng java.time.LocalDateTime để lấy
                                                                              // thời gian hiện tại
                }
            }
            phanAnhService.save(phanAnhGoc); // Lưu lại bản ghi đã cập nhật
        }
        return "redirect:/admin/phan-anh"; // Chuyển hướng về trang danh sách phàn nàn
    }
}