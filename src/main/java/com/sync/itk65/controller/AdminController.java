package com.sync.itk65.controller;

import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.CuDanRepository;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.PhanAnhRepository;
import com.sync.itk65.repository.TaiSanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private CuDanRepository cuDanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private PhanAnhRepository phanAnhRepository;

    @Autowired
    private TaiSanRepository taiSanRepository;

    @GetMapping
    public String index() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Kiểm tra quyền Admin hoặc Nhân Viên
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (nguoiDung == null || (nguoiDung.getVaiTro() != 1 && nguoiDung.getVaiTro() != 2)) {
            return "redirect:/"; // Về trang đăng nhập nếu chưa đăng nhập hoặc không phải admin/staff
        }

        // Lấy số liệu từ DB
        long totalCanHo = canHoRepository.countTotalCanHo();
        long vacantCanHo = canHoRepository.countVacantCanHo();
        long residentResiding = cuDanRepository.countResidentResiding();
        Double sumRevenue = hoaDonRepository.sumRevenueCurrentMonth();

        // Xử lý null cho doanh thu (trường hợp chưa có hóa đơn nào)
        if (sumRevenue == null) {
            sumRevenue = 0.0;
        }

        // Phản ánh: Đếm riêng "Chờ xử lý" và "Đang xử lý" (SỬ DỤNG LIKE ĐỂ TRÁNH LỖI ĐÁNH VẦN)
        long countChoXuLy = phanAnhRepository.countChoXuLy();
        long countDangXuLy = phanAnhRepository.countDangXuLy();

        // Tài sản cần bảo trì
        long countAlertTaiSan = taiSanRepository.findAlertAssets(java.time.LocalDate.now().plusDays(7)).size();

        // Đẩy vào Model
        model.addAttribute("totalCanHo", totalCanHo);
        model.addAttribute("vacantCanHo", vacantCanHo);
        model.addAttribute("residentResiding", residentResiding);
        model.addAttribute("sumRevenue", sumRevenue);
        model.addAttribute("countChoXuLy", countChoXuLy);
        model.addAttribute("countDangXuLy", countDangXuLy);
        model.addAttribute("countAlertTaiSan", countAlertTaiSan);

        return "admin/dashboard";
    }
}
