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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    // Xem danh sách cư dân (Hỗ trợ phân trang, và áp dụng các tiêu chí tìm kiếm bao
    // gồm Lọc theo ID Căn hộ)
    @GetMapping
    public String hienThiDanhSach(
            @RequestParam(required = false) Long canHoId,
            @RequestParam(required = false) String tuKhoa,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int page, // Trang bắt đầu (số 0)
            @RequestParam(defaultValue = "10") int size, // Kích thước phân trang mặc định
            Model model) {

        // Đối tượng chứa kết quả cư dân kèm theo thông tin tổng số trang
        org.springframework.data.domain.Page<CuDan> trangDuLieuCuDan;

        // Xử lý nhánh tìm kiếm: Kiểm tra xem người dùng có chọn cụ thể một Căn hộ để
        // trích xuất không
        if (canHoId != null) {
            trangDuLieuCuDan = cuDanService.timKiemTheoCanHo(canHoId, tuKhoa, trangThai, page, size);
            model.addAttribute("canHoHienTai", canHoService.layCanHoTheoId(canHoId));
        } else {
            trangDuLieuCuDan = cuDanService.timKiemCuDan(tuKhoa, trangThai, page, size);
        }

        // Đẩy danh sách dữ liệu thực tế (danh sách cư dân của trang hiện tại) sang view
        model.addAttribute("danhSachCuDan", trangDuLieuCuDan.getContent());
        // Danh sách căn hộ hỗ trợ cho dropdown bộ lọc trên giao diện
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());

        // Cung cấp dữ liệu phân trang để build thanh Pagination trên View
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangDuLieuCuDan.getTotalPages());

        // Tham số bộ lọc được giữ lại để điền sẵn vào Form (Giữ nguyên trạng thái hiển
        // thị)
        model.addAttribute("canHoId", canHoId);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("size", size);

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
        if (cuDan == null)
            return "redirect:/admin/cu-dan";
        if (cuDan.getCanHo() == null)
            cuDan.setCanHo(new com.sync.itk65.entity.CanHo());
        model.addAttribute("cuDan", cuDan);
        model.addAttribute("danhSachCanHo", canHoService.layTatCaCanHo());
        return "admin/cu_dan_form";
    }

    // Xử lý lưu (Lưu chung cả user info và Resident info nhờ kế thừa)
    @PostMapping("/luu")
    public String luuCuDan(@ModelAttribute("cuDan") CuDan cuDan, RedirectAttributes ra) {
        // Xử lý lỗi Căn hộ rỗng -> Hibernate không lưu được đối tượng có ID null
        if (cuDan.getCanHo() != null && cuDan.getCanHo().getId() == null) {
            cuDan.setCanHo(null);
        }

        // Gán cứng vai trò 3 cho tất cả cư dân (User)
        cuDan.setVaiTro(3);

        // Giữ lại thông tin bảo mật khi sửa, đặt mặc định khi tạo mới
        if (cuDan.getId() != null) {
            CuDan cuDanCu = cuDanService.layCuDanTheoId(cuDan.getId());
            if (cuDanCu != null) {
                // Giữ lại mật khẩu cũ (form sửa cư dân không có trường mật khẩu)
                cuDan.setMatKhauMaHoa(cuDanCu.getMatKhauMaHoa());
                // Giữ lại tên đăng nhập nếu form không gửi lên hoặc bị null
                if (cuDan.getTenDangNhap() == null || cuDan.getTenDangNhap().isEmpty()) {
                    cuDan.setTenDangNhap(cuDanCu.getTenDangNhap());
                }
            }
        } else {
            // Tạo mới: đặt mật khẩu mặc định nếu rỗng
            if (cuDan.getMatKhauMaHoa() == null || cuDan.getMatKhauMaHoa().isEmpty()) {
                cuDan.setMatKhauMaHoa("1234");
            }
        }

        try {
            cuDanService.luuCuDan(cuDan);
            return "redirect:/admin/cu-dan";
        } catch (IllegalArgumentException e) {
            // Ném lỗi nghiệp vụ về lại file giao diện (form) để người dùng xem
            ra.addFlashAttribute("errorMessage", e.getMessage());

            if (cuDan.getId() != null) {
                return "redirect:/admin/cu-dan/sua/" + cuDan.getId();
            } else {
                return "redirect:/admin/cu-dan/tao-moi";
            }
        } catch (Exception e) {
            // Bắt tất cả lỗi khác (bao gồm ConstraintViolationException) tránh lỗi 500
            ra.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi lưu cư dân. Vui lòng kiểm tra lại thông tin!");

            if (cuDan.getId() != null) {
                return "redirect:/admin/cu-dan/sua/" + cuDan.getId();
            } else {
                return "redirect:/admin/cu-dan/tao-moi";
            }
        }
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
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}