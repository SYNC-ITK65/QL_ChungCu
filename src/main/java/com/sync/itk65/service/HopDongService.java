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

    public HopDong taoHopDong(HopDong hopDong, Long canHoId, Long cuDanId) {
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ."));
        CuDan cuDan = cuDanRepository.findById(cuDanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân."));

        if (hopDong.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống.");
        }
        if (hopDong.getNgayKetThuc() == null) {
            throw new RuntimeException("Ngày kết thúc không được để trống.");
        }
        if (hopDong.getNgayKetThuc().isBefore(hopDong.getNgayBatDau())) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu.");
        }
        if (hopDong.getBenChoThue() == null || hopDong.getBenChoThue().trim().isEmpty()) {
            throw new RuntimeException("Bên cho thuê không được để trống.");
        }
        if (hopDong.getTienThue() == null || hopDong.getTienThue() <= 0) {
            throw new RuntimeException("Tiền thuê không hợp lệ.");
        }
        if (cuDan.getCanHo() == null || !cuDan.getCanHo().getId().equals(canHo.getId())) {
            throw new RuntimeException("Cư dân không thuộc căn hộ đã chọn.");
        }

        hopDong.setCanHo(canHo);
        hopDong.setCuDan(cuDan);
        hopDong.setBenChoThue(hopDong.getBenChoThue().trim());
        hopDong.setBenThue(cuDan.getHoTen());
        
        // VALIDATION: Loại hợp đồng - nếu null thì mặc định là "Thue", ngược lại giữ nguyên giá trị người dùng nhập
        if (hopDong.getLoaiHopDong() == null || hopDong.getLoaiHopDong().trim().isEmpty()) {
            hopDong.setLoaiHopDong("Thue");
        } else {
            hopDong.setLoaiHopDong(hopDong.getLoaiHopDong().trim());
        }
        
        // Giữ tương thích dữ liệu cũ: map giá trị hợp đồng = tiền thuê.
        hopDong.setGiaTriHopDong(hopDong.getTienThue());
        
        // VALIDATION: Tiền cọc - chỉ set mặc định nếu null, cho phép người dùng tùy chỉnh
        if (hopDong.getTienCoc() == null) {
            hopDong.setTienCoc(hopDong.getTienThue());
        }

        // VALIDATION: Kiểm tra trùng thời gian với hợp đồng ACTIVE khác
        // Vì đã validate ngayKetThuc không được null ở trên, nên chỉ cần check existsActiveOverlapWithEndDate
        boolean biTrung = hopDongRepository.existsActiveOverlapWithEndDate(
                canHo.getId(),
                hopDong.getNgayBatDau(),
                hopDong.getNgayKetThuc()
        );
        if (biTrung) {
            throw new RuntimeException("Đã tồn tại hợp đồng ACTIVE trùng thời gian cho căn hộ này.");
        }
        hopDong.setTrangThai("ACTIVE");

        HopDong savedHopDong = hopDongRepository.save(hopDong);

        // Tạo thông báo cho cư dân khi hợp đồng được tạo
        taoThongBaoHopDongMoi(savedHopDong, cuDan);

        return savedHopDong;
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

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float x = 50;
                float y = 780;
                float line = 24;

                ghiDong(contentStream, titleFont, 16, x, y, "HOP DONG THUE CAN HO");
                y -= line * 1.5f;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ma hop dong: HD-" + hopDong.getId());
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Can ho: " + (hopDong.getCanHo() != null ? hopDong.getCanHo().getMaCanHo() : ""));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ben cho thue: " + nullSafe(hopDong.getBenChoThue()));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ben thue: " + nullSafe(hopDong.getBenThue()));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ngay bat dau: " + (hopDong.getNgayBatDau() != null ? hopDong.getNgayBatDau().format(formatter) : ""));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Ngay ket thuc: " + (hopDong.getNgayKetThuc() != null ? hopDong.getNgayKetThuc().format(formatter) : ""));
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Tien thue: " + dinhDangTien(hopDong.getTienThue()) + " VND");
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Tien coc: " + dinhDangTien(hopDong.getTienCoc()) + " VND");
                y -= line;
                ghiDong(contentStream, bodyFont, 12, x, y, "Trang thai: " + nullSafe(hopDong.getTrangThai()));
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất PDF hợp đồng.", e);
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
    }

    private void taoThongBaoHopDongMoi(HopDong hopDong, CuDan cuDan) {
        try {
            com.sync.itk65.entity.ThongBao thongBao = new com.sync.itk65.entity.ThongBao();
            thongBao.setTieuDe("Hợp đồng mới đã được tạo");
            String noiDung = String.format(
                "Chào %s, một hợp đồng mới đã được tạo cho căn hộ %s. " +
                "Ngày bắt đầu: %s, Ngày kết thúc: %s, Tiền thuê: %,,.0f VND.",
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
}
