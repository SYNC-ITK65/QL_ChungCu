package com.sync.itk65.controller;

import com.sync.itk65.entity.*;
import com.sync.itk65.repository.*;
import com.sync.itk65.service.KhaoSatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/admin/khao-sat")
public class AdminKhaoSatController {

    @Autowired private KhaoSatService khaoSatService;
    @Autowired private CuDanRepository cuDanRepo;
    @Autowired private LichSuVoteRepository lichSuVoteRepo;

    @GetMapping
    public String index(@RequestParam(required = false) String tuKhoa,
                        @RequestParam(required = false) Integer trangThai,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        Model model) {

        // Gọi Service lọc danh sách
        org.springframework.data.domain.Page<KhaoSat> trangKhaoSat = khaoSatService.timKiemKhaoSat(tuKhoa, trangThai, page, size);

        // Đẩy dữ liệu lên giao diện
        model.addAttribute("listKhaoSat", trangKhaoSat.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangKhaoSat.getTotalPages());

        // Giữ lại tham số bộ lọc trên giao diện
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("size", size);

        return "admin/khao_sat_list";
    }

    @GetMapping("/tao-moi")
    public String showFormCreate(Model model) {
        KhaoSat ks = new KhaoSat();
        // Khởi tạo sẵn 2 ô rỗng cho giao diện dễ nhập
        ks.setDanhSachLuaChon(new ArrayList<>());
        ks.getDanhSachLuaChon().add(new LuaChonKhaoSat());
        ks.getDanhSachLuaChon().add(new LuaChonKhaoSat());
        model.addAttribute("khaoSat", ks);
        return "admin/khao_sat_form";
    }

