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
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Ngày kết thúc không thể trước ngày bắt đầu sử dụng!");
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
        redirectAttributes.addFlashAttribute("thongBaoThanhCong",
                "Đăng ký dịch vụ thành công! Vui lòng chờ BQL duyệt.");
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
                throw new RuntimeException("Tài khoản của bạn chưa được liên kết với Căn hộ nào!");
            }

            xe.setCanHo(cuDan.getCanHo());
            xe.setTrangThai("Chờ duyệt");

            // Kiểm tra dung lượng ảnh (giới hạn 2MB)
            if (multipartFile != null && !multipartFile.isEmpty()) {
                if (multipartFile.getSize() > 2 * 1024 * 1024) {
                    result.rejectValue("hinhAnh", "error.xe.hinhAnh.size", "Dung lượng ảnh không được vượt quá 2MB!");
                }
            }

            kiemTraLogicDangKyXe(xe, result);

            if (result.hasErrors()) {
                model.addAttribute("danhSachXe", phuongTienService.layXeTheoCanHoId(cuDan.getCanHo().getId()));
                model.addAttribute("thongBaoLoi", "Dữ liệu đăng ký không hợp lệ, vui lòng kiểm tra lại form!");
                return "cudan/phuong-tien";
            }

            // Upload hình ảnh lên Cloudinary
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(multipartFile);
                xe.setHinhAnh(imageUrl);
            }

            phuongTienService.dangKyXe(xe);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã gửi yêu cầu đăng ký xe! Vui lòng chờ BQL duyệt.");

        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("thongBaoLoi", "Từ chối: Biển số xe này đã tồn tại trong hệ thống bãi đỗ!");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi: " + e.getMessage());
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
            ra.addFlashAttribute("thongBaoThanhCong", "Đã hủy đăng ký xe thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", "Lỗi: " + e.getMessage());
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