package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.HopDong;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.CuDanRepository;
import com.sync.itk65.repository.HopDongRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class HopDongService {

    @Autowired
    private HopDongRepository hopDongRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private CuDanRepository cuDanRepository;

    @Autowired
    private ThongBaoService thongBaoService;

    public List<HopDong> layTatCaHopDong() {
        List<HopDong> danhSach = hopDongRepository.findAllOrderByIdDesc();
        danhSach.forEach(this::apDungDuLieuMacDinh);
        return danhSach;
    }

    public Page<HopDong> layTatCaHopDong(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HopDong> trangDuLieu = hopDongRepository.findAllOrderByIdDesc(pageable);
        trangDuLieu.forEach(this::apDungDuLieuMacDinh);
        return trangDuLieu;
    }

    public Page<HopDong> timKiemHopDong(String maCanHo, String loaiHopDong, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HopDong> trangDuLieu = hopDongRepository.timKiemHopDong(maCanHo, loaiHopDong, trangThai, pageable);
        trangDuLieu.forEach(this::apDungDuLieuMacDinh);
        return trangDuLieu;
    }

    public List<HopDong> layHopDongCuaCuDan(Long cuDanId) {
        CuDan cuDan = cuDanRepository.findById(cuDanId).orElse(null);
        if (cuDan == null || cuDan.getCanHo() == null) {
            return java.util.Collections.emptyList();
        }
        List<HopDong> danhSach = hopDongRepository.findByCanHoId(cuDan.getCanHo().getId());
        danhSach.forEach(this::apDungDuLieuMacDinh);
        return danhSach;
    }

    public HopDong layHopDongTheoId(Long id) {
        HopDong hopDong = hopDongRepository.findById(id).orElse(null);
        apDungDuLieuMacDinh(hopDong);
        return hopDong;
    }

    public Optional<HopDong> layHopDongTheoIdVaCuDan(Long hopDongId, Long cuDanId) {
        CuDan cuDan = cuDanRepository.findById(cuDanId).orElse(null);
        if (cuDan == null || cuDan.getCanHo() == null) {
            return Optional.empty();
        }
        Optional<HopDong> hopDong = hopDongRepository.findByIdAndCanHoId(hopDongId, cuDan.getCanHo().getId());
        hopDong.ifPresent(this::apDungDuLieuMacDinh);
        return hopDong;
    }

    public HopDong taoHopDong(HopDong hopDong, Long canHoId, Long cuDanId, int thoiHan) {
        // 1. Load entities
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ."));
        CuDan cuDan = cuDanRepository.findById(cuDanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân."));

        // 2. Validate cư dân thuộc căn hộ và là chủ hộ
        if (cuDan.getCanHo() == null || !cuDan.getCanHo().getId().equals(canHo.getId())) {
            throw new RuntimeException("Cư dân không thuộc căn hộ đã chọn.");
        }
        if (!"Chủ hộ".equalsIgnoreCase(cuDan.getMoiQuanHe()) && !"Chủ Hộ".equalsIgnoreCase(cuDan.getMoiQuanHe())) {
            throw new RuntimeException("Chỉ chủ hộ mới được đứng tên hợp đồng.");
        }

        // 2b. Validate trạng thái cư dân — chỉ cư dân "Đang ở" mới được ký hợp đồng
        if (!"Đang ở".equalsIgnoreCase(cuDan.getTrangThai()) && !"Đang Ở".equalsIgnoreCase(cuDan.getTrangThai())) {
            throw new RuntimeException("Cư dân '" + cuDan.getHoTen() + "' có trạng thái '" + cuDan.getTrangThai() + "'. Chỉ cư dân đang sinh sống mới có thể ký hợp đồng.");
        }

        // 3. Đọc loại hợp đồng từ đối tượng hopDong (được gửi từ form) và cố định bên cho thuê là Công ty SYNC
        if (hopDong.getLoaiHopDong() == null || hopDong.getLoaiHopDong().trim().isEmpty()) {
            hopDong.setLoaiHopDong("Thue"); // Mặc định
        }
        boolean isThue = "Thue".equalsIgnoreCase(hopDong.getLoaiHopDong());
        hopDong.setBenChoThue("Công ty SYNC");

        // 4. Validate cơ bản
        if (hopDong.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống.");
        }
        if (hopDong.getTienThue() == null || hopDong.getTienThue() <= 0) {
            throw new RuntimeException("Giá trị hợp đồng không hợp lệ.");
        }

        // 5. Validate thời hạn và tính ngày kết thúc tự động nếu là Thuê
        if (isThue) {
            if (thoiHan != 1 && thoiHan != 6 && thoiHan != 12) {
                throw new RuntimeException("Thời hạn không hợp lệ. Chỉ chấp nhận 1, 6 hoặc 12 tháng.");
            }
            hopDong.setNgayKetThuc(hopDong.getNgayBatDau().plusMonths(thoiHan));

            // Xử lý tiền cọc
            if (hopDong.getTienCoc() == null) {
                hopDong.setTienCoc(hopDong.getTienThue());
            }
        } else {
            // Nếu là Mua
            hopDong.setNgayKetThuc(null);
            hopDong.setTienCoc(null);
        }

        // 6. Set dữ liệu
        hopDong.setCanHo(canHo);
        hopDong.setCuDan(cuDan);
        hopDong.setBenThue(cuDan.getHoTen());
        hopDong.setGiaTriHopDong(hopDong.getTienThue());
        hopDong.setTrangThai("ACTIVE");

        // 7. Kiểm tra căn hộ đã có hợp đồng ACTIVE chưa
        if (hopDongRepository.findActiveByCanHoId(canHo.getId()).isPresent()) {
            throw new RuntimeException("Căn hộ này đang có hợp đồng còn thời hạn (ACTIVE). Không thể tạo thêm hợp đồng mới.");
        }

        HopDong savedHopDong = hopDongRepository.save(hopDong);

        // Tạo thông báo cho cư dân khi hợp đồng được tạo
        taoThongBaoHopDongMoi(savedHopDong, cuDan);

        return savedHopDong;
    }

    @Transactional
    public HopDong capNhatHopDong(Long hopDongId, HopDong hopDongMoi, Long canHoId, Long cuDanId, int thoiHan) {
        // 1. Load hợp đồng hiện tại
        HopDong hopDong = hopDongRepository.findById(hopDongId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng."));
        
        // 2. Load entities mới
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ."));
        CuDan cuDan = cuDanRepository.findById(cuDanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân."));

        // 3. Validate cư dân thuộc căn hộ và là chủ hộ
        if (cuDan.getCanHo() == null || !cuDan.getCanHo().getId().equals(canHo.getId())) {
            throw new RuntimeException("Cư dân không thuộc căn hộ đã chọn.");
        }
        if (!"Chủ hộ".equalsIgnoreCase(cuDan.getMoiQuanHe()) && !"Chủ Hộ".equalsIgnoreCase(cuDan.getMoiQuanHe())) {
            throw new RuntimeException("Chỉ chủ hộ mới được đứng tên hợp đồng.");
        }

        // 3b. Validate trạng thái cư dân — chỉ cư dân "Đang ở" mới được ký hợp đồng
        if (!"Đang ở".equalsIgnoreCase(cuDan.getTrangThai()) && !"Đang Ở".equalsIgnoreCase(cuDan.getTrangThai())) {
            throw new RuntimeException("Cư dân '" + cuDan.getHoTen() + "' có trạng thái '" + cuDan.getTrangThai() + "'. Chỉ cư dân đang sinh sống mới có thể ký hợp đồng.");
        }

        // 4. Đọc loại hợp đồng từ đối tượng hopDongMoi và cố định bên cho thuê là Công ty SYNC
        String loaiHopDong = hopDongMoi.getLoaiHopDong();
        if (loaiHopDong == null || loaiHopDong.trim().isEmpty()) {
            loaiHopDong = "Thue";
        }
        boolean isThue = "Thue".equalsIgnoreCase(loaiHopDong);
        hopDong.setBenChoThue("Công ty SYNC");

        // 5. Validate cơ bản
        if (hopDongMoi.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống.");
        }
        if (hopDongMoi.getTienThue() == null || hopDongMoi.getTienThue() <= 0) {
            throw new RuntimeException("Giá trị hợp đồng không hợp lệ.");
        }

        LocalDate ngayKetThuc = null;
        Double tienCoc = null;

        // 6. Validate thời hạn nếu là Thuê
        if (isThue) {
            if (thoiHan != 1 && thoiHan != 6 && thoiHan != 12) {
                throw new RuntimeException("Thời hạn không hợp lệ. Chỉ chấp nhận 1, 6 hoặc 12 tháng.");
            }
            ngayKetThuc = hopDongMoi.getNgayBatDau().plusMonths(thoiHan);
            tienCoc = hopDongMoi.getTienCoc() != null ? hopDongMoi.getTienCoc() : hopDongMoi.getTienThue();
        }

        // 7. Kiểm tra căn hộ đã có hợp đồng ACTIVE khác hay chưa
        if (hopDongRepository.findActiveByCanHoIdExcludeId(canHo.getId(), hopDongId).isPresent()) {
            throw new RuntimeException("Căn hộ này đang có hợp đồng khác còn thời hạn (ACTIVE). Không thể cập nhật.");
        }

        // 8. Cập nhật dữ liệu
        hopDong.setCanHo(canHo);
        hopDong.setCuDan(cuDan);
        hopDong.setNgayBatDau(hopDongMoi.getNgayBatDau());
        hopDong.setNgayKetThuc(ngayKetThuc);
        hopDong.setBenThue(cuDan.getHoTen());
        hopDong.setTienThue(hopDongMoi.getTienThue());
        hopDong.setGiaTriHopDong(hopDongMoi.getTienThue());
        hopDong.setTienCoc(tienCoc);
        hopDong.setLoaiHopDong(loaiHopDong);
        // Giữ nguyên trạng thái hiện tại
        // Giữ nguyên trạng thái hiện tại

        return hopDongRepository.save(hopDong);
    }

    public void capNhatTrangThai(Long hopDongId, String trangThaiMoi) {
        HopDong hopDong = hopDongRepository.findById(hopDongId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng."));
        apDungDuLieuMacDinh(hopDong);
        if (!"ACTIVE".equalsIgnoreCase(trangThaiMoi)
                && !"EXPIRED".equalsIgnoreCase(trangThaiMoi)
                && !"TERMINATED".equalsIgnoreCase(trangThaiMoi)) {
            throw new RuntimeException("Trạng thái hợp đồng không hợp lệ.");
        }
        hopDong.setTrangThai(trangThaiMoi.toUpperCase());
        hopDongRepository.save(hopDong);
    }

    public byte[] xuatHopDongPdf(Long hopDongId) {
        HopDong hopDong = hopDongRepository.findById(hopDongId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng."));

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Xác định loại hợp đồng
            boolean isMua = "Mua".equals(hopDong.getLoaiHopDong());
            String title = isMua ? "HOP DONG MUA CAN HO" : "HOP DONG THUE CAN HO";
            String labelBenA = isMua ? "Ben ban" : "Ben cho thue";
            String labelBenB = isMua ? "Ben mua" : "Ben thue";
            String labelGiaTri = isMua ? "Gia tri hop dong" : "Tien thue";
            String loaiHopDongDisplay = isMua ? "Mua" : "Thue";

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float x = 50;
                float y = 780;
                float line = 24;

                ghiDong(contentStream, titleFont, 16, x, y, title);
                y -= line * 1.5f;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ma hop dong: HD-" + hopDong.getId());
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Loai hop dong: " + loaiHopDongDisplay);
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Can ho: " + (hopDong.getCanHo() != null ? loaiBoDau(hopDong.getCanHo().getMaCanHo()) : ""));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, labelBenA + ": " + loaiBoDau(hopDong.getBenChoThue()));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, labelBenB + ": " + loaiBoDau(hopDong.getBenThue()));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ngay bat dau: " + (hopDong.getNgayBatDau() != null ? hopDong.getNgayBatDau().format(formatter) : ""));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ngay ket thuc: " + (hopDong.getNgayKetThuc() != null ? hopDong.getNgayKetThuc().format(formatter) : "Khong co (hop dong mua)"));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, labelGiaTri + ": " + dinhDangTien(hopDong.getTienThue()) + " VND");
                y -= line;
                if (!isMua) {
                    ghiDong(contentStream, bodyFont, 12, x, y, "Tien coc: " + dinhDangTien(hopDong.getTienCoc()) + " VND");
                    y -= line;
                }
                ghiDong(contentStream, bodyFont, 12, x, y, "Trang thai: " + nullSafe(hopDong.getTrangThai()));
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất PDF hợp đồng.", e);
        }
    }

    private String loaiBoDau(String str) {
        if (str == null) return "";
        try {
            String temp = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String result = pattern.matcher(temp).replaceAll("")
                    .replace("đ", "d")
                    .replace("Đ", "D");
            StringBuilder sb = new StringBuilder();
            for (char c : result.toCharArray()) {
                if (c < 128) {
                    sb.append(c);
                } else {
                    sb.append('?');
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return str;
        }
    }

    private void ghiDong(PDPageContentStream contentStream, PDType1Font font, int size, float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private String dinhDangTien(Double soTien) {
        return soTien == null ? "0" : String.format("%,.0f", soTien);
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private void apDungDuLieuMacDinh(HopDong hopDong) {
        if (hopDong == null) {
            return;
        }
        if (hopDong.getBenThue() == null && hopDong.getCuDan() != null) {
            hopDong.setBenThue(hopDong.getCuDan().getHoTen());
        }
        if (hopDong.getTienThue() == null && hopDong.getGiaTriHopDong() != null) {
            hopDong.setTienThue(hopDong.getGiaTriHopDong());
        }
        // Chỉ cập nhật trạng thái in-memory; việc lưu DB do scheduler tuDongHetHanHopDong đảm nhiệm
        if ("ACTIVE".equalsIgnoreCase(hopDong.getTrangThai())
                && hopDong.getNgayKetThuc() != null
                && hopDong.getNgayKetThuc().isBefore(LocalDate.now())) {
            hopDong.setTrangThai("EXPIRED");
        }
    }

    private void taoThongBaoHopDongMoi(HopDong hopDong, CuDan cuDan) {
        try {
            com.sync.itk65.entity.ThongBao thongBao = new com.sync.itk65.entity.ThongBao();
            thongBao.setTieuDe("Hợp đồng mới đã được tạo");
            String noiDung = String.format(
                "Chào %s, một hợp đồng mới đã được tạo cho căn hộ %s. " +
                "Ngày bắt đầu: %s, Ngày kết thúc: %s, Tiền thuê: %,.0f VND.",
                cuDan.getHoTen(),
                hopDong.getCanHo() != null ? hopDong.getCanHo().getMaCanHo() : "N/A",
                hopDong.getNgayBatDau() != null ? hopDong.getNgayBatDau().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A",
                hopDong.getNgayKetThuc() != null ? hopDong.getNgayKetThuc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A",
                hopDong.getTienThue()
            );
            thongBao.setNoiDung(noiDung);
            thongBao.setLoai(1); // Loại thông báo chung
            thongBao.setDoiTuongGui("HO_GIA_DINH");
            thongBao.setGiaTriDoiTuong(hopDong.getCanHo() != null ? hopDong.getCanHo().getMaCanHo() : "");
            thongBaoService.saveThongBao(thongBao);
        } catch (Exception e) {
            // Không throw exception để không ảnh hưởng đến việc tạo hợp đồng
            System.err.println("Lỗi khi tạo thông báo hợp đồng: " + e.getMessage());
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void tuDongHetHanHopDong() {
        List<HopDong> expiredContracts = hopDongRepository.findExpiredActiveContracts(LocalDate.now());
        for (HopDong hopDong : expiredContracts) {
            hopDong.setTrangThai("EXPIRED");
            hopDongRepository.save(hopDong);
        }
    }

    @Transactional
    public void xoaHopDong(Long id) {
        HopDong hopDong = hopDongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng có ID: " + id));
        hopDongRepository.delete(hopDong);
    }
}
