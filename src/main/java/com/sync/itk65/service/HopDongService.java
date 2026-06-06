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
        List<HopDong> danhSach = hopDongRepository.findByCuDanId(cuDanId);
        danhSach.forEach(this::apDungDuLieuMacDinh);
        return danhSach;
    }

    public HopDong layHopDongTheoId(Long id) {
        HopDong hopDong = hopDongRepository.findById(id).orElse(null);
        apDungDuLieuMacDinh(hopDong);
        return hopDong;
    }

    public Optional<HopDong> layHopDongTheoIdVaCuDan(Long hopDongId, Long cuDanId) {
        Optional<HopDong> hopDong = hopDongRepository.findByIdAndCuDanId(hopDongId, cuDanId);
        hopDong.ifPresent(this::apDungDuLieuMacDinh);
        return hopDong;
    }

    public HopDong taoHopDong(HopDong hopDong, Long canHoId, Long cuDanId, int thoiHan) {
        // 1. Load entities
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ."));
        CuDan cuDan = cuDanRepository.findById(cuDanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân."));

        // 2. Validate cư dân thuộc căn hộ
        if (cuDan.getCanHo() == null || !cuDan.getCanHo().getId().equals(canHo.getId())) {
            throw new RuntimeException("Cư dân không thuộc căn hộ đã chọn.");
        }

        // 2b. Validate trạng thái cư dân — chỉ cư dân "Đang ở" mới được ký hợp đồng
        if (!"Đang ở".equalsIgnoreCase(cuDan.getTrangThai()) && !"Đang Ở".equalsIgnoreCase(cuDan.getTrangThai())) {
            throw new RuntimeException("Cư dân '" + cuDan.getHoTen() + "' có trạng thái '" + cuDan.getTrangThai() + "'. Chỉ cư dân đang sinh sống mới có thể ký hợp đồng.");
        }

        // 3. Cố định loại hợp đồng là Thuê
        hopDong.setLoaiHopDong("Thue");

        // 4. Validate cơ bản
        if (hopDong.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống.");
        }
        if (hopDong.getBenChoThue() == null || hopDong.getBenChoThue().trim().isEmpty()) {
            throw new RuntimeException("Bên cho thuê không được để trống.");
        }
        if (hopDong.getTienThue() == null || hopDong.getTienThue() <= 0) {
            throw new RuntimeException("Giá trị hợp đồng không hợp lệ.");
        }

        // 5. Validate thời hạn và tính ngày kết thúc tự động
        if (thoiHan != 1 && thoiHan != 6 && thoiHan != 12) {
            throw new RuntimeException("Thời hạn không hợp lệ. Chỉ chấp nhận 1, 6 hoặc 12 tháng.");
        }
        hopDong.setNgayKetThuc(hopDong.getNgayBatDau().plusMonths(thoiHan));

        // 6. Xử lý tiền cọc
        if (hopDong.getTienCoc() == null) {
            hopDong.setTienCoc(hopDong.getTienThue());
        }

        // 6. Set dữ liệu
        hopDong.setCanHo(canHo);
        hopDong.setCuDan(cuDan);
        hopDong.setBenChoThue(hopDong.getBenChoThue().trim());
        hopDong.setBenThue(cuDan.getHoTen());
        hopDong.setGiaTriHopDong(hopDong.getTienThue());
        hopDong.setTrangThai("ACTIVE");

        // 7. Kiểm tra trùng thời gian theo căn hộ
        boolean biTrungCanHo = hopDongRepository.existsActiveOverlapWithEndDate(canHo.getId(), hopDong.getNgayBatDau(), hopDong.getNgayKetThuc());
        if (biTrungCanHo) {
            throw new RuntimeException("Đã tồn tại hợp đồng ACTIVE trùng thời gian cho căn hộ này.");
        }

        // 8. Kiểm tra cư dân đã có hợp đồng ACTIVE chưa
        boolean cuDanDaCoHopDong = hopDongRepository.existsActiveByCuDanId(cuDan.getId());
        if (cuDanDaCoHopDong) {
            throw new RuntimeException("Cư dân '" + cuDan.getHoTen() + "' hiện đã có hợp đồng đang hiệu lực. Không thể tạo thêm hợp đồng mới.");
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

        // 3. Validate cư dân thuộc căn hộ
        if (cuDan.getCanHo() == null || !cuDan.getCanHo().getId().equals(canHo.getId())) {
            throw new RuntimeException("Cư dân không thuộc căn hộ đã chọn.");
        }

        // 3b. Validate trạng thái cư dân — chỉ cư dân "Đang ở" mới được ký hợp đồng
        if (!"Đang ở".equalsIgnoreCase(cuDan.getTrangThai()) && !"Đang Ở".equalsIgnoreCase(cuDan.getTrangThai())) {
            throw new RuntimeException("Cư dân '" + cuDan.getHoTen() + "' có trạng thái '" + cuDan.getTrangThai() + "'. Chỉ cư dân đang sinh sống mới có thể ký hợp đồng.");
        }

        // 4. Cố định loại hợp đồng Thue
        String loaiHopDong = "Thue";

        // 5. Validate cơ bản
        if (hopDongMoi.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống.");
        }
        if (hopDongMoi.getBenChoThue() == null || hopDongMoi.getBenChoThue().trim().isEmpty()) {
            throw new RuntimeException("Bên cho thuê không được để trống.");
        }
        if (hopDongMoi.getTienThue() == null || hopDongMoi.getTienThue() <= 0) {
            throw new RuntimeException("Giá trị hợp đồng không hợp lệ.");
        }

        // 6. Validate thời hạn và tính ngày kết thúc tự động
        if (thoiHan != 1 && thoiHan != 6 && thoiHan != 12) {
            throw new RuntimeException("Thời hạn không hợp lệ. Chỉ chấp nhận 1, 6 hoặc 12 tháng.");
        }
        LocalDate ngayKetThuc = hopDongMoi.getNgayBatDau().plusMonths(thoiHan);

        // 7. Kiểm tra trùng thời gian căn hộ (ngoại trừ chính hợp đồng này)
        boolean biTrungCanHo = hopDongRepository.existsActiveOverlapWithEndDateExcludeId(canHo.getId(), hopDongMoi.getNgayBatDau(), ngayKetThuc, hopDongId);
        if (biTrungCanHo) {
            throw new RuntimeException("Đã tồn tại hợp đồng ACTIVE trùng thời gian cho căn hộ này.");
        }

        // 8. Kiểm tra trùng cư dân: nếu đổi sang cư dân khác thì kiểm tra họ chưa có HĐ ACTIVE
        boolean cuDanDaThay = !cuDan.getId().equals(hopDong.getCuDan() != null ? hopDong.getCuDan().getId() : null);
        if (cuDanDaThay) {
            boolean cuDanDaCoHopDong = hopDongRepository.existsActiveByCuDanIdExcludeId(cuDan.getId(), hopDongId);
            if (cuDanDaCoHopDong) {
                throw new RuntimeException("Cư dân '" + cuDan.getHoTen() + "' hiện đã có hợp đồng đang hiệu lực. Không thể gán thêm hợp đồng mới.");
            }
        }

        // 8. Cập nhật dữ liệu
        hopDong.setCanHo(canHo);
        hopDong.setCuDan(cuDan);
        hopDong.setNgayBatDau(hopDongMoi.getNgayBatDau());
        hopDong.setNgayKetThuc(ngayKetThuc);
        hopDong.setBenChoThue(hopDongMoi.getBenChoThue().trim());
        hopDong.setBenThue(cuDan.getHoTen());
        hopDong.setTienThue(hopDongMoi.getTienThue());
        hopDong.setGiaTriHopDong(hopDongMoi.getTienThue());
        hopDong.setTienCoc(hopDongMoi.getTienCoc() != null ? hopDongMoi.getTienCoc() : hopDongMoi.getTienThue());
        hopDong.setLoaiHopDong(loaiHopDong);
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
