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

// @Controller: Đánh dấu class này là "nhân viên tiếp tân", chuyên nhận request
// từ user rồi điều hướng
// @RequestMapping: Cái "địa chỉ nhà" để dẫn đường cho request tìm đến đúng
// method cần gặp
@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired // Spring sẽ tự động tìm và gán 1 object CanHoRepository vào đây
    // Tương tự như private CanHoRepository canHoRepository = new CanHoRepository();

    private CanHoRepository canHoRepository;

    @Autowired
    private CuDanRepository cuDanRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private PhanAnhRepository phanAnhRepository;

    @Autowired
    private TaiSanRepository taiSanRepository;

    // @GetMapping: Dùng để nhận nhiệm vụ khi request GET tới để vào thẳng trang
    // dashboard
    @GetMapping
    public String index() {
        return "redirect:/admin/dashboard"; // Trả về trang dashboard
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Kiểm tra quyền Admin hoặc Nhân Viên
        NguoiDung nguoiDung = (NguoiDung) session.getAttribute("nguoiDungDangNhap"); // Lấy thông tin người dùng từ
                                                                                     // session (session là nơi lưu trữ
                                                                                     // thông tin người dùng sau khi
                                                                                     // đăng nhập)

        // Kiểm tra quyền Admin hoặc Nhân Viên
        // Kiểm tra nếu không có người dùng hoặc vai trò không phải admin/staff (1 là
        // Admin, 2 là Nhân viên)
        if (nguoiDung == null || (nguoiDung.getVaiTro() != 1 && nguoiDung.getVaiTro() != 2)) {

            return "redirect:/"; // Về trang đăng nhập nếu chưa đăng nhập hoặc không phải admin/staff
        }

        // Lấy số liệu từ DB
        long totalCanHo = canHoRepository.countTotalCanHo(); // Đếm tổng số căn hộ
        long vacantCanHo = canHoRepository.countVacantCanHo(); // Đếm số căn hộ trống
        long residentResiding = cuDanRepository.countResidentResiding(); // Đếm số cư dân đang ở
        Double sumRevenue = hoaDonRepository.sumRevenueCurrentMonth(); // Tính tổng doanh thu tháng này

        // Xử lý null cho doanh thu (trường hợp chưa có hóa đơn nào)
        if (sumRevenue == null) {
            sumRevenue = 0.0; // Nếu không có hóa đơn thì doanh thu là 0.0
        }

        // CHART: Doanh thu 6 tháng gần nhất
        java.time.LocalDate sixMonthsAgo = java.time.LocalDate.now().minusMonths(5).withDayOfMonth(1); // Lấy ngày 1 của
                                                                                                       // tháng 6 tháng
                                                                                                       // trước (để đảm
                                                                                                       // bảo tính đủ 6
                                                                                                       // tháng)
        java.util.List<Object[]> revenueDataRaw = hoaDonRepository.getRevenueLast6Months(sixMonthsAgo); // Lấy doanh thu
                                                                                                        // 6 tháng gần
                                                                                                        // nhất từ DB

        java.util.List<String> labelsRevenue = new java.util.ArrayList<>(); // Tạo list để lưu trữ nhãn (tháng)
        java.util.List<Double> dataRevenue = new java.util.ArrayList<>(); // Tạo list để lưu trữ dữ liệu (doanh thu)

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

        long occupiedCanHo = totalCanHo - vacantCanHo;
        model.addAttribute("labelsRevenue", labelsRevenue);
        model.addAttribute("dataRevenue", dataRevenue);
        model.addAttribute("occupiedCanHo", occupiedCanHo);

        // Phản ánh: Đếm riêng "Chờ xử lý" và "Đang xử lý" (SỬ DỤNG LIKE ĐỂ TRÁNH LỖI
        // ĐÁNH VẦN)
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
