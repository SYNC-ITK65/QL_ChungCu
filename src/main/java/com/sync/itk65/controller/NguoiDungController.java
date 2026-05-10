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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/nguoi-dung") // Đường dẫn trên web: localhost:8080/admin/nguoi-dung

public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private CuDanService cuDanService;

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

    // Hàm xử lý lưu dữ liệu từ form (Chỉ lưu Admin/Nhân viên - NguoiDung thuần túy)
    @PostMapping("/luu")
    public String luuNguoiDung(@ModelAttribute("nguoiDung") NguoiDung nguoiDung, RedirectAttributes ra) {
        try {
            // Đảm bảo vai trò chỉ là Admin (1) hoặc Nhân viên (2)
            if (nguoiDung.getVaiTro() != 1 && nguoiDung.getVaiTro() != 2) {
                ra.addFlashAttribute("errorMessage", "Module này chỉ quản lý tài khoản Admin và Nhân viên. Vui lòng sử dụng module Quản lý cư dân.");
                return "redirect:/admin/nguoi-dung/tao-moi";
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

            nguoiDungService.luuNguoiDung(nguoiDung);
            return "redirect:/admin/nguoi-dung";
        } catch (IllegalArgumentException e) {
            // Ném thông báo lỗi về form thông qua biến flash attribute
            ra.addFlashAttribute("errorMessage", e.getMessage());

            // Trả người dùng về lại màn hình Cập nhật hoặc Thêm mới tương ứng
            if (nguoiDung.getId() != null) {
                return "redirect:/admin/nguoi-dung/sua/" + nguoiDung.getId();
            } else {
                return "redirect:/admin/nguoi-dung/tao-moi";
            }
        } catch (Exception e) {
            // Bắt tất cả lỗi khác (bao gồm ConstraintViolationException)
            ra.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi lưu người dùng. Vui lòng kiểm tra lại thông tin!");

            if (nguoiDung.getId() != null) {
                return "redirect:/admin/nguoi-dung/sua/" + nguoiDung.getId();
            } else {
                return "redirect:/admin/nguoi-dung/tao-moi";
            }
        }
    }

    // Xóa người dùng - Chặn xóa Cư dân từ module này
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable("id") Long id, RedirectAttributes ra) {
        NguoiDung nguoiDung = nguoiDungService.layNguoiDungTheoId(id);

        if (nguoiDung == null) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy người dùng với ID: " + id);
            return "redirect:/admin/nguoi-dung";
        }

        // Nếu là Cư dân, chuyển hướng sang module Quản lý cư dân để xóa
        if (nguoiDung.getVaiTro() == 3) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa Cư dân từ module này. Vui lòng sử dụng trang Quản lý cư dân.");
            return "redirect:/admin/nguoi-dung";
        }

        nguoiDungService.xoaNguoiDung(id);
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
