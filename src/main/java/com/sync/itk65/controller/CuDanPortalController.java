package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.entity.DichVu;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.DatDichVuService;
import com.sync.itk65.service.DichVuService;
import com.sync.itk65.service.PhuongTienService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.dao.DataIntegrityViolationException;
import com.sync.itk65.service.CloudinaryService;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.service.ThongBaoService;
import com.sync.itk65.service.KhaoSatService;
import com.sync.itk65.service.PhanAnhService;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.ThongBao;
import com.sync.itk65.entity.KhaoSat;
import com.sync.itk65.entity.PhanAnh;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Controller
@RequestMapping("/cudan")
public class CuDanPortalController {

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private DichVuService dichVuService;

    @Autowired
    private DatDichVuService datDichVuService;

    @Autowired
    private PhuongTienService phuongTienService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ThongBaoService thongBaoService;

    @Autowired
    private KhaoSatService khaoSatService;

    @Autowired
    private PhanAnhService phanAnhService;

    @Autowired
    private MessageSource messageSource;

    // =======================================================
    // BẢNG ĐIỀU KHIỂN CƯ DÂN (DASHBOARD)
    // =======================================================
    @GetMapping("/dashboard")
    public String viewDashboard(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (user == null) {
            return "redirect:/login";
        }
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        model.addAttribute("cuDan", cuDan);

        if (cuDan != null && cuDan.getCanHo() != null) {
            Long canHoId = cuDan.getCanHo().getId();
            String maCanHo = cuDan.getCanHo().getMaCanHo();
            String tang = cuDan.getCanHo().getTang() != null ? String.valueOf(cuDan.getCanHo().getTang()) : null;

            // 1. Hoa don gan nhat can dong (unpaid)
            List<HoaDon> unpaidInvoices = hoaDonRepository.findUnpaidByCanHoId(canHoId);
            HoaDon recentInvoice = null;
            if (unpaidInvoices != null && !unpaidInvoices.isEmpty()) {
                recentInvoice = unpaidInvoices.get(0); // oldest unpaid, which is the most urgent
            }
            model.addAttribute("recentInvoice", recentInvoice);
            model.addAttribute("unpaidCount", unpaidInvoices != null ? unpaidInvoices.size() : 0);

            // 2. Thong bao / Cam nang gan nhat
            Page<ThongBao> newsPage = thongBaoService.locThongBaoTheoCuDan(1, maCanHo, tang, 0, 3);
            Page<ThongBao> handbookPage = thongBaoService.locThongBaoTheoCuDan(2, maCanHo, tang, 0, 3);
            model.addAttribute("recentAnnouncements", newsPage.getContent());
            model.addAttribute("recentHandbooks", handbookPage.getContent());

            // 3. Khao sat dang mo
            Page<KhaoSat> activeSurveys = khaoSatService.timKiemKhaoSat(null, 2, 0, 3);
            model.addAttribute("activeSurveys", activeSurveys.getContent());

            // 4. Tinh trang phan anh cua minh
            List<PhanAnh> myFeedbacks = phanAnhService.findByCanHoId(canHoId);
            // Limit to 3 items
            List<PhanAnh> recentFeedbacks = myFeedbacks.stream().limit(3).collect(Collectors.toList());
            model.addAttribute("recentFeedbacks", recentFeedbacks);

            // Extra metrics for summary stats card
            // Vehicles count for the apartment
            List<PhuongTien> vehicles = phuongTienService.layXeTheoCanHoId(canHoId);
            model.addAttribute("vehicleCount", vehicles != null ? vehicles.size() : 0);
        } else {
            model.addAttribute("recentInvoice", null);
            model.addAttribute("unpaidCount", 0);
            model.addAttribute("recentAnnouncements", List.of());
            model.addAttribute("recentHandbooks", List.of());
            model.addAttribute("activeSurveys", List.of());
            model.addAttribute("recentFeedbacks", List.of());
            model.addAttribute("vehicleCount", 0);
        }

        return "cudan/dashboard";
    }

    // =======================================================
    // CHỨC NĂNG 1: THÔNG TIN CÁ NHÂN & CĂN HỘ
    // =======================================================
    @GetMapping("/thong-tin")
    public String xemThongTin(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        model.addAttribute("cuDan", cuDan);
        return "cudan/thong_tin";
    }

    // =======================================================
    // CHỨC NĂNG 2: QUẢN LÝ DỊCH VỤ (Hồ bơi, Gym, BBQ...)
    // =======================================================

