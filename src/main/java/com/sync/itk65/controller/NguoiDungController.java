package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        
        // Nếu là cư dân (vai trò 2 hoặc 3), lấy thêm thông tin cư dân
        if (nguoiDung.getVaiTro() == 2 || nguoiDung.getVaiTro() == 3) {
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
            // Admin: Tạo CuDan wrapper để Thymeleaf có thể bind các field canHo, moiQuanHe, trangThai
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

    // Hàm xử lý lưu dữ liệu từ form
    @PostMapping("/luu")
    public String luuNguoiDung(@ModelAttribute("nguoiDung") CuDan cuDan) {
        // Xử lý lỗi Căn hộ rỗng -> Hibernate không lưu được đối tượng có ID null
        if (cuDan.getCanHo() != null && cuDan.getCanHo().getId() == null) {
            cuDan.setCanHo(null);
        }

        if (cuDan.getVaiTro() == 1) {
            // Lưu admin: Chỉ lưu thông tin NguoiDung, tránh lưu vào cả bảng cu_dan
            NguoiDung admin = new NguoiDung();
            admin.setId(cuDan.getId()); // Nếu là update
            admin.setTenDangNhap(cuDan.getTenDangNhap());
            admin.setMatKhauMaHoa(cuDan.getMatKhauMaHoa());
            admin.setHoTen(cuDan.getHoTen());
            admin.setEmail(cuDan.getEmail());
            admin.setSoDienThoai(cuDan.getSoDienThoai());
            admin.setVaiTro(1);
            
            // Đặt mật khẩu mặc định nếu rỗng
            if (admin.getMatKhauMaHoa() == null || admin.getMatKhauMaHoa().isEmpty()) {
                admin.setMatKhauMaHoa("123456");
            }
            nguoiDungService.luuNguoiDung(admin);
        } else {
            // Lưu cư dân (Chủ hộ/Người thuê)
            if (cuDan.getMatKhauMaHoa() == null || cuDan.getMatKhauMaHoa().isEmpty()) {
                cuDan.setMatKhauMaHoa("123456");
            }
            cuDanService.luuCuDan(cuDan);
        }
        return "redirect:/admin/nguoi-dung";
    }

    // Xóa người dùng
    @GetMapping("/xoa/{id}")
    public String xoaNguoiDung(@PathVariable("id") Long id) {
        nguoiDungService.xoaNguoiDung(id);
        return "redirect:/admin/nguoi-dung";
    }

}
