package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.ChiSoHangThangRepository;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.PhuongTienRepository;
import com.sync.itk65.repository.DatDichVuRepository;
import com.sync.itk65.repository.ThanhToanRepository;
import com.sync.itk65.repository.HopDongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

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

    @Autowired
    private ThanhToanRepository thanhToanRepository;

    @Autowired
    private HopDongRepository hopDongRepository;

    // Tìm kiếm hóa đơn theo nhiều điều kiện
    public List<HoaDon> timKiemHoaDon(String maCanHo, String trangThai, Integer thang, Integer nam) {
        return hoaDonRepository.searchWithFilters(maCanHo, trangThai, thang, nam);
    }

    public Page<HoaDon> layTatCaHoaDon(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonRepository.findAll(pageable);
    }

    // Lấy hóa đơn theo ID
    public HoaDon layHoaDonById(Long id) {
        return hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn có ID: " + id));
    }
    // Lấy danh sách toàn bộ hóa đơn
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }
    // Cập nhật hóa đơn
    public void capNhatHoaDon(HoaDon hoaDon) {
        // Nếu tổng tiền là null hoặc 0, tính lại tổng
        if (hoaDon.getTongTien() == null || hoaDon.getTongTien() == 0) {
            taoHoaDonTuDong(hoaDon);
        } else {
            // Chỉ cần cập nhật thông tin cơ bản
            hoaDonRepository.save(hoaDon);
        }
    }

    public void taoHoaDonTuDong(HoaDon hoaDon) {
        Long canHoId = hoaDon.getCanHo().getId();
        int thang = hoaDon.getNgayPhatHanh().getMonthValue();
        int nam = hoaDon.getNgayPhatHanh().getYear();

        // VALIDATION: Kiểm tra xem đã có hóa đơn cho căn hộ trong tháng đó chưa (tránh tạo trùng)
        Long soLuongHoaDonTrung = hoaDonRepository.countByCanHoAndThangNam(canHoId, thang, nam);
        if (soLuongHoaDonTrung > 0) {
            throw new RuntimeException("Không thể tạo hóa đơn: Căn hộ này đã có hóa đơn cho tháng " + thang + "/" + nam + ". Vui lòng kiểm tra lại.");
        }

        // VALIDATION: Kiểm tra xem đã có chỉ số điện nước cho tháng đó chưa
        ChiSoHangThang chiSo = chiSoHangThangRepository.findByCanHoAndThangNam(canHoId, thang, nam)
                .orElseThrow(
                        () -> new RuntimeException("Không thể tạo hóa đơn: Căn hộ chưa được ghi nhận chỉ số điện nước cho tháng " + thang + "/" + nam + ". Vui lòng ghi nhận chỉ số trước khi tạo hóa đơn."));

        // VALIDATION: Kiểm tra chỉ số có hợp lệ không
        if (chiSo.getDienTieuThu() == null || chiSo.getDienTieuThu() < 0) {
            throw new RuntimeException("Chỉ số điện tiêu thụ không hợp lệ. Vui lòng kiểm tra lại.");
        }
        if (chiSo.getNuocTieuThu() == null || chiSo.getNuocTieuThu() < 0) {
            throw new RuntimeException("Chỉ số nước tiêu thụ không hợp lệ. Vui lòng kiểm tra lại.");
        }

        Double tongTien = 0.0;

        // BƯỚC 2: Tính tiền Điện và Nước
        Double tienDien = tinhTienDienTheoBacThang(chiSo.getDienTieuThu());
        Double tienNuoc = (chiSo.getNuocTieuThu() != null ? chiSo.getNuocTieuThu() : 0.0) * 18000.0;

        tongTien += tienDien + tienNuoc;

        // BƯỚC 3: Tính Phí quản lý
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Căn hộ"));
        Double donGiaQuanLy = 12000.0;
        Double phiQuanLy = (canHo.getDienTich() != null ? canHo.getDienTich() : 0.0) * donGiaQuanLy;

        tongTien += phiQuanLy;

        // BƯỚC 4: Tính phí gửi xe
        List<PhuongTien> danhSachXe = phuongTienRepository.findByCanHoId(canHoId);
        for (PhuongTien xe : danhSachXe) {
            if (!"Đã duyệt".equalsIgnoreCase(xe.getTrangThai())) continue;
            tongTien += tinhPhiGuiXe(xe.getLoaiXe());
        }

        // BƯỚC 5: Tính phí Dịch vụ phát sinh
        List<DatDichVu> dichVuDaDung = datDichVuRepository.findDichVuCuaCanHoTrongThang(canHoId, thang, nam);
        for (DatDichVu dv : dichVuDaDung) {
            if ("Đã duyệt".equalsIgnoreCase(dv.getTrangThai()) || "Đã hoàn thành".equalsIgnoreCase(dv.getTrangThai())) {
                if (dv.getDichVu() != null && dv.getDichVu().getDonGia() != null) {
                    tongTien += dv.getDichVu().getDonGia();
                }
            }
        }

        // BƯỚC 6: Tính tiền thuê chung cư
        com.sync.itk65.entity.HopDong hopDong = hopDongRepository.findActiveByCanHoId(canHoId).orElse(null);
        Double tienThueChungCu = 0.0;
        if (hopDong != null && hopDong.getTienThue() != null && hopDong.getNgayBatDau() != null) {
            int ngayTrongThang = hoaDon.getNgayPhatHanh().lengthOfMonth();
            int ngayDonVao = hopDong.getNgayBatDau().getDayOfMonth();
            tienThueChungCu = (hopDong.getTienThue() / ngayTrongThang) * (ngayTrongThang - ngayDonVao + 1);
        }
        tongTien += tienThueChungCu;

        // BƯỚC 7: Lưu hóa đơn vào Database
        hoaDon.setTongTien(tongTien);
        hoaDon.setTrangThaiThanhToan("Chưa đóng");

        if (hoaDon.getNgayDenHan() == null) {
            hoaDon.setNgayDenHan(hoaDon.getNgayPhatHanh().plusDays(10));
        }

        hoaDonRepository.save(hoaDon);
    }

    /**
     * ✅ TÍNH CHI TIẾT HÓA ĐƠN
     */
    public Map<String, Object> tinhChiTietHoaDon(Long hoaDonId) {
        Map<String, Object> chiTiet = new LinkedHashMap<>();

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        Long canHoId = hoaDon.getCanHo().getId();
        int thang = hoaDon.getNgayPhatHanh().getMonthValue();
        int nam = hoaDon.getNgayPhatHanh().getYear();

        // ========== 1. TÍNH TIỀN ĐIỆN ==========
        ChiSoHangThang chiSo = chiSoHangThangRepository.findByCanHoAndThangNam(canHoId, thang, nam)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chỉ số điện nước"));

        Double dienTieuThu = chiSo.getDienTieuThu() != null ? chiSo.getDienTieuThu() : 0.0;
        Double tienDien = tinhTienDienTheoBacThang(dienTieuThu);

        Map<String, Object> dien = new LinkedHashMap<>();
        dien.put("soKwh", dienTieuThu);
        dien.put("tienTruocThue", Math.round(tienDien / 1.08 * 100.0) / 100.0);
        dien.put("thueVat", Math.round((tienDien - (tienDien / 1.08)) * 100.0) / 100.0);
        dien.put("tongTienDien", Math.round(tienDien * 100.0) / 100.0);
        chiTiet.put("dien", dien);

        // ========== 2. TÍNH TIỀN NƯỚC ==========
        Double nuocTieuThu = chiSo.getNuocTieuThu() != null ? chiSo.getNuocTieuThu() : 0.0;
        Double donGiaNuoc = 18000.0;
        Double tienNuoc = nuocTieuThu * donGiaNuoc;

        Map<String, Object> nuoc = new LinkedHashMap<>();
        nuoc.put("soKhoiNuoc", nuocTieuThu);
        nuoc.put("donGia", donGiaNuoc);
        nuoc.put("tongTienNuoc", Math.round(tienNuoc * 100.0) / 100.0);
        chiTiet.put("nuoc", nuoc);

        // ========== 3. TÍNH PHÍ QUẢN LÝ ==========
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ"));

        Double dienTich = canHo.getDienTich() != null ? canHo.getDienTich() : 0.0;
        Double donGiaQuanLy = 12000.0;
        Double phiQuanLy = dienTich * donGiaQuanLy;

        Map<String, Object> quanLy = new LinkedHashMap<>();
        quanLy.put("dienTich", dienTich);
        quanLy.put("donGia", donGiaQuanLy);
        quanLy.put("tongPhiQuanLy", Math.round(phiQuanLy * 100.0) / 100.0);
        chiTiet.put("quanLy", quanLy);

        // ========== 4. TÍNH PHÍ GỬI XE ==========
        List<PhuongTien> danhSachXe = phuongTienRepository.findByCanHoId(canHoId);
        List<Map<String, Object>> danhSachPhiXe = new ArrayList<>();
        Double tongPhiXe = 0.0;

        for (PhuongTien xe : danhSachXe) {
            if (!"Đã duyệt".equalsIgnoreCase(xe.getTrangThai())) continue;
            Map<String, Object> xe_info = new LinkedHashMap<>();
            xe_info.put("bienSo", xe.getBienSoXe());
            xe_info.put("loaiXe", xe.getLoaiXe());

            Double phiXe = tinhPhiGuiXe(xe.getLoaiXe());
            String tenXe = "";
            if (laOto(xe.getLoaiXe())) {
                tenXe = "Ô tô";
                xe_info.put("donGia", 1200000.0);
                xe_info.put("moTa", "Phí gửi ô tô");
            } else if (laXeMay(xe.getLoaiXe())) {
                tenXe = "Xe máy";
                xe_info.put("donGia", 150000.0);
                xe_info.put("moTa", "Phí gửi xe máy");
            } else {
                tenXe = "Xe đạp";
                xe_info.put("donGia", 0.0);
                xe_info.put("moTa", "Miễn phí gửi xe đạp");
            }

            xe_info.put("tenXe", tenXe);
            xe_info.put("tienXe", Math.round(phiXe * 100.0) / 100.0);
            danhSachPhiXe.add(xe_info);
            tongPhiXe += phiXe;
        }

        Map<String, Object> xe = new LinkedHashMap<>();
        xe.put("danhSachXe", danhSachPhiXe);
        xe.put("tongPhiXe", Math.round(tongPhiXe * 100.0) / 100.0);
        xe.put("soLuongXe", danhSachPhiXe.size());
        chiTiet.put("xe", xe);

        // ========== 5. TÍNH PHÍ DỊCH VỤ PHÁT SINH ==========
        List<DatDichVu> dichVuDaDung = datDichVuRepository.findDichVuCuaCanHoTrongThang(canHoId, thang, nam);
        List<Map<String, Object>> danhSachDichVuChiTiet = new ArrayList<>();
        Double tongPhiDichVu = 0.0;

        for (DatDichVu dv : dichVuDaDung) {
            if ("Đã duyệt".equalsIgnoreCase(dv.getTrangThai()) || "Đã hoàn thành".equalsIgnoreCase(dv.getTrangThai())) {
                if (dv.getDichVu() != null && dv.getDichVu().getDonGia() != null) {
                    Map<String, Object> dv_info = new LinkedHashMap<>();
                    dv_info.put("tenDichVu", dv.getDichVu().getTen());
                    dv_info.put("loai", dv.getDichVu().getLoai());
                    dv_info.put("donGia", dv.getDichVu().getDonGia());
                    dv_info.put("donViTinh", dv.getDichVu().getDonViTinh());
                    dv_info.put("trangThai", dv.getTrangThai());

                    danhSachDichVuChiTiet.add(dv_info);
                    tongPhiDichVu += dv.getDichVu().getDonGia();
                }
            }
        }

        Map<String, Object> dichVu = new LinkedHashMap<>();
        dichVu.put("danhSachDichVu", danhSachDichVuChiTiet);
        dichVu.put("tongPhiDichVu", Math.round(tongPhiDichVu * 100.0) / 100.0);
        chiTiet.put("dichVu", dichVu);

        // ========== 6. TÍNH TIỀN THUÊ CHUNG CƯ ==========
        com.sync.itk65.entity.HopDong hopDong = hopDongRepository.findActiveByCanHoId(canHoId).orElse(null);
        Double tienThueChungCu = 0.0;
        int ngayDonVao = 0;
        if (hopDong != null && hopDong.getTienThue() != null && hopDong.getNgayBatDau() != null) {
            int ngayTrongThang = hoaDon.getNgayPhatHanh().lengthOfMonth();
            ngayDonVao = hopDong.getNgayBatDau().getDayOfMonth();
            tienThueChungCu = (hopDong.getTienThue() / ngayTrongThang) * (ngayTrongThang - ngayDonVao + 1);
        }

        Map<String, Object> thueChungCu = new LinkedHashMap<>();
        thueChungCu.put("tienThue", hopDong != null ? hopDong.getTienThue() : 0.0);
        thueChungCu.put("ngayTrongThang", hoaDon.getNgayPhatHanh().lengthOfMonth());
        thueChungCu.put("ngayDonVao", ngayDonVao);
        thueChungCu.put("tienThueChungCu", Math.round(tienThueChungCu * 100.0) / 100.0);
        chiTiet.put("thueChungCu", thueChungCu);

        // ========== 7. TỔNG CỘNG TẤT CẢ ==========
        Double tongCong = tienDien + tienNuoc + phiQuanLy + tongPhiXe + tongPhiDichVu + tienThueChungCu;

        Map<String, Object> tongHop = new LinkedHashMap<>();
        tongHop.put("tienDien", Math.round(tienDien * 100.0) / 100.0);
        tongHop.put("tienNuoc", Math.round(tienNuoc * 100.0) / 100.0);
        tongHop.put("phiQuanLy", Math.round(phiQuanLy * 100.0) / 100.0);
        tongHop.put("phiXe", Math.round(tongPhiXe * 100.0) / 100.0);
        tongHop.put("phiDichVu", Math.round(tongPhiDichVu * 100.0) / 100.0);
        tongHop.put("tienThueChungCu", Math.round(tienThueChungCu * 100.0) / 100.0);
        tongHop.put("tongCong", Math.round(tongCong * 100.0) / 100.0);
        chiTiet.put("tongHop", tongHop);

        // ========== 8. THÔNG TIN HÓA ĐƠN ==========
        Map<String, Object> thongTinHD = new LinkedHashMap<>();
        thongTinHD.put("id", hoaDon.getId());
        thongTinHD.put("maCanHo", canHo.getMaCanHo());
        thongTinHD.put("ngayPhatHanh", hoaDon.getNgayPhatHanh());
        thongTinHD.put("ngayDenHan", hoaDon.getNgayDenHan());
        thongTinHD.put("trangThai", hoaDon.getTrangThaiThanhToan());
        chiTiet.put("thongTinHoaDon", thongTinHD);

        return chiTiet;
    }

    /**
     * ✅ THÊM CHI TIẾT HÓA ĐƠN VÀO MODEL
     */
    public void themChiTietHoaDonVaoModel(Long hoaDonId, Model model) {
        try {
            Map<String, Object> chiTiet = tinhChiTietHoaDon(hoaDonId);

            model.addAttribute("chiTietDien", chiTiet.get("dien"));
            model.addAttribute("chiTietNuoc", chiTiet.get("nuoc"));
            model.addAttribute("chiTietQuanLy", chiTiet.get("quanLy"));
            model.addAttribute("chiTietXe", chiTiet.get("xe"));
            model.addAttribute("chiTietDichVu", chiTiet.get("dichVu"));
            model.addAttribute("chiTietThueChungCu", chiTiet.get("thueChungCu"));
            model.addAttribute("tongHop", chiTiet.get("tongHop"));
            model.addAttribute("thongTinHoaDon", chiTiet.get("thongTinHoaDon"));
            model.addAttribute("chiTietDayDu", chiTiet);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi tính toán chi tiết hóa đơn: " + e.getMessage());
        }
    }

    // Đánh dấu đã thanh toán
    public void danhDauDaThanhToan(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElse(null);
        if (hoaDon != null) {
            hoaDon.setTrangThaiThanhToan("Đã đóng");
            hoaDonRepository.save(hoaDon);
        }
    }

    // Xóa hóa đơn và các bản ghi thanh toán liên quan
    public void xoaHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn có ID: " + id));

        // Xóa các bản ghi thanh toán liên quan đến hóa đơn này
        List<com.sync.itk65.entity.ThanhToan> danhSachThanhToan = thanhToanRepository.findByHoaDonId(id);
        thanhToanRepository.deleteAll(danhSachThanhToan);

        // Xóa hóa đơn
        hoaDonRepository.delete(hoaDon);
    }

    /**
     * Tính tiền điện theo bậc thang EVN (bao gồm VAT 8%)
     */
    private Double tinhTienDienTheoBacThang(Double soDien) {
        if (soDien == null || soDien <= 0)
            return 0.0;
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

        return Math.round(tienDien * 1.08 * 100.0) / 100.0;
    }

    private Double tinhPhiGuiXe(String loaiXe) {
        if (laOto(loaiXe)) return 1200000.0;
        if (laXeMay(loaiXe)) return 150000.0;
        return 0.0;
    }

    private boolean laOto(String loaiXe) {
        return "Oto".equalsIgnoreCase(loaiXe) || "Ô tô".equalsIgnoreCase(loaiXe);
    }

    private boolean laXeMay(String loaiXe) {
        return "XeMay".equalsIgnoreCase(loaiXe) || "Xe máy".equalsIgnoreCase(loaiXe);
    }
}
