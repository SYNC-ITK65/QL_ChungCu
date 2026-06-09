package com.sync.itk65.controller;

import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
@RequestMapping("/admin/nguoi-dung") // Đường dẫn trên web: localhost:8080/admin/nguoi-dung

public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private MessageSource messageSource;



    // Hàm hiển thị danh sách người dùng
    @GetMapping
    public String hienThiDanhSach(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String tuKhoa,
            @RequestParam(required = false) Integer vaiTro,
            Model model) {
        
        org.springframework.data.domain.Page<NguoiDung> trangDuLieu;
        
        // Nếu có từ khóa tìm kiếm hoặc vai trò, thực hiện tìm kiếm
        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) || vaiTro != null) {
            trangDuLieu = nguoiDungService.timKiemNguoiDung(tuKhoa, vaiTro, page, size);
        } else {
            trangDuLieu = nguoiDungService.layTatCaNguoiDung(page, size);
        }

        // Nhờ Service lấy dữ liệu từ Database
        model.addAttribute("danhSachNguoiDung", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("vaiTro", vaiTro);

        // Trả về tên file HTML giao diện (sẽ tạo sau trong thư mục resources/templates)
        return "admin/nguoi_dung_list";
    }

    // Hàm hiển thị form tạo mới (Chỉ dành cho Admin / Nhân viên BQL)
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setVaiTro(1); // Mặc định là Admin
        model.addAttribute("nguoiDung", nguoiDung);
        return "admin/nguoi_dung_form";
    }

    // Hàm hiển thị form cập nhật (Sửa) - Chỉ cho Admin/Nhân viên
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        NguoiDung nguoiDung = nguoiDungService.layNguoiDungTheoId(id);

        if (nguoiDung == null) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy người dùng với ID: " + id);
            return "redirect:/admin/nguoi-dung";
        }

        // Nếu là Cư dân (vaiTro = 3), chuyển hướng sang module Quản lý cư dân
        if (nguoiDung.getVaiTro() == 3) {
            return "redirect:/admin/cu-dan/sua/" + id;
        }

        model.addAttribute("nguoiDung", nguoiDung);
        return "admin/nguoi_dung_form";
    }

    // Hàm xử lý lưu dữ liệu từ form (Chỉ lưu Admin/Quản lý/Lễ tân/Bảo vệ - NguoiDung thuần túy)
    @PostMapping("/luu")
    public String luuNguoiDung(@Valid @ModelAttribute("nguoiDung") NguoiDung nguoiDung, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        // Đảm bảo vai trò chỉ là Admin (1), Quản lý (2), Lễ tân (4) hoặc Bảo vệ (5)
        int vaiTro = nguoiDung.getVaiTro();
        if (vaiTro != 1 && vaiTro != 2 && vaiTro != 4 && vaiTro != 5) {
            Locale locale = LocaleContextHolder.getLocale();
            model.addAttribute("errorMessage", messageSource.getMessage("nd.error.invalidRole", null, "Module này chỉ quản lý tài khoản Admin, Quản lý, Lễ tân và Bảo vệ. Vui lòng sử dụng module Quản lý cư dân.", locale));
            return "admin/nguoi_dung_form";
        }

        // Xử lý mật khẩu: giữ mật khẩu cũ khi sửa, đặt mặc định khi tạo mới
        if (nguoiDung.getId() != null) {
            NguoiDung nguoiDungCu = nguoiDungService.layNguoiDungTheoId(nguoiDung.getId());
            if (nguoiDungCu != null) {
                // Nếu mật khẩu rỗng (không sửa), giữ nguyên mật khẩu cũ
                if (nguoiDung.getMatKhauMaHoa() == null || nguoiDung.getMatKhauMaHoa().isEmpty()) {
                    nguoiDung.setMatKhauMaHoa(nguoiDungCu.getMatKhauMaHoa());
                }
            }
        } else {
            // Tạo mới: đặt mật khẩu mặc định nếu rỗng
            if (nguoiDung.getMatKhauMaHoa() == null || nguoiDung.getMatKhauMaHoa().isEmpty()) {
                nguoiDung.setMatKhauMaHoa("1234");
            }
        }

        // Lọc bỏ lỗi validation trên các trường không có trong form (ví dụ: matKhauMaHoa đã bị ẩn)
        java.util.List<String> allowedFields = java.util.Arrays.asList(
            "hoTen", "tenDangNhap", "email", "soDienThoai", "vaiTro"
        );
        org.springframework.validation.BindingResult filteredResult = new org.springframework.validation.BeanPropertyBindingResult(
                nguoiDung, "nguoiDung");
        for (org.springframework.validation.FieldError error : bindingResult.getFieldErrors()) {
            if (allowedFields.contains(error.getField())) {
                filteredResult.addError(error);
            }
        }
        for (org.springframework.validation.ObjectError error : bindingResult.getGlobalErrors()) {
            filteredResult.addError(error);
        }

        if (filteredResult.hasErrors()) {
            // Đẩy lại lỗi vào model để Thymeleaf hiển thị
            model.addAttribute("org.springframework.validation.BindingResult.nguoiDung", filteredResult);
            return "admin/nguoi_dung_form";
        }

        try {
            nguoiDungService.luuNguoiDung(nguoiDung);
            return "redirect:/admin/nguoi-dung";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/nguoi_dung_form";
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            model.addAttribute("errorMessage", messageSource.getMessage("nd.error.save", null, "Có lỗi xảy ra khi lưu người dùng. Vui lòng kiểm tra lại thông tin!", locale));
            return "admin/nguoi_dung_form";
        }
    }

    // Xóa người dùng - Chặn xóa Cư dân từ module này
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable("id") Long id, RedirectAttributes ra) {
        NguoiDung nguoiDung = nguoiDungService.layNguoiDungTheoId(id);

        if (nguoiDung == null) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("nd.error.notFound", new Object[]{id}, "Không tìm thấy người dùng với ID: " + id, locale));
            return "redirect:/admin/nguoi-dung";
        }

        // Nếu là Cư dân, chuyển hướng sang module Quản lý cư dân để xóa
        if (nguoiDung.getVaiTro() == 3) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("errorMessage", messageSource.getMessage("nd.error.deleteCuDan", null, "Không thể xóa Cư dân từ module này. Vui lòng sử dụng trang Quản lý cư dân.", locale));
            return "redirect:/admin/nguoi-dung";
        }

        nguoiDungService.xoaNguoiDung(id);
        return "redirect:/admin/nguoi-dung";
    }

    // Reset mật khẩu về 1234
    @GetMapping("/reset-mat-khau/{id}")
    public String resetMatKhau(@PathVariable("id") Long id, java.util.Locale locale, RedirectAttributes ra) {
        try {
            NguoiDung nd = nguoiDungService.layNguoiDungTheoId(id);
            if (nd != null) {
                nguoiDungService.doiMatKhau(id, "1234");
                String successMsg = messageSource.getMessage("nd.msg.reset_mat_khau_thanh_cong", new Object[]{nd.getTenDangNhap()}, "Đã reset mật khẩu tài khoản thành công!", locale);
                ra.addFlashAttribute("successMessage", successMsg);
            } else {
                String errorMsg = messageSource.getMessage("nd.msg.reset_mat_khau_khong_tim_thay", null, "Không tìm thấy người dùng!", locale);
                ra.addFlashAttribute("errorMessage", errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = messageSource.getMessage("nd.msg.reset_mat_khau_loi", new Object[]{e.getMessage()}, "Có lỗi xảy ra!", locale);
            ra.addFlashAttribute("errorMessage", errorMsg);
        }
        return "redirect:/admin/nguoi-dung";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel() {
        byte[] bytes = nguoiDungService.xuatExcelDanhSachNguoiDung();

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_nguoi_dung_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }

}
