package com.sync.itk65.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                            @RequestParam(required = false) String ghiChu,
                            RedirectAttributes redirectAttributes) {

        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        DichVu dichVu = dichVuService.layDichVuTheoId(dichVuId);

        // Tạo đối tượng đặt dịch vụ mới
        DatDichVu datDichVu = new DatDichVu();
        datDichVu.setCuDan(cuDan);
        datDichVu.setDichVu(dichVu);
        datDichVu.setNgayDat(ngayDat);
        datDichVu.setGhiChu(ghiChu);

        // TRẠNG THÁI MẶC ĐỊNH LÀ CHỜ DUYỆT ĐỂ ADMIN XÁC NHẬN RỒI MỚI TÍNH TIỀN
        datDichVu.setTrangThai("Chờ duyệt");

        // Gọi service lưu vào DB
        datDichVuService.dangKyDichVu(datDichVu);

        // Gửi thông báo thành công
        redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đăng ký dịch vụ thành công! Vui lòng chờ BQL duyệt.");
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
                                RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());

        try {
            // Ràng buộc: Xe này tự động thuộc về Căn hộ của cư dân đang đăng nhập
            if (cuDan.getCanHo() == null) {
                throw new RuntimeException("Tài khoản của bạn chưa được liên kết với Căn hộ nào!");
            }
            xe.setCanHo(cuDan.getCanHo());

            // Trạng thái mặc định khi cư dân đăng ký là "Chờ duyệt"
            xe.setTrangThai("Chờ duyệt");

            phuongTienService.dangKyXe(xe);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã gửi yêu cầu đăng ký xe! Vui lòng chờ BQL duyệt.");
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/cudan/phuong-tien";
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