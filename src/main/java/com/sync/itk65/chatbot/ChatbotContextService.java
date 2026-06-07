package com.sync.itk65.chatbot;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.entity.ThongBao;
import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.entity.KienHang;
import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.entity.DangKyKhachTham;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.HoaDonRepository;
import com.sync.itk65.repository.ThongBaoRepository;
import com.sync.itk65.repository.PhuongTienRepository;
import com.sync.itk65.repository.KienHangRepository;
import com.sync.itk65.repository.PhanAnhRepository;
import com.sync.itk65.repository.DatDichVuRepository;
import com.sync.itk65.repository.DangKyKhachThamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service truy vấn CSDL để xây dựng ngữ cảnh (context) thực tế
 * trước khi đưa vào prompt cho AI (cơ chế RAG đơn giản / Context-Injection).
 */
@Service
public class ChatbotContextService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private PhuongTienRepository phuongTienRepository;

    @Autowired
    private KienHangRepository kienHangRepository;

    @Autowired
    private PhanAnhRepository phanAnhRepository;

    @Autowired
    private DatDichVuRepository datDichVuRepository;

    @Autowired
    private DangKyKhachThamRepository dangKyKhachThamRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =========================================================
    //  CONTEXT CHO CƯ DÂN
    // =========================================================

    /**
     * Xây dựng context dữ liệu dành cho cư dân.
     */
    public String buildCuDanContext(Long canHoId, String maCanHo, Integer tang, Long cuDanId) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DỮ LIỆU THỰC TẾ CỦA CĂN HỘ ").append(maCanHo).append(" ===\n\n");

        // --- Hóa đơn chưa đóng ---
        List<HoaDon> chuaDong = hoaDonRepository.findUnpaidByCanHoId(canHoId);
        sb.append("[ HÓA ĐƠN CHƯA ĐÓNG ]\n");
        if (chuaDong.isEmpty()) {
            sb.append("  → Không có hóa đơn chưa đóng.\n");
        } else {
            for (HoaDon hd : chuaDong) {
                sb.append(String.format("  - ID: %d | Phát hành: %s | Đến hạn: %s | Tổng tiền: %,.0f VNĐ | Trạng thái: %s\n",
                        hd.getId(),
                        hd.getNgayPhatHanh() != null ? hd.getNgayPhatHanh().format(DATE_FMT) : "N/A",
                        hd.getNgayDenHan() != null ? hd.getNgayDenHan().format(DATE_FMT) : "N/A",
                        hd.getTongTien() != null ? hd.getTongTien() : 0.0,
                        hd.getTrangThaiThanhToan()));
            }
        }

        // --- Hóa đơn đã đóng gần nhất (top 5) ---
        List<HoaDon> daDong = hoaDonRepository.findPaidByCanHoId(canHoId);
        sb.append("\n[ HÓA ĐƠN ĐÃ ĐÓNG GẦN NHẤT ]\n");
        if (daDong.isEmpty()) {
            sb.append("  → Chưa có hóa đơn đã đóng.\n");
        } else {
            int limit = Math.min(daDong.size(), 5);
            for (int i = 0; i < limit; i++) {
                HoaDon hd = daDong.get(i);
                sb.append(String.format("  - ID: %d | Phát hành: %s | Tổng tiền: %,.0f VNĐ | Trạng thái: %s\n",
                        hd.getId(),
                        hd.getNgayPhatHanh() != null ? hd.getNgayPhatHanh().format(DATE_FMT) : "N/A",
                        hd.getTongTien() != null ? hd.getTongTien() : 0.0,
                        hd.getTrangThaiThanhToan()));
            }
        }

        // --- Phương tiện đăng ký của căn hộ ---
        List<PhuongTien> phuongTiens = phuongTienRepository.findByCanHoId(canHoId);
        sb.append("\n[ PHƯƠNG TIỆN ĐĂNG KÝ CỦA CĂN HỘ ]\n");
        if (phuongTiens == null || phuongTiens.isEmpty()) {
            sb.append("  → Không có phương tiện đăng ký.\n");
        } else {
            for (PhuongTien pt : phuongTiens) {
                sb.append(String.format("  - Biển số: %s | Loại xe: %s | Màu sắc: %s | Trạng thái: %s\n",
                        pt.getBienSoXe(), pt.getLoaiXe(), pt.getMauXe(), pt.getTrangThai()));
            }
        }

        // --- Kiện hàng ---
        List<KienHang> kienHangs = kienHangRepository.findByCanHoId(canHoId, PageRequest.of(0, 10)).getContent();
        sb.append("\n[ KIỆN HÀNG / BƯU PHẨM CỦA CĂN HỘ ]\n");
        if (kienHangs.isEmpty()) {
            sb.append("  → Không có kiện hàng nào gần đây.\n");
        } else {
            for (KienHang kh : kienHangs) {
                sb.append(String.format("  - Người gửi: %s | Người nhận: %s | Ngày nhận: %s | Trạng thái: %s\n",
                        kh.getNguoiGui(), kh.getNguoiNhan(),
                        kh.getNgayNhan() != null ? kh.getNgayNhan().format(DATE_FMT) : "N/A",
                        kh.getTrangThai()));
            }
        }

        // --- Phản ánh ---
        List<PhanAnh> phanAnhs = phanAnhRepository.findByCanHoIdOrderByNgayGuiDesc(canHoId);
        sb.append("\n[ PHẢN ÁNH CỦA CĂN HỘ ]\n");
        if (phanAnhs == null || phanAnhs.isEmpty()) {
            sb.append("  → Không có phản ánh nào.\n");
        } else {
            int limit = Math.min(phanAnhs.size(), 5);
            for (int i = 0; i < limit; i++) {
                PhanAnh pa = phanAnhs.get(i);
                sb.append(String.format("  - Tiêu đề: %s | Ngày gửi: %s | Trạng thái: %s\n",
                        pa.getTieuDe(),
                        pa.getNgayGui() != null ? pa.getNgayGui().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        pa.getTrangThai()));
                if (pa.getPhanHoi() != null && !pa.getPhanHoi().isBlank()) {
                    sb.append(String.format("    → Phản hồi từ BQL: %s\n", pa.getPhanHoi()));
                }
            }
        }

        // --- Đăng ký dịch vụ ---
        if (cuDanId != null) {
            List<DatDichVu> datDichVus = datDichVuRepository.layLichSuDatDichVu(cuDanId);
            sb.append("\n[ ĐĂNG KÝ DỊCH VỤ CỦA BẠN ]\n");
            if (datDichVus == null || datDichVus.isEmpty()) {
                sb.append("  → Bạn chưa đăng ký dịch vụ nào.\n");
            } else {
                int limit = Math.min(datDichVus.size(), 5);
                for (int i = 0; i < limit; i++) {
                    DatDichVu ddv = datDichVus.get(i);
                    String tenDv = ddv.getDichVu() != null ? ddv.getDichVu().getTen() : "N/A";
                    sb.append(String.format("  - Dịch vụ: %s | Ngày đặt: %s | Trạng thái: %s | Ghi chú: %s\n",
                            tenDv,
                            ddv.getNgayDat() != null ? ddv.getNgayDat().format(DATE_FMT) : "N/A",
                            ddv.getTrangThai(),
                            ddv.getGhiChu() != null ? ddv.getGhiChu() : ""));
                }
            }
        }

        // --- Đăng ký khách thăm ---
        if (cuDanId != null) {
            List<DangKyKhachTham> khachThams = dangKyKhachThamRepository.findByCuDanIdOrderByThoiGianDuKienDesc(cuDanId, PageRequest.of(0, 5)).getContent();
            sb.append("\n[ ĐĂNG KÝ KHÁCH THĂM CỦA BẠN ]\n");
            if (khachThams.isEmpty()) {
                sb.append("  → Bạn chưa đăng ký khách thăm nào gần đây.\n");
            } else {
                for (DangKyKhachTham kt : khachThams) {
                    sb.append(String.format("  - Tên khách: %s | Biển số xe: %s | Thời gian dự kiến: %s | Trạng thái: %s\n",
                            kt.getTenKhach(),
                            kt.getBienSoXe() != null ? kt.getBienSoXe() : "Không có",
                            kt.getThoiGianDuKien() != null ? kt.getThoiGianDuKien().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                            kt.getTrangThai()));
                }
            }
        }

        // --- Thông báo bảng tin chung (loại 1) ---
        List<ThongBao> thongBaoBangTin = thongBaoRepository
                .locThongBaoTheoCuDan(1, maCanHo, String.valueOf(tang), PageRequest.of(0, 5))
                .getContent();
        sb.append("\n[ THÔNG BÁO BẢNG TIN GẦN NHẤT (tối đa 5) ]\n");
        if (thongBaoBangTin.isEmpty()) {
            sb.append("  → Không có thông báo bảng tin nào.\n");
        } else {
            for (ThongBao tb : thongBaoBangTin) {
                sb.append(String.format("  - [%s] %s: %s\n",
                        tb.getNgayDang() != null ? tb.getNgayDang().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        tb.getTieuDe(),
                        truncate(tb.getNoiDung(), 120)));
            }
        }

        // --- Cẩm nang chung cư (loại 2) ---
        List<ThongBao> camNang = thongBaoRepository
                .locThongBaoTheoCuDan(2, maCanHo, String.valueOf(tang), PageRequest.of(0, 5))
                .getContent();
        sb.append("\n[ CẨM NANG CƯ DÂN GẦN NHẤT (tối đa 5) ]\n");
        if (camNang.isEmpty()) {
            sb.append("  → Không có cẩm nang nào.\n");
        } else {
            for (ThongBao tb : camNang) {
                sb.append(String.format("  - [%s] %s: %s\n",
                        tb.getNgayDang() != null ? tb.getNgayDang().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        tb.getTieuDe(),
                        truncate(tb.getNoiDung(), 120)));
            }
        }

        return sb.toString();
    }

    // =========================================================
    //  CONTEXT CHO ADMIN / BAN QUẢN TRỊ
    // =========================================================

    /**
     * Xây dựng context dữ liệu tổng hợp dành cho Admin/Ban Quản lý.
     */
    public String buildAdminContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DỮ LIỆU THỐNG KÊ TOÀN HỆ THỐNG CHUNG CƯ ===\n\n");

        // --- Thống kê căn hộ ---
        long tongCanHo = canHoRepository.countTotalCanHo();
        long canHoTrong = canHoRepository.countVacantCanHo();
        long canHoCoDan = tongCanHo - canHoTrong;
        double tyLeTrong = tongCanHo > 0 ? (canHoTrong * 100.0 / tongCanHo) : 0;

        sb.append("[ THỐNG KÊ CĂN HỘ ]\n");
        sb.append(String.format("  - Tổng số căn hộ: %d\n", tongCanHo));
        sb.append(String.format("  - Đã có cư dân: %d căn (%.1f%%)\n", canHoCoDan, (100.0 - tyLeTrong)));
        sb.append(String.format("  - Đang trống: %d căn (%.1f%%)\n", canHoTrong, tyLeTrong));

        // --- Thống kê hóa đơn ---
        Long soHoaDonChuaDong = hoaDonRepository.countByStatus("Chưa đóng");
        Long soHoaDonDaDong = hoaDonRepository.countByStatus("Đã đóng");

        sb.append("\n[ THỐNG KÊ HÓA ĐƠN ]\n");
        sb.append(String.format("  - Tổng hóa đơn chưa đóng: %d\n", soHoaDonChuaDong != null ? soHoaDonChuaDong : 0));
        sb.append(String.format("  - Tổng hóa đơn đã đóng: %d\n", soHoaDonDaDong != null ? soHoaDonDaDong : 0));

        // --- Hóa đơn quá hạn (top 10) ---
        List<HoaDon> quaHan = hoaDonRepository.findOverdueInvoices();
        sb.append("\n[ HÓA ĐƠN QUÁ HẠN CHƯA ĐÓNG ]\n");
        if (quaHan.isEmpty()) {
            sb.append("  → Không có hóa đơn quá hạn.\n");
        } else {
            sb.append(String.format("  Tổng số: %d hóa đơn quá hạn\n", quaHan.size()));
            int limit = Math.min(quaHan.size(), 10);
            for (int i = 0; i < limit; i++) {
                HoaDon hd = quaHan.get(i);
                String maCanHo = hd.getCanHo() != null ? hd.getCanHo().getMaCanHo() : "N/A";
                sb.append(String.format("  - Căn hộ: %s | Đến hạn: %s | Tổng tiền: %,.0f VNĐ\n",
                        maCanHo,
                        hd.getNgayDenHan() != null ? hd.getNgayDenHan().format(DATE_FMT) : "N/A",
                        hd.getTongTien() != null ? hd.getTongTien() : 0.0));
            }
        }

        // --- Doanh thu 6 tháng gần nhất ---
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        List<Object[]> doanhThu = hoaDonRepository.getRevenueLast6Months(sixMonthsAgo);
        sb.append("\n[ DOANH THU 6 THÁNG GẦN NHẤT (chỉ hóa đơn đã đóng) ]\n");
        if (doanhThu.isEmpty()) {
            sb.append("  → Chưa có dữ liệu doanh thu.\n");
        } else {
            double tongDoanhThu = 0;
            for (Object[] row : doanhThu) {
                Integer thang = ((Number) row[0]).intValue();
                Integer nam = ((Number) row[1]).intValue();
                Double tien = ((Number) row[2]).doubleValue();
                tongDoanhThu += tien;
                sb.append(String.format("  - Tháng %d/%d: %,.0f VNĐ\n", thang, nam, tien));
            }
            sb.append(String.format("  → Tổng 6 tháng: %,.0f VNĐ\n", tongDoanhThu));
        }

        // --- Doanh thu tháng hiện tại ---
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        Double doanhThuThang = hoaDonRepository.sumRevenueByPeriodAndStatus(startOfMonth, LocalDate.now(), "Đã đóng");
        sb.append(String.format("\n[ DOANH THU THÁNG %d/%d ]\n",
                LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        sb.append(String.format("  - Đã thu: %,.0f VNĐ\n", doanhThuThang != null ? doanhThuThang : 0.0));

        // --- Thống kê phương tiện ---
        long tongXe = phuongTienRepository.count();
        long xeChoDuyet = phuongTienRepository.countByTrangThai("Chờ duyệt");
        long xeDaDuyet = phuongTienRepository.countByTrangThai("Đã duyệt");
        sb.append("\n[ THỐNG KÊ PHƯƠNG TIỆN ]\n");
        sb.append(String.format("  - Tổng số phương tiện đăng ký: %d\n", tongXe));
        sb.append(String.format("  - Xe đã duyệt: %d | Xe chờ duyệt: %d\n", xeDaDuyet, xeChoDuyet));

        // --- Thống kê kiện hàng ---
        long kienHangChoNhan = kienHangRepository.findByTrangThai("Chờ nhận").size();
        sb.append("\n[ THỐNG KÊ KIỆN HÀNG ]\n");
        sb.append(String.format("  - Số kiện hàng đang chờ cư dân nhận: %d\n", kienHangChoNhan));

        // --- Thống kê phản ánh ---
        long phanAnhCho = phanAnhRepository.countChoXuLy();
        long phanAnhDang = phanAnhRepository.countDangXuLy();
        sb.append("\n[ THỐNG KÊ PHẢN ÁNH ]\n");
        sb.append(String.format("  - Phản ánh chờ xử lý: %d | Phản ánh đang xử lý: %d\n", phanAnhCho, phanAnhDang));

        // --- Đăng ký dịch vụ gần đây (top 5) ---
        Page<DatDichVu> dsDichVu = datDichVuRepository.findAllByOrderByNgayDatDesc(PageRequest.of(0, 5));
        sb.append("\n[ YÊU CẦU ĐẶT DỊCH VỤ MỚI NHẤT ]\n");
        if (dsDichVu.hasContent()) {
            for (DatDichVu ddv : dsDichVu.getContent()) {
                String hoTen = ddv.getCuDan() != null ? ddv.getCuDan().getHoTen() : "N/A";
                String maCanHo = (ddv.getCuDan() != null && ddv.getCuDan().getCanHo() != null) ? ddv.getCuDan().getCanHo().getMaCanHo() : "N/A";
                String tenDv = ddv.getDichVu() != null ? ddv.getDichVu().getTen() : "N/A";
                sb.append(String.format("  - Căn hộ: %s | Người đặt: %s | Dịch vụ: %s | Ngày đặt: %s | Trạng thái: %s\n",
                        maCanHo, hoTen, tenDv,
                        ddv.getNgayDat() != null ? ddv.getNgayDat().format(DATE_FMT) : "N/A",
                        ddv.getTrangThai()));
            }
        }

        // --- Đăng ký khách thăm mới nhất (top 5) ---
        Page<DangKyKhachTham> dsKhach = dangKyKhachThamRepository.findAllByOrderByThoiGianDuKienDesc(PageRequest.of(0, 5));
        sb.append("\n[ ĐĂNG KÝ KHÁCH THĂM MỚI NHẤT ]\n");
        if (dsKhach.hasContent()) {
            for (DangKyKhachTham kt : dsKhach.getContent()) {
                String hoTen = kt.getCuDan() != null ? kt.getCuDan().getHoTen() : "N/A";
                sb.append(String.format("  - Khách: %s | Đăng ký bởi cư dân: %s | Thời gian dự kiến: %s | Trạng thái: %s\n",
                        kt.getTenKhach(), hoTen,
                        kt.getThoiGianDuKien() != null ? kt.getThoiGianDuKien().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        kt.getTrangThai()));
            }
        }

        // --- Thông báo mới nhất (5 thông báo) ---
        List<ThongBao> thongBaoMoi = thongBaoRepository.findAllByOrderByNgayDangDesc()
                .stream().limit(5).toList();
        sb.append("\n[ THÔNG BÁO MỚI NHẤT (5 thông báo gần nhất) ]\n");
        if (thongBaoMoi.isEmpty()) {
            sb.append("  → Không có thông báo.\n");
        } else {
            for (ThongBao tb : thongBaoMoi) {
                String loaiStr = tb.getLoai() != null && tb.getLoai() == 2 ? "Cẩm nang" : "Bảng tin";
                sb.append(String.format("  - [%s][%s] %s: %s\n",
                        tb.getNgayDang() != null ? tb.getNgayDang().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        loaiStr,
                        tb.getTieuDe(),
                        truncate(tb.getNoiDung(), 100)));
            }
        }

        return sb.toString();
    }

    // =========================================================
    //  CONTEXT CHO LỄ TÂN
    // =========================================================

    /**
     * Xây dựng context dữ liệu công việc dành riêng cho Lễ tân.
     */
    public String buildLeTanContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DỮ LIỆU CÔNG VIỆC CỦA LỄ TÂN ===\n\n");

        // --- Kiện hàng chờ nhận ---
        List<KienHang> choNhan = kienHangRepository.findByTrangThai("Chờ nhận");
        sb.append("[ KIỆN HÀNG / BƯU PHẨM CHỜ CƯ DÂN NHẬN ]\n");
        sb.append(String.format("  Tổng số: %d kiện hàng đang chờ nhận\n", choNhan.size()));
        if (!choNhan.isEmpty()) {
            int limit = Math.min(choNhan.size(), 10);
            for (int i = 0; i < limit; i++) {
                KienHang kh = choNhan.get(i);
                String maCanHo = kh.getCanHo() != null ? kh.getCanHo().getMaCanHo() : "N/A";
                sb.append(String.format("  - Căn hộ: %s | Người nhận: %s | Người gửi: %s | Ngày nhận: %s\n",
                        maCanHo, kh.getNguoiNhan(), kh.getNguoiGui(),
                        kh.getNgayNhan() != null ? kh.getNgayNhan().format(DATE_FMT) : "N/A"));
            }
        }

        // --- Yêu cầu dịch vụ chờ duyệt ---
        Page<DatDichVu> choDuyetDv = datDichVuRepository.timKiemVaLocDonDatDichVu(null, "Chờ duyệt", PageRequest.of(0, 10));
        sb.append("\n[ ĐƠN ĐẶT DỊCH VỤ CHỜ DUYỆT ]\n");
        sb.append(String.format("  Tổng số chờ duyệt: %d đơn\n", choDuyetDv.getTotalElements()));
        if (choDuyetDv.hasContent()) {
            for (DatDichVu ddv : choDuyetDv.getContent()) {
                String hoTen = ddv.getCuDan() != null ? ddv.getCuDan().getHoTen() : "N/A";
                String maCanHo = (ddv.getCuDan() != null && ddv.getCuDan().getCanHo() != null) ? ddv.getCuDan().getCanHo().getMaCanHo() : "N/A";
                String tenDv = ddv.getDichVu() != null ? ddv.getDichVu().getTen() : "N/A";
                sb.append(String.format("  - Căn hộ: %s | Người đặt: %s | Dịch vụ: %s | Ngày đặt: %s | Ghi chú: %s\n",
                        maCanHo, hoTen, tenDv,
                        ddv.getNgayDat() != null ? ddv.getNgayDat().format(DATE_FMT) : "N/A",
                        ddv.getGhiChu() != null ? ddv.getGhiChu() : ""));
            }
        }

        // --- Phản ánh chờ xử lý ---
        Page<PhanAnh> phanAnhs = phanAnhRepository.timKiemVaLocPhanAnh(null, "Chờ xử lý", PageRequest.of(0, 10));
        sb.append("\n[ PHẢN ÁNH CỦA CƯ DÂN CHỜ XỬ LÝ ]\n");
        sb.append(String.format("  Tổng số chờ xử lý: %d phản ánh\n", phanAnhs.getTotalElements()));
        if (phanAnhs.hasContent()) {
            for (PhanAnh pa : phanAnhs.getContent()) {
                String maCanHo = pa.getCanHo() != null ? pa.getCanHo().getMaCanHo() : "N/A";
                sb.append(String.format("  - Căn hộ: %s | Tiêu đề: %s | Ngày gửi: %s | Nội dung: %s\n",
                        maCanHo, pa.getTieuDe(),
                        pa.getNgayGui() != null ? pa.getNgayGui().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        truncate(pa.getNoiDung(), 80)));
            }
        }

        // --- Đăng ký khách thăm gần đây ---
        Page<DangKyKhachTham> khachThams = dangKyKhachThamRepository.findAllByOrderByThoiGianDuKienDesc(PageRequest.of(0, 10));
        sb.append("\n[ ĐĂNG KÝ KHÁCH THĂM GẦN ĐÂY ]\n");
        if (khachThams.hasContent()) {
            for (DangKyKhachTham kt : khachThams.getContent()) {
                String hoTenCuDan = kt.getCuDan() != null ? kt.getCuDan().getHoTen() : "N/A";
                String maCanHo = (kt.getCuDan() != null && kt.getCuDan().getCanHo() != null) ? kt.getCuDan().getCanHo().getMaCanHo() : "N/A";
                sb.append(String.format("  - Căn hộ đăng ký: %s (Cư dân: %s) | Tên khách: %s | Biển số xe: %s | Thời gian: %s | Trạng thái: %s\n",
                        maCanHo, hoTenCuDan, kt.getTenKhach(),
                        kt.getBienSoXe() != null ? kt.getBienSoXe() : "Không có",
                        kt.getThoiGianDuKien() != null ? kt.getThoiGianDuKien().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        kt.getTrangThai()));
            }
        }

        return sb.toString();
    }

    // =========================================================
    //  CONTEXT CHO BẢO VỆ
    // =========================================================

    /**
     * Xây dựng context dữ liệu công việc dành riêng cho Bảo vệ.
     */
    public String buildBaoVeContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DỮ LIỆU CÔNG VIỆC CỦA BẢO VỆ ===\n\n");

        // --- Đăng ký khách thăm hôm nay / sắp tới ---
        Page<DangKyKhachTham> khachThams = dangKyKhachThamRepository.findAllByOrderByThoiGianDuKienDesc(PageRequest.of(0, 15));
        sb.append("[ DANH SÁCH ĐĂNG KÝ KHÁCH THĂM GẦN ĐÂY VÀ SẮP TỚI ]\n");
        if (khachThams.hasContent()) {
            for (DangKyKhachTham kt : khachThams.getContent()) {
                String hoTenCuDan = kt.getCuDan() != null ? kt.getCuDan().getHoTen() : "N/A";
                String maCanHo = (kt.getCuDan() != null && kt.getCuDan().getCanHo() != null) ? kt.getCuDan().getCanHo().getMaCanHo() : "N/A";
                sb.append(String.format("  - Khách: %s | CMND: %s | Biển xe: %s | Căn hộ vào: %s (Cư dân: %s) | Dự kiến: %s | Trạng thái: %s\n",
                        kt.getTenKhach(),
                        kt.getCmnd() != null ? kt.getCmnd() : "N/A",
                        kt.getBienSoXe() != null ? kt.getBienSoXe() : "Không có",
                        maCanHo, hoTenCuDan,
                        kt.getThoiGianDuKien() != null ? kt.getThoiGianDuKien().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                        kt.getTrangThai()));
            }
        } else {
            sb.append("  → Không có danh sách đăng ký khách thăm.\n");
        }

        // --- Danh sách phương tiện đăng ký của chung cư để tra cứu biển số ---
        List<PhuongTien> phuongTiens = phuongTienRepository.findAll();
        sb.append("\n[ DANH SÁCH PHƯƠNG TIỆN ĐĂNG KÝ TRONG HỆ THỐNG (DÙNG ĐỂ TRA CỨU BIỂN SỐ XE) ]\n");
        if (phuongTiens != null && !phuongTiens.isEmpty()) {
            sb.append(String.format("  Tổng số phương tiện đăng ký: %d\n", phuongTiens.size()));
            for (PhuongTien pt : phuongTiens) {
                String maCanHo = pt.getCanHo() != null ? pt.getCanHo().getMaCanHo() : "N/A";
                String chuHo = "N/A";
                if (pt.getCanHo() != null && pt.getCanHo().getDanhSachCuDan() != null) {
                    for (CuDan cd : pt.getCanHo().getDanhSachCuDan()) {
                        if ("Chủ hộ".equalsIgnoreCase(cd.getMoiQuanHe()) || "Chủ Hộ".equalsIgnoreCase(cd.getMoiQuanHe())) {
                            chuHo = cd.getHoTen();
                            break;
                        }
                    }
                }
                sb.append(String.format("  - Biển số: %s | Căn hộ: %s (Chủ hộ: %s) | Loại xe: %s | Màu: %s | Trạng thái: %s\n",
                        pt.getBienSoXe(), maCanHo, chuHo, pt.getLoaiXe(), pt.getMauXe() != null ? pt.getMauXe() : "N/A", pt.getTrangThai()));
            }
        } else {
            sb.append("  → Chưa có phương tiện nào đăng ký trong hệ thống.\n");
        }

        return sb.toString();
    }

    // =========================================================
    //  UTILITY
    // =========================================================

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() <= maxLen ? text : text.substring(0, maxLen) + "...";
    }
}
