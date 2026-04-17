package com.sync.itk65.controller;

import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    // Hàm lấy danh sách doanh thu đóng gói cho Biểu đồ
    @GetMapping("/doanh-thu-6-thang")
    public Map<String, Object> getDoanhThuSauThang() {
        // Dùng hàm LocalDate lùi về 5 tháng trước (kèm tháng hiện tại sẽ bằng 6 tháng)
        LocalDate mocThoiGian = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        
        // Nhận bảng dữ liệu trả về từ JPQL cơ sở dữ liệu
        List<Object[]> danhSachKetQua = hoaDonRepository.getRevenueLast6Months(mocThoiGian);

        // Khai báo tập hợp List cơ bản chứa nhãn Tháng/Năm và dữ liệu Tổng Tiền để Chart.js hiển thị
        List<String> danhSachNhanThang = new ArrayList<>();
        List<Double> danhSachDoanhThu = new ArrayList<>();

        // Sử dụng vòng lặp for truyền thống để duyệt từng dòng kết quả
        for (Object[] dongDuLieu : danhSachKetQua) {
            // Ép kiểu theo cấu trúc SELECT của Repository: 0 là Tháng, 1 là Năm, 2 là Tổng tiền
            Integer thang = ((Number) dongDuLieu[0]).intValue();
            Integer nam = ((Number) dongDuLieu[1]).intValue();
            Double doanhThuThang = ((Number) dongDuLieu[2]).doubleValue();

            // Nối chuỗi để hiển thị đẹp đẽ
            danhSachNhanThang.add("Tháng " + thang + "/" + nam);
            danhSachDoanhThu.add(doanhThuThang);
        }

        // Đóng gói mảng danhSach vào cấu trúc dữ liệu Map<String, Object> (Sẽ tự động ánh xạ ra JSON phía Frontend)
        Map<String, Object> bieuDoDoanhThu = new HashMap<>();
        bieuDoDoanhThu.put("labels", danhSachNhanThang);
        bieuDoDoanhThu.put("data", danhSachDoanhThu);

        return bieuDoDoanhThu;
    }

    // Hàm tính toán và đóng gói Tỉ lệ chung cư dùng cho Biểu đồ tròn
    @GetMapping("/ti-le-can-ho")
    public Map<String, Object> getTiLeCanHo() {
        // Chủ động truy vấn đếm từ Repository
        long tongSoCanHo = canHoRepository.countTotalCanHo();
        long soCanHoTrong = canHoRepository.countVacantCanHo();

        // Sử dụng toán tử trừ cơ bản để lấy ra số lượng phòng đã lấp người
        long soCanHoDaCoNguoiO = tongSoCanHo - soCanHoTrong;

        // Khai báo Mảng (List) chứa Nhãn mô tả trạng thái
        List<String> danhSachNhanTrangThai = new ArrayList<>();
        danhSachNhanTrangThai.add("Đã có cư dân ở");
        danhSachNhanTrangThai.add("Đang để trống");

        // Khai báo Mảng (List) chứa giá trị tương ứng
        List<Long> danhSachTiLeCanHo = new ArrayList<>();
        danhSachTiLeCanHo.add(soCanHoDaCoNguoiO);
        danhSachTiLeCanHo.add(soCanHoTrong);

        // Đóng gói đối tượng bằng Map (Hiển thị mảng json)
        Map<String, Object> bieuDoTiLe = new HashMap<>();
        bieuDoTiLe.put("labels", danhSachNhanTrangThai);
        bieuDoTiLe.put("data", danhSachTiLeCanHo);

        return bieuDoTiLe;
    }
}
