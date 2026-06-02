package com.sync.itk65.controller;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.service.HoaDonService;
import com.sync.itk65.repository.CanHoRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/hoa-don")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private CanHoRepository canHoRepository;

    // Hiển thị danh sách
    @GetMapping
    public String hienThiDanhSach(Model model,
                                  @RequestParam(required = false) String maCanHo,
                                  @RequestParam(required = false) String trangThai,
                                  @RequestParam(required = false) Integer thang,
                                  @RequestParam(required = false) Integer nam) {
        List<HoaDon> danhSachHoaDon;

        // Xử lý empty string thành null để query hoạt động đúng
        String maCanHoFilter = (maCanHo != null && !maCanHo.trim().isEmpty()) ? maCanHo.trim() : null;
        String trangThaiFilter = (trangThai != null && !trangThai.trim().isEmpty()) ? trangThai.trim() : null;

        // Nếu có tham số tìm kiếm, sử dụng filter
        if (maCanHoFilter != null || trangThaiFilter != null || thang != null || nam != null) {
            danhSachHoaDon = hoaDonService.timKiemHoaDon(maCanHoFilter, trangThaiFilter, thang, nam);
        } else {
            danhSachHoaDon = hoaDonService.layTatCaHoaDon();
        }

        model.addAttribute("danhSachHoaDon", danhSachHoaDon);
        model.addAttribute("maCanHo", maCanHo != null ? maCanHo : "");
        model.addAttribute("trangThai", trangThai != null ? trangThai : "");
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        return "admin/hoa_don_list";
    }

    // Mở trang Form tạo mới
    // 1. API Hiển thị form tạo mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("hoaDon", new HoaDon());
        
        // Nếu có lỗi từ redirect, hiển thị trên form
        if (error != null && !error.isEmpty()) {
            try {
                String decodedError = java.net.URLDecoder.decode(error, java.nio.charset.StandardCharsets.UTF_8.name());
                model.addAttribute("errorMessage", decodedError);
            } catch (Exception e) {
                model.addAttribute("errorMessage", error);
            }
        }

        // Gửi danh sách căn hộ sang Form để làm menu xổ xuống
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());

        return "admin/hoa_don_form";
    }

    // Xử lý lưu hóa đơn khi bấm nút Lưu
    @PostMapping("/luu")
    public String luuHoaDon(@RequestParam("ngayPhatHanh") String ngayPhatHanhStr,
                            @RequestParam("ngayDenHan") String ngayDenHanStr,
                            @RequestParam("canHoId") Long canHoId) {
        try {
            System.out.println("DEBUG: Creating invoice with dates: " + ngayPhatHanhStr + " to " + ngayDenHanStr + " for apartment ID: " + canHoId);
            
            // Validate inputs
            if (ngayPhatHanhStr == null || ngayPhatHanhStr.trim().isEmpty()) {
                throw new RuntimeException("Vui lòng chọn ngày phát hành");
            }
            if (ngayDenHanStr == null || ngayDenHanStr.trim().isEmpty()) {
                throw new RuntimeException("Vui lòng chọn ngày đến hạn");
            }
            if (canHoId == null) {
                throw new RuntimeException("Vui lòng chọn căn hộ");
            }
            
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayPhatHanh(LocalDate.parse(ngayPhatHanhStr));
            hoaDon.setNgayDenHan(LocalDate.parse(ngayDenHanStr));

            CanHo canHo = new CanHo();
            canHo.setId(canHoId);
            hoaDon.setCanHo(canHo);

            // Trigger tính tiền tự động toàn bộ
            hoaDonService.taoHoaDonTuDong(hoaDon);
            
            System.out.println("DEBUG: Invoice created successfully");

            return "redirect:/admin/hoa-don";

        } catch (RuntimeException e) {
            System.out.println("DEBUG: Runtime Exception: " + e.getMessage());
            // e.getMessage() sẽ trả về "Chưa ghi nhận chỉ số điện nước..."
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/admin/hoa-don/tao-moi?error=" + errorMessage;
        } catch (Exception e) {
            System.out.println("DEBUG: General Exception: " + e.getMessage());
            e.printStackTrace();
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/admin/hoa-don/tao-moi?error=" + errorMessage;
        }
    }

    // API Xử lý khi người dùng bấm nút "Xem"
    @GetMapping("/xem/{id}")
    public String xemHoaDon(@PathVariable("id") Long id, Model model) {
        hoaDonService.themChiTietHoaDonVaoModel(id, model);
        return "admin/hoa_don_chi_tiet";
    }

    // API Xử lý khi người dùng bấm nút "Sửa"
    @GetMapping("/sua/{id}")
    public String suaHoaDon(@PathVariable("id") Long id, Model model) {
        HoaDon hoaDon = hoaDonService.layHoaDonById(id);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("danhSachCanHo", canHoRepository.findAll());
        return "admin/hoa_don_sua";
    }

    // API Xử lý khi người dùng bấm nút "Lưu thay đổi"
    @PostMapping("/cap-nhat")
    public String capNhatHoaDon(@ModelAttribute("hoaDon") HoaDon hoaDon,
                               @RequestParam("canHoId") Long canHoId) {
        try {
            CanHo canHo = new CanHo();
            canHo.setId(canHoId);
            hoaDon.setCanHo(canHo);

            hoaDonService.capNhatHoaDon(hoaDon);
            return "redirect:/admin/hoa-don";

        } catch (RuntimeException e) {
            return "redirect:/admin/hoa-don/sua/" + hoaDon.getId() + "?error=" + e.getMessage();
        }
    }

    // API Xử lý khi người dùng bấm nút "Thanh toán"
//    @GetMapping("/thanh-toan/{id}")
//    public String thanhToan(@PathVariable("id") Long id) {
//        hoaDonService.danhDauDaThanhToan(id);
//        return "redirect:/admin/hoa-don"; // Quay về trang danh sách
//    }

    // API Xử lý khi người dùng bấm nút "Xóa"
    @GetMapping("/xoa/{id}")
    public String xoaHoaDon(@PathVariable("id") Long id) {
        try {
            hoaDonService.xoaHoaDon(id);
            return "redirect:/admin/hoa-don";
        } catch (RuntimeException e) {
            return "redirect:/admin/hoa-don?error=" + e.getMessage();
        }
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel() {
        byte[] bytes = hoaDonService.xuatExcelDanhSachHoaDon();
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_hoa_don_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }

    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("thongBaoLoi", "Vui lòng chọn file Excel để import!");
            return "redirect:/admin/hoa-don";
        }
        try {
            String ketQua = hoaDonService.importExcelHoaDon(file);
            ra.addFlashAttribute("thongBaoThanhCong", ketQua);
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi import: " + e.getMessage());
        }
        return "redirect:/admin/hoa-don";
    }
}