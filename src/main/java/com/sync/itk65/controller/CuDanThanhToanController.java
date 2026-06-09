package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.service.ThanhToanService;
import com.sync.itk65.service.CuDanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
@RequestMapping("/cudan/thanh-toan")
public class CuDanThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/lich-su")
    public String xemLichSuThanhToan(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }

        model.addAttribute(
                "danhSachThanhToan",
                thanhToanService.layLichSuTheoCanHo(cuDan.getCanHo().getId()));
        return "cudan/thanh_toan_lich_su";
    }

    /**
     * Hiển thị trang thanh toán QR code
     * URL: /cudan/thanh-toan/qr/{hoaDonId}
     */
    @GetMapping("/qr/{hoaDonId}")
    public String hienThiTrangThanhToanQR(@PathVariable("hoaDonId") Long hoaDonId,
            HttpSession session,
            Model model,
            RedirectAttributes ra) {
        // Kiểm tra đăng nhập
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }

        // Lấy hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElse(null);
        if (hoaDon == null) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("loi", messageSource.getMessage("hd.error.notFound", null, "Không tìm thấy hóa đơn", locale));
            return "redirect:/cudan/hoa-don";
        }

        // Kiểm tra quyền: chỉ cư dân của căn hộ này mới được xem
        if (!hoaDon.getCanHo().getId().equals(cuDan.getCanHo().getId())) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("loi", messageSource.getMessage("hd.error.noPermission", null, "Bạn không có quyền xem hóa đơn này", locale));
            return "redirect:/cudan/hoa-don";
        }

        model.addAttribute("hoaDon", hoaDon);
        return "cudan/thanh_toan_qr";
    }

    /**
     * Xác nhận đã thanh toán qua QR (chuyển sang trạng thái "Chờ duyệt")
     * URL: /cudan/thanh-toan/xac-nhan-qr
     */
    @PostMapping("/xac-nhan-qr")
    public String xacNhanThanhToanQR(@RequestParam("hoaDonId") Long hoaDonId,
            HttpSession session,
            RedirectAttributes ra) {
        // Kiểm tra đăng nhập
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }

        // Lấy hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElse(null);
        if (hoaDon == null) {
            ra.addFlashAttribute("loi", "Không tìm thấy hóa đơn");
            return "redirect:/cudan/hoa-don";
        }

        // Kiểm tra quyền
        if (!hoaDon.getCanHo().getId().equals(cuDan.getCanHo().getId())) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("loi", messageSource.getMessage("hd.error.actionNoPermission", null, "Bạn không có quyền thực hiện hành động này", locale));
            return "redirect:/cudan/hoa-don";
        }

        // Kiểm tra trạng thái hiện tại
        if (!"Chưa đóng".equals(hoaDon.getTrangThaiThanhToan())) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("loi", messageSource.getMessage("hd.error.notPayable", null, "Hóa đơn này không ở trạng thái có thể thanh toán", locale));
            return "redirect:/cudan/hoa-don";
        }

        // Chuyển sang trạng thái "Chờ duyệt"
        hoaDon.setTrangThaiThanhToan("Chờ duyệt");
        hoaDonRepository.save(hoaDon);

        Locale locale = LocaleContextHolder.getLocale();
        ra.addFlashAttribute("thanhCong", messageSource.getMessage("hd.success.payQR", null, "Đã gửi xác nhận thanh toán. Vui lòng chờ admin duyệt.", locale));
        return "redirect:/cudan/hoa-don";
    }
}
