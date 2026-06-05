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
        // Kiểm tra quyền Admin, Quản lý, Lễ tân hoặc Bảo vệ
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDungDangNhap");
        if (nguoiDung == null) {
            return "redirect:/";
        }
        int role = nguoiDung.getVaiTro();
        if (role != 1 && role != 2 && role != 4 && role != 5) {
            return "redirect:/"; // Về trang đăng nhập nếu chưa đăng nhập hoặc không phải admin/staff/receptionist/security
        }

        // Lấy số liệu từ DB
        long totalCanHo = canHoRepository.countTotalCanHo();
        long vacantCanHo = canHoRepository.countVacantCanHo();
        long residentResiding = cuDanRepository.countResidentResiding();
        long occupiedCanHo = totalCanHo - vacantCanHo;

        // Đẩy thông số cơ bản vào Model
        model.addAttribute("totalCanHo", totalCanHo);
        model.addAttribute("vacantCanHo", vacantCanHo);
        model.addAttribute("residentResiding", residentResiding);
        model.addAttribute("occupiedCanHo", occupiedCanHo);

        // Chỉ Admin (1) và Quản lý (2) mới được truy cập dữ liệu nhạy cảm
        if (role == 1 || role == 2) {
            Double sumRevenue = hoaDonRepository.sumRevenueCurrentMonth();
            // Xử lý null cho doanh thu (trường hợp chưa có hóa đơn nào)
            if (sumRevenue == null) {
                sumRevenue = 0.0;
            }

            // CHART: Doanh thu 6 tháng gần nhất
            java.time.LocalDate sixMonthsAgo = java.time.LocalDate.now().minusMonths(5).withDayOfMonth(1);
            java.util.List<Object[]> revenueDataRaw = hoaDonRepository.getRevenueLast6Months(sixMonthsAgo);
            
            java.util.List<String> labelsRevenue = new java.util.ArrayList<>();
            java.util.List<Double> dataRevenue = new java.util.ArrayList<>();
            
            java.time.LocalDate current = sixMonthsAgo;
            for (int i = 0; i < 6; i++) {
                int m = current.getMonthValue();
                int y = current.getYear();
                labelsRevenue.add("T" + m + "/" + y);
                
                double total = 0.0;
                for (Object[] row : revenueDataRaw) {
                    int dbMonth = ((Number) row[0]).intValue();
                    int dbYear = ((Number) row[1]).intValue();
                    if (dbMonth == m && dbYear == y) {
                        total = ((Number) row[2]).doubleValue();
                        break;
                    }
                }
                dataRevenue.add(total);
                current = current.plusMonths(1);
            }

            // Phản ánh: Đếm riêng "Chờ xử lý" và "Đang xử lý"
            long countChoXuLy = phanAnhRepository.countChoXuLy();
            long countDangXuLy = phanAnhRepository.countDangXuLy();

            // Tài sản cần bảo trì
            long countAlertTaiSan = taiSanRepository.findAlertAssets(java.time.LocalDate.now().plusDays(7)).size();

            // Lấy 2 phản ánh gần nhất
            org.springframework.data.domain.Pageable topTwo = org.springframework.data.domain.PageRequest.of(0, 2, org.springframework.data.domain.Sort.by("ngayGui").descending());
            java.util.List<com.sync.itk65.entity.PhanAnh> recentPhanAnh = phanAnhRepository.findAll(topTwo).getContent();

            // Đẩy vào Model
            model.addAttribute("labelsRevenue", labelsRevenue);
            model.addAttribute("dataRevenue", dataRevenue);
            model.addAttribute("sumRevenue", sumRevenue);
            model.addAttribute("countChoXuLy", countChoXuLy);
            model.addAttribute("countDangXuLy", countDangXuLy);
            model.addAttribute("countAlertTaiSan", countAlertTaiSan);
            model.addAttribute("recentPhanAnh", recentPhanAnh);
        }

        return "admin/dashboard";
    }
}
