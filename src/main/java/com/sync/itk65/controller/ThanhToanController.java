package com.sync.itk65.controller;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.ThanhToan;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.service.ThanhToanService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import org.springframework.context.MessageSource;
import java.util.Locale;

@Controller
@RequestMapping("/admin/thanh-toan")
public class ThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private MessageSource messageSource;

    // 1. Xem danh sách Lịch sử thanh toán
    // Đường dẫn: http://localhost:8080/admin/thanh-toan/lich-su
    @GetMapping("/lich-su")
    public String danhSachThanhToan(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String maCanHo,
                                    @RequestParam(required = false) String phuongThuc,
                                    @RequestParam(required = false) LocalDate tuNgay,
                                    @RequestParam(required = false) LocalDate denNgay) {
        Page<ThanhToan> trangDuLieu = thanhToanService.timKiemThanhToan(maCanHo, phuongThuc, tuNgay, denNgay, page, size);
        model.addAttribute("danhSachThanhToan", trangDuLieu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("maCanHo", maCanHo);
        model.addAttribute("phuongThuc", phuongThuc);
        model.addAttribute("tuNgay", tuNgay);
        model.addAttribute("denNgay", denNgay);
        return "admin/thanh_toan_list";
    }

    // 2. Mở Form xác nhận thu tiền
    // Đường dẫn: http://localhost:8080/admin/thanh-toan/xac-nhan/{id}
    @GetMapping("/xac-nhan/{id}")
    public String hienThiFormThanhToan(@PathVariable("id") Long hoaDonId, Model model) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa Đơn với ID: " + hoaDonId));

        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setNgayThanhToan(LocalDate.now());

        // Tính phí trễ hạn (nếu có) để hiển thị và set mặc định số tiền phải thu
        var fee = thanhToanService.tinhPhiTreHan(hoaDon, thanhToan.getNgayThanhToan());
        thanhToan.setSoTien(fee.totalDue());

        // Nếu hóa đơn đang ở trạng thái "Chờ duyệt", mặc định là chuyển khoản QR
        if ("Chờ duyệt".equals(hoaDon.getTrangThaiThanhToan())) {
            thanhToan.setPhuongThuc("Chuyển khoản QR");
        }

        model.addAttribute("thanhToan", thanhToan);
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("daysLate", fee.daysLate());
        model.addAttribute("lateFee", fee.lateFee());
        model.addAttribute("totalDue", fee.totalDue());
        return "admin/thanh_toan_form"; // Đã thêm admin/
    }

    // 3. Xử lý lưu thanh toán
    @PostMapping("/luu")
    public String luuThanhToan(@ModelAttribute("thanhToan") ThanhToan thanhToan, RedirectAttributes ra) {
        try {
            thanhToanService.thucHienThanhToan(thanhToan);
            Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("thanhToan.success.xacNhan", null, "Xác nhận thanh toán thành công.", locale));
            // Redirect về trang lịch sử thanh toán thay vì danh sách hóa đơn
            return "redirect:/admin/thanh-toan/lich-su";
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
            return "redirect:/admin/hoa-don";
        }
    }
}