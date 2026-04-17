package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CanHoService;
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
    private CanHoService canHoService;

    @Autowired
    private CuDanService cuDanService;

    // Hàm hiển thị danh sách người dùng
    @GetMapping
    public String hienThiDanhSach(Model model) {
        // Nhờ Service lấy dữ liệu từ Database
        model.addAttribute("danhSachNguoiDung", nguoiDungService.layTatCaNguoiDung());

        // Trả về tên file HTML giao diện (sẽ tạo sau trong thư mục resources/templates)
        return "admin/nguoi_dung_list";
    }

    // Hàm hiển thị form tạo mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model) {
        CuDan cuDan = new CuDan();
        cuDan.setCanHo(new com.sync.itk65.entity.CanHo()); // Tránh NPE
        model.addAttribute("nguoiDung", cuDan);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/nguoi_dung_form";
    }

    // Hàm hiển thị form cập nhật (Sửa)
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        NguoiDung nguoiDung = nguoiDungService.layNguoiDungTheoId(id);

        // Nếu là cư dân (vai trò 3), lấy thêm thông tin cư dân
        if (nguoiDung.getVaiTro() == 3) {
            CuDan cuDan = cuDanService.layCuDanTheoId(id);
            if (cuDan != null) {
                model.addAttribute("nguoiDung", cuDan);
            } else {
                // Nếu không tìm thấy CuDan, tạo CuDan từ NguoiDung
                CuDan cuDanMoi = new CuDan();
                cuDanMoi.setId(nguoiDung.getId());
                cuDanMoi.setTenDangNhap(nguoiDung.getTenDangNhap());
                cuDanMoi.setMatKhauMaHoa(nguoiDung.getMatKhauMaHoa());
                cuDanMoi.setHoTen(nguoiDung.getHoTen());
                cuDanMoi.setEmail(nguoiDung.getEmail());
                cuDanMoi.setSoDienThoai(nguoiDung.getSoDienThoai());
                cuDanMoi.setVaiTro(nguoiDung.getVaiTro());
                cuDanMoi.setCanHo(new com.sync.itk65.entity.CanHo());
                model.addAttribute("nguoiDung", cuDanMoi);
            }
        } else {
            // Admin: Tạo CuDan wrapper để Thymeleaf có thể bind các field canHo, moiQuanHe,
            // trangThai
            CuDan cuDanWrapper = new CuDan();
            cuDanWrapper.setId(nguoiDung.getId());
            cuDanWrapper.setTenDangNhap(nguoiDung.getTenDangNhap());
            cuDanWrapper.setMatKhauMaHoa(nguoiDung.getMatKhauMaHoa());
            cuDanWrapper.setHoTen(nguoiDung.getHoTen());
            cuDanWrapper.setEmail(nguoiDung.getEmail());
            cuDanWrapper.setSoDienThoai(nguoiDung.getSoDienThoai());
            cuDanWrapper.setVaiTro(nguoiDung.getVaiTro());
            cuDanWrapper.setCanHo(new com.sync.itk65.entity.CanHo()); // Tránh NPE
            model.addAttribute("nguoiDung", cuDanWrapper);
        }

        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/nguoi_dung_form";
    }

    // Hàm xử lý lưu dữ liệu từ form (Kèm bắt lỗi Validator bằng Exception)
    @PostMapping("/luu")
    public String luuNguoiDung(@ModelAttribute("nguoiDung") CuDan cuDan, RedirectAttributes ra) {
        // Xử lý lỗi Căn hộ rỗng -> Hibernate không lưu được đối tượng có ID null
        if (cuDan.getCanHo() != null && cuDan.getCanHo().getId() == null) {
            cuDan.setCanHo(null);
        }

        try {
            if (cuDan.getVaiTro() == 1 || cuDan.getVaiTro() == 2) {
                // Lưu admin/staff: Chỉ lưu thông tin NguoiDung, tránh lưu vào cả bảng cu_dan
                NguoiDung adminStaff = new NguoiDung();
                adminStaff.setId(cuDan.getId()); // Nếu là update
                adminStaff.setTenDangNhap(cuDan.getTenDangNhap());
                adminStaff.setMatKhauMaHoa(cuDan.getMatKhauMaHoa());
                adminStaff.setHoTen(cuDan.getHoTen());
                adminStaff.setEmail(cuDan.getEmail());
                adminStaff.setSoDienThoai(cuDan.getSoDienThoai());
                adminStaff.setVaiTro(cuDan.getVaiTro());

                // Đặt mật khẩu mặc định nếu rỗng
                if (adminStaff.getMatKhauMaHoa() == null || adminStaff.getMatKhauMaHoa().isEmpty()) {
                    adminStaff.setMatKhauMaHoa("1234");
                }
                nguoiDungService.luuNguoiDung(adminStaff);
            } else {
                // Lưu cư dân (Chủ hộ/Người thuê)
                if (cuDan.getMatKhauMaHoa() == null || cuDan.getMatKhauMaHoa().isEmpty()) {
                    cuDan.setMatKhauMaHoa("1234");
                }
                cuDanService.luuCuDan(cuDan);
            }
            return "redirect:/admin/nguoi-dung";
        } catch (IllegalArgumentException e) {
            // Ném thông báo lỗi về form thông qua biến flash attribute
            ra.addFlashAttribute("errorMessage", e.getMessage());

            // Trả người dùng về lại màn hình Cập nhật hoặc Thêm mới tương ứng
            if (cuDan.getId() != null) {
                return "redirect:/admin/nguoi-dung/sua/" + cuDan.getId();
            } else {
                return "redirect:/admin/nguoi-dung/tao-moi";
            }
        }
    }

    // Xóa người dùng
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable("id") Long id) {
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