    // 2.1 Hiển thị trang đăng ký và lịch sử dịch vụ
    @GetMapping("/dich-vu")
    public String hienThiTrangDichVu(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        // Gửi danh sách các dịch vụ đang có ở chung cư để cư dân chọn
        model.addAttribute("danhSachDichVu", dichVuService.layTatCaDichVu());
        // Lấy lịch sử dịch vụ mà cư dân này đã đặt
        model.addAttribute("lichSuDat", datDichVuService.layLichSuDatCuaCuDan(cuDan.getId()));

        return "cudan/dich_vu";
    }

    // 2.2 Xử lý khi cư dân bấm nút "Đăng ký"
    @PostMapping("/dich-vu/dat")
    public String datDichVu(HttpSession session,
            @RequestParam Long dichVuId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDat,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
            @RequestParam(required = false) String ghiChu,
            RedirectAttributes redirectAttributes) {

        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        DichVu dichVu = dichVuService.layDichVuTheoId(dichVuId);

        if (ngayKetThuc != null && ngayKetThuc.isBefore(ngayDat)) {
            Locale locale = LocaleContextHolder.getLocale();
            redirectAttributes.addFlashAttribute("thongBaoLoi", messageSource.getMessage("cd.portal.error.invalidDate", null, "Ngày kết thúc không thể trước ngày bắt đầu sử dụng!", locale));
            return "redirect:/cudan/dich-vu";
        }

        // Tạo đối tượng đặt dịch vụ mới
        DatDichVu datDichVu = new DatDichVu();
        datDichVu.setCuDan(cuDan);
        datDichVu.setDichVu(dichVu);
        datDichVu.setNgayDat(ngayDat);
        datDichVu.setNgayKetThuc(ngayKetThuc);
        datDichVu.setGhiChu(ghiChu);

        // TRẠNG THÁI MẶC ĐỊNH LÀ CHỜ DUYỆT ĐỂ ADMIN XÁC NHẬN RỒI MỚI TÍNH TIỀN
        datDichVu.setTrangThai("Chờ duyệt");

        // Gọi service lưu vào DB
        datDichVuService.dangKyDichVu(datDichVu);

        // Gửi thông báo thành công
        Locale locale = LocaleContextHolder.getLocale();
        redirectAttributes.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("cd.portal.success.registerDichVu", null, "Đăng ký dịch vụ thành công! Vui lòng chờ BQL duyệt.", locale));
        return "redirect:/cudan/dich-vu";
    }

    // =======================================================
    // CHỨC NĂNG 3: QUẢN LÝ PHƯƠNG TIỆN (Gửi xe)
    // =======================================================

    // 3.1 Hiển thị trang quản lý xe của Cư dân
    @GetMapping("/phuong-tien")
    public String hienThiPhuongTien(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        // Nếu cư dân có căn hộ, load danh sách xe của căn hộ đó ra
        if (cuDan.getCanHo() != null) {
            model.addAttribute("danhSachXe", phuongTienService.layXeTheoCanHoId(cuDan.getCanHo().getId()));
        }

        model.addAttribute("xeMoi", new PhuongTien());
        return "cudan/phuong-tien";
    }

    // 3.2 Xử lý khi Cư dân bấm gửi yêu cầu đăng ký xe mới
    @PostMapping("/phuong-tien/luu")
    public String dangKyXeCuDan(HttpSession session,
            @ModelAttribute("xeMoi") PhuongTien xe,
            BindingResult result,
            @RequestParam("fileImage") MultipartFile multipartFile,
            Model model,
            RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        try {
            if (cuDan.getCanHo() == null) {
                Locale locale = LocaleContextHolder.getLocale();
                throw new RuntimeException(messageSource.getMessage("cd.portal.error.noCanHo", null, "Tài khoản của bạn chưa được liên kết với Căn hộ nào!", locale));
            }

            xe.setCanHo(cuDan.getCanHo());
            xe.setTrangThai("Chờ duyệt");

            // Kiểm tra dung lượng ảnh (giới hạn 2MB)
            if (multipartFile != null && !multipartFile.isEmpty()) {
                if (multipartFile.getSize() > 2 * 1024 * 1024) {
                    Locale locale = LocaleContextHolder.getLocale();
                    result.rejectValue("hinhAnh", "error.xe.hinhAnh.size", messageSource.getMessage("cd.portal.error.imageSize", null, "Dung lượng ảnh không được vượt quá 2MB!", locale));
                }
            }

            kiemTraLogicDangKyXe(xe, result);

            if (result.hasErrors()) {
                model.addAttribute("danhSachXe", phuongTienService.layXeTheoCanHoId(cuDan.getCanHo().getId()));
                Locale locale = LocaleContextHolder.getLocale();
                model.addAttribute("thongBaoLoi", messageSource.getMessage("cd.portal.error.invalidForm", null, "Dữ liệu đăng ký không hợp lệ, vui lòng kiểm tra lại form!", locale));
                return "cudan/phuong-tien";
            }

            // Upload hình ảnh lên Cloudinary
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(multipartFile);
                xe.setHinhAnh(imageUrl);
            }

            phuongTienService.dangKyXe(xe);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("cd.portal.success.registerXe", null, "Đã gửi yêu cầu đăng ký xe! Vui lòng chờ BQL duyệt.", locale));

        } catch (DataIntegrityViolationException e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("cd.portal.error.duplicatePlate", null, "Từ chối: Biển số xe này đã tồn tại trong hệ thống bãi đỗ!", locale));
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("cd.portal.error.general", new Object[]{e.getMessage()}, "Lỗi: " + e.getMessage(), locale));
        }
        return "redirect:/cudan/phuong-tien";
    }

    private void kiemTraLogicDangKyXe(PhuongTien xe, BindingResult result) {

        // Kiểm tra Loại xe
        if (xe.getLoaiXe() == null || xe.getLoaiXe().trim().isEmpty()) {
            result.rejectValue("loaiXe", "error.xe", "Vui lòng chọn loại xe");
        }

        // Kiểm tra Màu xe
        if (xe.getMauXe() == null || xe.getMauXe().trim().isEmpty()) {
            result.rejectValue("mauXe", "error.xe", "Vui lòng nhập màu xe.");
        } else if (!xe.getMauXe().matches("^[\\p{L}\\s]+$")) {
            result.rejectValue("mauXe", "error.xe", "Màu xe chỉ được dùng chữ cái, không chứa số hay ký tự đặc biệt.");
        }

        // Kiểm tra Biển số xe (Tách riêng Ô tô và Xe máy)
        if ("Ô tô".equals(xe.getLoaiXe())) {
            if (xe.getBienSoXe() == null || xe.getBienSoXe().trim().isEmpty()) {
                result.rejectValue("bienSoXe", "error.xe", "Ô tô bắt buộc phải nhập biển số!");
            } else if (!xe.getBienSoXe().trim().matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9\\s.-]+$")) {
                result.rejectValue("bienSoXe", "error.xe.bienSoXe.oto.pattern", "Biển số Ô tô phải bao gồm cả chữ và số.");
            }
        } else if ("Xe máy".equals(xe.getLoaiXe())) {
            if (xe.getBienSoXe() == null || xe.getBienSoXe().trim().isEmpty()) {
                result.rejectValue("bienSoXe", "error.xe", "Xe máy bắt buộc phải nhập biển số!");
            } else if (!xe.getBienSoXe().trim().matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9\\s.-]+$")) {
                result.rejectValue("bienSoXe", "error.xe.bienSoXe.xemay.pattern", "Biển số Xe máy phải bao gồm cả chữ và số.");
            }
        } else if ("Xe điện".equals(xe.getLoaiXe())) {
            if (xe.getBienSoXe() == null || xe.getBienSoXe().trim().isEmpty()) {
                result.rejectValue("bienSoXe", "error.xe", "Xe điện bắt buộc phải nhập biển số (hoặc số khung)!");
            } else if (!xe.getBienSoXe().trim().matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9\\s.-]+$")) {
                result.rejectValue("bienSoXe", "error.xe.bienSoXe.xedien.pattern", "Biển số hoặc số khung xe điện phải bao gồm cả chữ và số.");
            }
        }
    }

    // 3.3 Xử lý khi cư dân muốn hủy/xóa xe (Chỉ cho xóa nếu không muốn đăng ký nữa)
    @GetMapping("/phuong-tien/xoa/{id}")
    public String xoaXeCuDan(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            phuongTienService.huyGuiXe(id);
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoThanhCong", messageSource.getMessage("cd.portal.success.cancelXe", null, "Đã hủy đăng ký xe thành công.", locale));
        } catch (Exception e) {
            Locale locale = LocaleContextHolder.getLocale();
            ra.addFlashAttribute("thongBaoLoi", messageSource.getMessage("cd.portal.error.general", new Object[]{e.getMessage()}, "Lỗi: " + e.getMessage(), locale));
        }
        return "redirect:/cudan/phuong-tien";
    }

    @GetMapping("/can-ho")
    public String thongTinCanHo(HttpSession session, Model model) {
        // 1. Lấy thông tin người dùng đang đăng nhập từ Session
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");

        if (user == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập thì quay về trang login
        }

        // 2. Tìm thông tin Cư dân dựa trên ID người dùng
        // (Vì ID cư dân trùng với ID người dùng trong thiết kế của bạn)
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        if (cuDan != null && cuDan.getCanHo() != null) {
            // 3. Lấy đối tượng Căn hộ từ cư dân và gửi sang giao diện
            model.addAttribute("canHo", cuDan.getCanHo());
        }

        return "cudan/can_ho_detail"; // Trả về file HTML hiển thị thông tin
    }
}