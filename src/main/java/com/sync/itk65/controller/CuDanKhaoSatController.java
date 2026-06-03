package com.sync.itk65.controller;

import com.sync.itk65.entity.*;
import com.sync.itk65.repository.*;
import com.sync.itk65.service.KhaoSatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cudan/khao-sat")
public class CuDanKhaoSatController {

    @Autowired private KhaoSatService khaoSatService;
    @Autowired private CuDanRepository cuDanRepo;
    @Autowired private LichSuVoteRepository lichSuVoteRepo;

    private CuDan layCuDanSession(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if(user == null) return null;
        // Giả sử NguoiDung và CuDan dùng chung ID, hoặc map tương tự các controller khác của bạn
        return cuDanRepo.findById(user.getId()).orElse(null);
    }

    @GetMapping
    public String list(@RequestParam(required = false) String tuKhoa,
                       @RequestParam(required = false) Integer trangThai,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model, HttpSession session) {

        if(layCuDanSession(session) == null) return "redirect:/";

        // Sử dụng hàm phân trang mới thay cho layTatCa() cũ
        org.springframework.data.domain.Page<KhaoSat> trangKhaoSat = khaoSatService.timKiemKhaoSat(tuKhoa, trangThai, page, size);

        // Đẩy dữ liệu lên giao diện
        model.addAttribute("listKhaoSat", trangKhaoSat.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trangKhaoSat.getTotalPages());

        // Giữ lại tham số bộ lọc
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("size", size);

        return "cudan/khao_sat_list";
    }

    @GetMapping("/chi-tiet/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        CuDan cuDan = layCuDanSession(session);
        if(cuDan == null) return "redirect:/";

        KhaoSat ks = khaoSatService.findById(id);
        LichSuVote ls = lichSuVoteRepo.findByCuDanAndKhaoSat(cuDan, ks).orElse(null);

        model.addAttribute("khaoSat", ks);
        model.addAttribute("lichSu", ls);
        return "cudan/khao_sat_detail";
    }

    @PostMapping("/vote")
    public String vote(@RequestParam("khaoSatId") Long khaoSatId,
                       @RequestParam("luaChonId") Long luaChonId,
                       HttpSession session, RedirectAttributes ra) {
        CuDan cuDan = layCuDanSession(session);
        String rs = khaoSatService.thucHienBinhChon(cuDan, khaoSatId, luaChonId);

        if(rs.startsWith("SUCCESS")) ra.addFlashAttribute("msg", "Ghi nhận bình chọn thành công!");
        else ra.addFlashAttribute("err", rs);

        return "redirect:/cudan/khao-sat/chi-tiet/" + khaoSatId;
    }
}