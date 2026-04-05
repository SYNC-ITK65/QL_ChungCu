package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.entity.DatDichVu; // Import Entity DatDichVu
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.ChiSoHangThangRepository;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.PhuongTienRepository;
import com.sync.itk65.repository.DatDichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiSoHangThangRepository chiSoHangThangRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private PhuongTienRepository phuongTienRepository;

    @Autowired
    private DatDichVuRepository datDichVuRepository;

    // Lấy danh sách toàn bộ hóa đơn
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }

    public void taoHoaDonTuDong(HoaDon hoaDon) {
        Long canHoId = hoaDon.getCanHo().getId();
        int thang = hoaDon.getNgayPhatHanh().getMonthValue();
        int nam = hoaDon.getNgayPhatHanh().getYear();

        Double tongTien = 0.0;

        // BƯỚC 1: Lấy chỉ số điện nước của tháng đó
        ChiSoHangThang chiSo = chiSoHangThangRepository.findByCanHoAndThangNam(canHoId, thang, nam)
                .orElseThrow(() -> new RuntimeException("Chưa ghi nhận chỉ số điện nước cho tháng " + thang + "/" + nam));

        // BƯỚC 2: Tính tiền Điện và Nước
        Double tienDien = tinhTienDienTheoBacThang(chiSo.getDienTieuThu());
        Double tienNuoc = (chiSo.getNuocTieuThu() != null ? chiSo.getNuocTieuThu() : 0.0) * 18000.0; // 18k/khối nước

        tongTien += tienDien + tienNuoc;

        // BƯỚC 3: Tính Phí quản lý (Dựa vào diện tích căn hộ)
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Căn hộ"));
        Double donGiaQuanLy = 12000.0; // 12.000đ/m2
        Double phiQuanLy = (canHo.getDienTich() != null ? canHo.getDienTich() : 0.0) * donGiaQuanLy;

        tongTien += phiQuanLy;

        // BƯỚC 4: Tính phí gửi xe (Lấy từ bảng Phương Tiện)
        List<PhuongTien> danhSachXe = phuongTienRepository.findByCanHoId(canHoId);
        for (PhuongTien xe : danhSachXe) {
            if ("Oto".equalsIgnoreCase(xe.getLoaiXe())) {
                tongTien += 1200000.0; // 1.2 triệu/tháng
            } else if ("XeMay".equalsIgnoreCase(xe.getLoaiXe())) {
                tongTien += 150000.0;  // 150k/tháng
            }
        }

        // BƯỚC 5: Tính phí Dịch vụ phát sinh (Hồ bơi, BBQ...)
        List<DatDichVu> dichVuDaDung = datDichVuRepository.findDichVuCuaCanHoTrongThang(canHoId, thang, nam);
        for (DatDichVu dv : dichVuDaDung) {
            if(dv.getDichVu() != null && dv.getDichVu().getDonGia() != null) {
                tongTien += dv.getDichVu().getDonGia();
            }
        }

        // BƯỚC 6: Lưu hóa đơn vào Database
        hoaDon.setTongTien(tongTien);
        hoaDon.setTrangThaiThanhToan("Chưa đóng");

        // Mặc định ngày đến hạn là 10 ngày sau ngày phát hành
        if (hoaDon.getNgayDenHan() == null) {
            hoaDon.setNgayDenHan(hoaDon.getNgayPhatHanh().plusDays(10));
        }

        hoaDonRepository.save(hoaDon);
    }

    // Đánh dấu đã thanh toán
    public void danhDauDaThanhToan(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElse(null);
        if (hoaDon != null) {
            hoaDon.setTrangThaiThanhToan("Đã đóng");
            hoaDonRepository.save(hoaDon);
        }
    }
    private Double tinhTienDienTheoBacThang(Double soDien) {
        if (soDien == null || soDien <= 0) return 0.0;
        double tienDien = 0;

        if (soDien <= 50) {
            tienDien = soDien * 1806;
        } else if (soDien <= 100) {
            tienDien = 50 * 1806 + (soDien - 50) * 1866;
        } else if (soDien <= 200) {
            tienDien = 50 * 1806 + 50 * 1866 + (soDien - 100) * 2167;
        } else if (soDien <= 300) {
            tienDien = 50 * 1806 + 50 * 1866 + 100 * 2167 + (soDien - 200) * 2729;
        } else if (soDien <= 400) {
            tienDien = 50 * 1806 + 50 * 1866 + 100 * 2167 + 100 * 2729 + (soDien - 300) * 3050;
        } else {
            tienDien = 50 * 1806 + 50 * 1866 + 100 * 2167 + 100 * 2729 + 100 * 3050 + (soDien - 400) * 3151;
        }

        return tienDien * 1.08; // Cộng thêm 8% Thuế GTGT (VAT)
    }
}