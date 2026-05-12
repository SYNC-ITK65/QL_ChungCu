package com.sync.itk65.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.HoaDonService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cudan/hoa-don")
public class CuDanHoaDonController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private CuDanService cuDanService;

    /**
     * 1️⃣ Hiển thị DANH SÁCH HÓA ĐƠN của cư dân
     * URL: /cudan/hoa-don
     */
    @GetMapping
    public String hienThiDanhSachHoaDon(HttpSession session, Model model,
                                        @RequestParam(required = false) String trangThai,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        // Lấy thông tin cư dân từ session
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/"; // Chưa đăng nhập
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/"; // Không có cư dân hoặc không có căn hộ
        }

        // Lấy danh sách hóa đơn theo căn hộ
        List<HoaDon> danhSachHoaDon;
        Page<HoaDon> trangDuLieu;
        Pageable pageable = PageRequest.of(page, size);

        if (trangThai != null && !trangThai.isEmpty()) {
            if ("chua-dong".equalsIgnoreCase(trangThai)) {
                danhSachHoaDon = hoaDonRepository.findUnpaidByCanHoId(cuDan.getCanHo().getId());
                trangDuLieu = hoaDonRepository.findUnpaidByCanHoId(cuDan.getCanHo().getId(), pageable);
            } else if ("da-dong".equalsIgnoreCase(trangThai)) {
                danhSachHoaDon = hoaDonRepository.findPaidByCanHoId(cuDan.getCanHo().getId());
                trangDuLieu = hoaDonRepository.findPaidByCanHoId(cuDan.getCanHo().getId(), pageable);
            } else {
                danhSachHoaDon = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId());
                trangDuLieu = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId(), pageable);
            }
        } else {
            danhSachHoaDon = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId());
            trangDuLieu = hoaDonRepository.findByCanHoId(cuDan.getCanHo().getId(), pageable);
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
        model.addAttribute("danhSachHoaDon", trangDuLieu.getContent());
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("soHoaDonChuaDong", soHoaDonChuaDong);
        model.addAttribute("tongTienNo", tongTienNo);
        model.addAttribute("soHoaDonDaDong", soHoaDonDaDong);
        model.addAttribute("trangThaiFilter", trangThai != null ? trangThai : "");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieu.getTotalPages());
        model.addAttribute("size", size);
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
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
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
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return java.util.Map.of("error", "Chưa đăng nhập");
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null) {
            return java.util.Map.of("error", "Không tìm thấy cư dân");
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
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/";
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        if (cuDan == null || cuDan.getCanHo() == null) {
            return "redirect:/";
        }

        // Tái sử dụng logic từ hienThiDanhSachHoaDon
        return hienThiDanhSachHoaDon(session, model, trangThai, 1, 2 );
    }

    /**
     * 5️⃣ Thống kê hóa đơn (for dashboard)
     * URL: /cudan/hoa-don/thong-ke
     */
    @GetMapping("/thong-ke")
    @ResponseBody
    public java.util.Map<String, Object> thongKeHoaDon(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return java.util.Map.of("error", "Chưa đăng nhập");
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
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