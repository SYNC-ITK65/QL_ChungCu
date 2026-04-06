package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.service.HoaDonService;
import com.sync.itk65.repository.HoaDonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cudan/hoa-don")
public class CuDanHoaDonController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonService hoaDonService;

    /**
     * 1️⃣ Hiển thị DANH SÁCH HÓA ĐƠN của cư dân
     * URL: /cudan/hoa-don
     */
    @GetMapping
    public String hienThiDanhSachHoaDon(HttpSession session, Model model,
                                        @RequestParam(required = false) String trangThai) {
        // Lấy thông tin cư dân từ session
        CuDan cuDan = (CuDan) session.getAttribute("nguoiDungDangNhap");

        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/"; // Chưa đăng nhập hoặc không có căn hộ
        }

        // Lấy danh sách hóa đơn theo căn hộ
        List<HoaDon> danhSachHoaDon;

        if (trangThai != null && !trangThai.isEmpty()) {
            if ("chua-dong".equalsIgnoreCase(trangThai)) {
                danhSachHoaDon = hoaDonRepository.findUnpaidByCanHoId(cuDan.getCanHo().getId());
            } else if ("da-dong".equalsIgnoreCase(trangThai)) {
                danhSachHoaDon = hoaDonRepository.findPaidByCanHoId(cuDan.getCanHo().getId());
            } else {
                danhSachHoaDon = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId());
            }
        } else {
            danhSachHoaDon = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId());
        }

        // === TÍNH TOÁN THỐNG KÊ ===
        long soHoaDonChuaDong = danhSachHoaDon.stream()
                .filter(hd -> "Chưa đóng".equalsIgnoreCase(hd.getTrangThaiThanhToan()))
                .count();

        Double tongTienNo = danhSachHoaDon.stream()
                .filter(hd -> "Chưa đóng".equalsIgnoreCase(hd.getTrangThaiThanhToan()))
                .mapToDouble(HoaDon::getTongTien)
                .sum();

        long soHoaDonDaDong = danhSachHoaDon.stream()
                .filter(hd -> "Đã đóng".equalsIgnoreCase(hd.getTrangThaiThanhToan()))
                .count();

        // Đẩy dữ liệu xuống view
        model.addAttribute("danhSachHoaDon", danhSachHoaDon);
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("soHoaDonChuaDong", soHoaDonChuaDong);
        model.addAttribute("tongTienNo", tongTienNo);
        model.addAttribute("soHoaDonDaDong", soHoaDonDaDong);
        model.addAttribute("trangThaiFilter", trangThai != null ? trangThai : "");
        return "cudan/hoa_don_list";
    }
    /**
     * 2️⃣ Hiển thị CHI TIẾT HÓA ĐƠN của cư dân
     * URL: /cudan/hoa-don/chi-tiet/{id}
     */
    @GetMapping("/chi-tiet/{id}")
    public String xemChiTietHoaDon(@PathVariable("id") Long hoaDonId,
                                   HttpSession session,
                                   Model model) {
        // Kiểm tra đăng nhập
        CuDan cuDan = (CuDan) session.getAttribute("nguoiDungDangNhap");

        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }
        // Lấy hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));
        // 🔒 KIỂM TRA QUYỀN: Chỉ cư dân có căn hộ này mới được xem
        if (!hoaDon.getCanHo().getId().equals(cuDan.getCanHo().getId())) {
            return "redirect:/cudan/hoa-don";
        }
        // Thêm hóa đơn vào model
        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("cuDan", cuDan);
        // Gọi service để tính chi tiết và thêm vào model
        hoaDonService.themChiTietHoaDonVaoModel(hoaDonId, model);
        return "cudan/hoa_don_chi_tiet";
    }

    /**
     * 3️⃣ API: Lấy chi tiết hóa đơn (JSON)
     * URL: /cudan/hoa-don/api/chi-tiet/{id}
     */
    @GetMapping("/api/chi-tiet/{id}")
    @ResponseBody
    public java.util.Map<String, Object> layChiTietHoaDonAPI(@PathVariable("id") Long hoaDonId,
                                                             HttpSession session) {
        CuDan cuDan = (CuDan) session.getAttribute("nguoiDungDangNhap");

        if (cuDan == null) {
            return java.util.Map.of("error", "Chưa đăng nhập");
        }

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElse(null);
        if (hoaDon == null || !hoaDon.getCanHo().getId().equals(cuDan.getCanHo().getId())) {
            return java.util.Map.of("error", "Không có quyền truy cập");
        }

        return hoaDonService.tinhChiTietHoaDon(hoaDonId);
    }

    /**
     * 4️⃣ Lọc hóa đơn theo trạng thái (AJAX)
     * URL: /cudan/hoa-don/loc
     */
    @GetMapping("/loc")
    public String locHoaDon(@RequestParam(required = false) String trangThai,
                            HttpSession session,
                            Model model) {
        CuDan cuDan = (CuDan) session.getAttribute("nguoiDungDangNhap");

        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }

        // Tái sử dụng logic từ hienThiDanhSachHoaDon
        return hienThiDanhSachHoaDon(session, model, trangThai);
    }

    /**
     * 5️⃣ Thống kê hóa đơn (for dashboard)
     * URL: /cudan/hoa-don/thong-ke
     */
    @GetMapping("/thong-ke")
    @ResponseBody
    public java.util.Map<String, Object> thongKeHoaDon(HttpSession session) {
        CuDan cuDan = (CuDan) session.getAttribute("nguoiDungDangNhap");

        if (cuDan == null || cuDan.getCanHo() == null) {
            return java.util.Map.of("error", "Không có quyền");
        }

        List<HoaDon> allInvoices = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId());

        long unpaid = allInvoices.stream()
                .filter(hd -> "Chưa đóng".equalsIgnoreCase(hd.getTrangThaiThanhToan()))
                .count();

        Double totalUnpaid = allInvoices.stream()
                .filter(hd -> "Chưa đóng".equalsIgnoreCase(hd.getTrangThaiThanhToan()))
                .mapToDouble(HoaDon::getTongTien)
                .sum();

        long paid = allInvoices.stream()
                .filter(hd -> "Đã đóng".equalsIgnoreCase(hd.getTrangThaiThanhToan()))
                .count();

        return java.util.Map.of(
                "hoaDonChuaDong", unpaid,
                "tongTienNo", totalUnpaid,
                "hoaDonDaDong", paid,
                "tongHoaDon", allInvoices.size()
        );
    }
}