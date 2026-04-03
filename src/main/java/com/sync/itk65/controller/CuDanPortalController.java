package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.entity.DichVu;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.service.CuDanService;
import com.sync.itk65.service.DatDichVuService;
import com.sync.itk65.service.DichVuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // ----- CHỨC NĂNG 1: THÔNG TIN CÁ NHÂN (Giữ nguyên của bạn) -----
    @GetMapping("/thong-tin")
    public String xemThongTin(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        CuDan cuDan = cuDanService.layCuDanTheoId(user.getId());
        model.addAttribute("cuDan", cuDan);
        return "cudan/thong_tin";
    }

    // ----- CHỨC NĂNG 2: ĐĂNG KÝ DỊCH VỤ -----
    // 2.1 Hiển thị trang đăng ký và lịch sử
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

        // Gọi service lưu vào DB
        datDichVuService.dangKyDichVu(datDichVu);

        // Gửi thông báo thành công
        redirectAttributes.addFlashAttribute("thongBao", "Đăng ký dịch vụ thành công! Vui lòng chờ BQL duyệt.");
        return "redirect:/cudan/dich-vu";
    }
}