    @GetMapping("/sua/{id}")
    public String showFormEdit(@PathVariable("id") Long id, Model model) {
        KhaoSat ks = khaoSatService.findById(id);
        model.addAttribute("khaoSat", ks);
        return "admin/khao_sat_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("khaoSat") KhaoSat khaoSat,
                       BindingResult result,
                       RedirectAttributes ra,
                       Model model) {

        // 1. LÀM SẠCH VÀ KIỂM TRA DỮ LIỆU CHUỖI
        if (khaoSat.getTieuDe() != null) khaoSat.setTieuDe(khaoSat.getTieuDe().trim());
        if (khaoSat.getMoTa() != null) khaoSat.setMoTa(khaoSat.getMoTa().trim());

        validateChuoi(khaoSat.getTieuDe(), 5, 100, "tieuDe", "Tiêu đề", result);
        validateChuoi(khaoSat.getMoTa(), 10, 500, "moTa", "Nội dung/Mô tả", result);

        // 2. KIỂM TRA CÁC PHƯƠNG ÁN (Lựa chọn)
        if (khaoSat.getDanhSachLuaChon() == null || khaoSat.getDanhSachLuaChon().isEmpty()) {
            model.addAttribute("globalError", "Khảo sát phải có ít nhất 2 phương án bình chọn hợp lệ.");
            result.reject("globalError");
        } else {
            khaoSat.getDanhSachLuaChon().removeIf(lc -> lc == null || lc.getNoiDungLuaChon() == null || lc.getNoiDungLuaChon().trim().isEmpty());

            if (khaoSat.getDanhSachLuaChon().size() < 2) {
                model.addAttribute("globalError", "Khảo sát phải có ít nhất 2 phương án bình chọn hợp lệ.");
                result.reject("globalError");
            } else {
                for (int i = 0; i < khaoSat.getDanhSachLuaChon().size(); i++) {
                    LuaChonKhaoSat lc = khaoSat.getDanhSachLuaChon().get(i);
                    lc.setNoiDungLuaChon(lc.getNoiDungLuaChon().trim());
                    validateChuoi(lc.getNoiDungLuaChon(), 2, 50, "danhSachLuaChon[" + i + "].noiDungLuaChon", "Phương án thứ " + (i+1), result);
                }
            }
        }

        // 3. KIỂM TRA THỜI GIAN
        LocalDateTime now = LocalDateTime.now();
        if (khaoSat.getThoiGianBatDau() == null) {
            result.rejectValue("thoiGianBatDau", "error.ks", "Vui lòng chọn thời gian bắt đầu.");
        } else if (khaoSat.getId() == null && khaoSat.getThoiGianBatDau().isBefore(now.minusMinutes(2))) {
            result.rejectValue("thoiGianBatDau", "error.ks", "Thời gian bắt đầu không được nằm trong quá khứ.");
        }

        if (khaoSat.getThoiGianKetThuc() == null) {
            result.rejectValue("thoiGianKetThuc", "error.ks", "Vui lòng chọn thời gian kết thúc.");
        } else if (khaoSat.getThoiGianBatDau() != null && khaoSat.getThoiGianKetThuc().isBefore(khaoSat.getThoiGianBatDau())) {
            result.rejectValue("thoiGianKetThuc", "error.ks", "Thời gian kết thúc phải diễn ra sau thời gian bắt đầu.");
        }

        // 4. KẾT LUẬN LỖI
        if (result.hasErrors() || model.containsAttribute("globalError")) {
            return "admin/khao_sat_form";
        }

        // THÀNH CÔNG -> Lưu CSDL
        khaoSatService.luuKhaoSat(khaoSat);
        ra.addFlashAttribute("msg", "Lưu khảo sát thành công!");
        return "redirect:/admin/khao-sat";
    }
    // --- HÀM KIỂM TRA CHUỖI ĐA LỚP ---
    private void validateChuoi(String input, int min, int max, String fieldName, String label, BindingResult result) {
        if (input == null || input.trim().isEmpty()) {
            result.rejectValue(fieldName, "error.ks", label + " không được để trống.");
            return;
        }
        String txt = input.trim();
        if (txt.length() < min || txt.length() > max) {
            result.rejectValue(fieldName, "error.ks", label + " phải từ " + min + " đến " + max + " ký tự.");
        } else if (!txt.matches(".*\\p{L}.*")) {
            result.rejectValue(fieldName, "error.ks", label + " phải chứa ít nhất một chữ cái (không được chỉ toàn số/ký tự đặc biệt).");
        } else if (txt.matches(".*(.)\\1{4,}.*")) {
            result.rejectValue(fieldName, "error.ks", label + " chứa ký tự lặp lại liên tiếp vô nghĩa (Vd: aaaaa).");
        } else if (txt.toLowerCase().matches(".*(asdf|qwerty|test|abc|hehe|xxxx).*")) {
            result.rejectValue(fieldName, "error.ks", label + " chứa mẫu dữ liệu rác không hợp lệ.");
        } else {
            int nonLetterCount = 0;
            for (char c : txt.toCharArray()) {
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) nonLetterCount++;
            }
            if ((double) nonLetterCount / txt.length() > 0.5) {
                result.rejectValue(fieldName, "error.ks", label + " chứa quá nhiều số hoặc ký tự bất thường (>50%).");
            }
        }
    }

    @GetMapping("/xoa/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        String rs = khaoSatService.xoaKhaoSat(id);
        if(rs.equals("SUCCESS")) ra.addFlashAttribute("msg", "Đã xóa thành công!");
        else ra.addFlashAttribute("err", rs);
        return "redirect:/admin/khao-sat";
    }

    @GetMapping("/ket-thuc/{id}")
    public String stop(@PathVariable("id") Long id) {
        khaoSatService.ketThucSom(id);
        return "redirect:/admin/khao-sat";
    }

    @GetMapping("/chi-tiet/{id}")
    public String detailAdmin(@PathVariable("id") Long id,
                              @RequestParam(required = false) String tuKhoa,
                              @RequestParam(required = false) Long luaChonId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        model.addAttribute("khaoSat", khaoSatService.findById(id));

        // Gọi Service tìm kiếm, lọc và phân trang
        org.springframework.data.domain.Page<LichSuVote> trangLichSu = khaoSatService.timKiemLichSuVote(id, luaChonId, tuKhoa, page, size);

        // Đẩy dữ liệu đã lọc lên giao diện
        model.addAttribute("lichSuList", trangLichSu.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangLichSu.getTotalPages());

        // Giữ lại tham số trên form sau khi load lại trang
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("luaChonId", luaChonId);
        model.addAttribute("size", size);

        return "admin/khao_sat_detail";
    }

    @GetMapping("/xuat-excel")
    public ResponseEntity<byte[]> xuatExcel(@RequestParam(required = false) String tuKhoa,
                                            @RequestParam(required = false) Integer trangThai) {
        byte[] bytes = khaoSatService.xuatExcelDanhSachKhaoSat(tuKhoa, trangThai);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "danh_sach_khao_sat_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }

    @GetMapping("/chi-tiet/{id}/xuat-excel")
    public ResponseEntity<byte[]> xuatExcelChiTiet(@PathVariable("id") Long id,
                                                   @RequestParam(required = false) String tuKhoa,
                                                   @RequestParam(required = false) Long luaChonId) {
        byte[] bytes = khaoSatService.xuatExcelLichSuVote(id, luaChonId, tuKhoa);

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "chi_tiet_khao_sat_" + id + "_" + ts + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}