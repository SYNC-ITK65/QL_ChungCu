package com.sync.itk65.controller;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.service.CanHoService;
import com.sync.itk65.service.CuDanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/cu-dan")
public class CuDanController {
    @Autowired
    private CuDanService cuDanService;
    @Autowired
    private CanHoService canHoService;

    // Xem danh sách cư dân (Có thể lọc theo căn hộ, từ khóa, trạng thái, và có phân trang)
    @GetMapping
    public String hienThiDanhSach(
            @RequestParam(required = false) Long canHoId,
            @RequestParam(required = false) String tuKhoa,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        org.springframework.data.domain.Page<CuDan> pageCuDan;

        if (canHoId != null) {
            pageCuDan = cuDanService.timKiemTheoCanHo(canHoId, tuKhoa, trangThai, page, size);
            model.addAttribute("canHoHienTai", canHoService.layCanHoTheoId(canHoId));
        } else {
            pageCuDan = cuDanService.timKiemCuDan(tuKhoa, trangThai, page, size);
        }

        model.addAttribute("danhSachCuDan", pageCuDan.getContent());
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageCuDan.getTotalPages());
        model.addAttribute("canHoId", canHoId);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        
        return "admin/cu_dan_list";
    }

    // Form thêm mới
    @GetMapping("/tao-moi")
    public String hienThiFormTaoMoi(@RequestParam(required = false) Long canHoId, Model model) {
        CuDan cuDan = new CuDan();
        if (canHoId != null) {
            cuDan.setCanHo(canHoService.layCanHoTheoId(canHoId));
        } else {
            cuDan.setCanHo(new com.sync.itk65.entity.CanHo()); // Đảm bảo CanHo không null để tránh lỗi Thymeleaf
        }
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/cu_dan_form";
    }

    // Form sửa cư dân
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable("id") Long id, Model model) {
        CuDan cuDan = cuDanService.layCuDanTheoId(id);
        if (cuDan == null) return "redirect:/admin/cu-dan";
        if (cuDan.getCanHo() == null) cuDan.setCanHo(new com.sync.itk65.entity.CanHo());
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/cu_dan_form";
    }

    // Xử lý lưu (Lưu chung cả user info và Resident info nhờ kế thừa)
    @PostMapping("/luu")
    public String luuCuDan(@ModelAttribute("cuDan") CuDan cuDan) {
        // Xử lý lỗi Căn hộ rỗng -> Hibernate không lưu được đối tượng có ID null
        if (cuDan.getCanHo() != null && cuDan.getCanHo().getId() == null) {
            cuDan.setCanHo(null);
        }

        // Gán cứng vai trò 3 cho tất cả cư dân (User)
        cuDan.setVaiTro(3);

        // Giữ lại mật khẩu cũ khi sửa, đặt mặc định khi tạo mới
        if (cuDan.getId() != null) {
            CuDan cuDanCu = cuDanService.layCuDanTheoId(cuDan.getId());
            if (cuDanCu != null) {
                cuDan.setMatKhauMaHoa(cuDanCu.getMatKhauMaHoa());
            }
        } else if (cuDan.getMatKhauMaHoa() == null || cuDan.getMatKhauMaHoa().isEmpty()) {
            cuDan.setMatKhauMaHoa("123456");
        }

        cuDanService.luuCuDan(cuDan);
        return "redirect:/admin/cu-dan";
    }

    // Xóa cư dân chuyển đi
    @GetMapping("/xoa/{id}")
    public String xoaCuDan(@PathVariable("id") Long id) {
        cuDanService.xoaCuDan(id);
        return "redirect:/admin/cu-dan";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel(@RequestParam(required = false) Long canHoId) {
        byte[] bytes = cuDanService.xuatExcelDanhSachCuDan(canHoId);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = (canHoId == null)
                ? ("danh_sach_cu_dan_" + ts + ".xlsx")
                : ("danh_sach_cu_dan_can_ho_" + canHoId + "_" + ts + ".xlsx");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}