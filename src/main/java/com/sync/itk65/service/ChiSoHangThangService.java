package com.sync.itk65.service;

import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.entity.CanHo;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.ChiSoHangThangRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChiSoHangThangService {

    @Autowired
    private ChiSoHangThangRepository chiSoHangThangRepository;
    @Autowired
    private CanHoRepository canHoRepository;

    // Lấy danh sách toàn bộ chỉ số, sắp xếp ngày mới nhất lên đầu
    public List<ChiSoHangThang> layTatCaChiSo() {
        return chiSoHangThangRepository.findAllOrderByNgayGhiNhanDesc(Pageable.unpaged()).getContent();
    }

    public Page<ChiSoHangThang> layTatCaChiSo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chiSoHangThangRepository.findAll(pageable);
    }

    // Tìm kiếm chỉ số theo nhiều điều kiện
    public Page<ChiSoHangThang> timKiemChiSo(String maCanHo, Long canHoId, Integer thang, Integer nam, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chiSoHangThangRepository.searchWithFilters(maCanHo, canHoId, thang, nam, pageable);
    }

    // Lấy chỉ số theo ID căn hộ
    public List<ChiSoHangThang> layChiSoTheoCanHo(Long canHoId) {
        return chiSoHangThangRepository.findByCanHoId(canHoId);
    }

    // Lưu chỉ số mới
    public ChiSoHangThang luuChiSo(ChiSoHangThang chiSoMoi) {
        if (chiSoMoi == null) {
            throw new IllegalArgumentException("Chỉ số không được null.");
        }
        if (chiSoMoi.getCanHo() == null || chiSoMoi.getCanHo().getId() == null) {
            throw new IllegalArgumentException("Chỉ số phải có căn hộ hợp lệ.");
        }
        // 1. Tự động gán ngày nếu chưa có
        if (chiSoMoi.getNgayGhiNhan() == null) {
            chiSoMoi.setNgayGhiNhan(LocalDate.now());
        }

        if (chiSoMoi.getDienTieuThu() == null) {
            throw new IllegalArgumentException("Chỉ số điện tiêu thụ không được null.");
        }
        if (chiSoMoi.getNuocTieuThu() == null) {
            throw new IllegalArgumentException("Chỉ số nước tiêu thụ không được null.");
        }
        requireFiniteNonNegative(chiSoMoi.getDienTieuThu(), "Chỉ số điện");
        requireFiniteNonNegative(chiSoMoi.getNuocTieuThu(), "Chỉ số nước");

        Long canHoId = chiSoMoi.getCanHo().getId();
        LocalDate ngayGhiNhan = chiSoMoi.getNgayGhiNhan();
        LocalDate firstDayOfMonth = ngayGhiNhan.withDayOfMonth(1);
        LocalDate lastDayOfMonth = ngayGhiNhan.withDayOfMonth(ngayGhiNhan.lengthOfMonth());

        // Cho phép nhiều bản ghi trong cùng tháng, nhưng phải chặt chẽ:
        // - Ngày ghi nhận phải tăng dần so với bản ghi gần nhất trong tháng đó
        // - Giá trị điện/nước không được giảm so với bản ghi gần nhất
        Optional<ChiSoHangThang> chiSoGanNhatTrongThang = chiSoHangThangRepository
                .findFirstByCanHoIdAndNgayGhiNhanBetweenOrderByNgayGhiNhanDescIdDesc(canHoId, firstDayOfMonth, lastDayOfMonth);

        if (chiSoGanNhatTrongThang.isPresent()) {
            ChiSoHangThang cs = chiSoGanNhatTrongThang.get();

            if (cs.getNgayGhiNhan() != null) {
                if (!ngayGhiNhan.isAfter(cs.getNgayGhiNhan())) {
                    throw new IllegalArgumentException("Ngày ghi nhận phải sau bản ghi gần nhất trong tháng (" + cs.getNgayGhiNhan() + ").");
                }
            }

            if (cs.getDienTieuThu() != null && chiSoMoi.getDienTieuThu() < cs.getDienTieuThu()) {
                throw new IllegalArgumentException("Chỉ số điện mới không được thấp hơn bản ghi gần nhất trong tháng.");
            }
            if (cs.getNuocTieuThu() != null && chiSoMoi.getNuocTieuThu() < cs.getNuocTieuThu()) {
                throw new IllegalArgumentException("Chỉ số nước mới không được thấp hơn bản ghi gần nhất trong tháng.");
            }
        }

        return chiSoHangThangRepository.save(chiSoMoi);
    }

    // Xóa chỉ số
    public void xoaChiSo(Long id) {
        chiSoHangThangRepository.deleteById(id);
    }

    public byte[] xuatExcelDanhSachChiSo() {
        List<ChiSoHangThang> danhSachChiSo = layTatCaChiSo();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ChiSo");
            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "ID", "Ngày ghi nhận", "Mã căn hộ", "Điện tiêu thụ", "Nước tiêu thụ" };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (ChiSoHangThang chiSo : danhSachChiSo) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(chiSo.getId() == null ? "" : String.valueOf(chiSo.getId()));
                row.createCell(1).setCellValue(chiSo.getNgayGhiNhan() == null ? "" : chiSo.getNgayGhiNhan().toString());
                row.createCell(2).setCellValue(
                        (chiSo.getCanHo() != null && chiSo.getCanHo().getMaCanHo() != null) ? chiSo.getCanHo().getMaCanHo()
                                : "");
                row.createCell(3).setCellValue(chiSo.getDienTieuThu() == null ? 0 : chiSo.getDienTieuThu());
                row.createCell(4).setCellValue(chiSo.getNuocTieuThu() == null ? 0 : chiSo.getNuocTieuThu());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất Excel danh sách chỉ số", e);
        }
    }

    public String importExcelChiSo(MultipartFile file) {
        List<String> danhSachBoQua = new ArrayList<>();
        int soLuongThanhCong = 0;

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String ngayGhiNhanStr = cellAsString(row, 0, formatter);
                String maCanHo = cellAsString(row, 1, formatter);
                String dienTieuThuStr = cellAsString(row, 2, formatter);
                String nuocTieuThuStr = cellAsString(row, 3, formatter);

                if (maCanHo.isEmpty()) {
                    continue;
                }

                CanHo canHo = canHoRepository.findByMaCanHo(maCanHo).orElse(null);
                if (canHo == null) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Mã căn hộ '" + maCanHo + "' không tồn tại");
                    continue;
                }

                LocalDate ngayGhiNhan;
                try {
                    ngayGhiNhan = ngayGhiNhanStr.isEmpty() ? LocalDate.now() : LocalDate.parse(ngayGhiNhanStr);
                } catch (Exception ex) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Ngày ghi nhận không hợp lệ");
                    continue;
                }

                Double dienTieuThu;
                Double nuocTieuThu;
                try {
                    dienTieuThu = dienTieuThuStr.isEmpty() ? 0.0 : Double.parseDouble(dienTieuThuStr);
                    nuocTieuThu = nuocTieuThuStr.isEmpty() ? 0.0 : Double.parseDouble(nuocTieuThuStr);
                    requireFiniteNonNegative(dienTieuThu, "Chỉ số điện");
                    requireFiniteNonNegative(nuocTieuThu, "Chỉ số nước");
                } catch (Exception ex) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Chỉ số điện/nước không hợp lệ");
                    continue;
                }

                ChiSoHangThang chiSo = new ChiSoHangThang();
                chiSo.setCanHo(canHo);
                chiSo.setNgayGhiNhan(ngayGhiNhan);
                chiSo.setDienTieuThu(dienTieuThu);
                chiSo.setNuocTieuThu(nuocTieuThu);
                try {
                    luuChiSo(chiSo);
                    soLuongThanhCong++;
                } catch (Exception ex) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": " + ex.getMessage().replace("\n", " "));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel: " + e.getMessage(), e);
        }

        StringBuilder ketQua = new StringBuilder();
        ketQua.append("Import thành công ").append(soLuongThanhCong).append(" chỉ số.");
        if (!danhSachBoQua.isEmpty()) {
            ketQua.append(" Bỏ qua ").append(danhSachBoQua.size()).append(" dòng: ");
            ketQua.append(String.join("; ", danhSachBoQua));
        }
        return ketQua.toString();
    }

    private static void requireFiniteNonNegative(Double value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " không được null.");
        }
        if (value.isNaN() || value.isInfinite()) {
            throw new IllegalArgumentException(fieldName + " không được NaN/Infinity.");
        }
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " không được âm.");
        }
    }

    private static String cellAsString(Row row, int cellIdx, DataFormatter formatter) {
        var cell = row.getCell(cellIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return "";
        }
        String val = formatter.formatCellValue(cell);
        return val == null ? "" : val.trim();
    }

    // ============================================================
    //  TẠO CHỈ SỐ HÀNG LOẠT (sinh ngẫu nhiên hợp lý)
    // ============================================================

    /**
     * Kết quả sau khi tạo chỉ số hàng loạt.
     */
    public static class KetQuaTaoHangLoat {
        private int soThanhCong = 0;
        private final List<String> danhSachBoQua = new ArrayList<>();
        private final List<String> danhSachLoi   = new ArrayList<>();

        public int getSoThanhCong()             { return soThanhCong; }
        public void taoThanhCong()              { soThanhCong++; }
        public List<String> getDanhSachBoQua()  { return danhSachBoQua; }
        public List<String> getDanhSachLoi()    { return danhSachLoi; }
        public int getSoBoQua()                 { return danhSachBoQua.size(); }
        public int getSoLoi()                   { return danhSachLoi.size(); }
        public int getTongCanHo()               { return soThanhCong + danhSachBoQua.size() + danhSachLoi.size(); }
        public boolean coKetQua()               { return getTongCanHo() > 0; }
    }

    /**
     * Tạo chỉ số hàng loạt cho tất cả căn hộ trong tháng/năm.
     * Bỏ qua căn hộ đã có chỉ số trong tháng đó.
     * Sinh ngẫu nhiên hợp lý: điện 80-350 kWh, nước 8-45 m³.
     */
    public KetQuaTaoHangLoat taoChiSoHangLoat(int thang, int nam) {
        if (thang < 1 || thang > 12) {
            throw new IllegalArgumentException("Tháng không hợp lệ (phải từ 1 đến 12).");
        }
        LocalDate ngayHienTai = LocalDate.now();
        LocalDate thangDuoc = LocalDate.of(nam, thang, 1);
        LocalDate thangHienTai = LocalDate.of(ngayHienTai.getYear(), ngayHienTai.getMonthValue(), 1);

        if (thangDuoc.isAfter(thangHienTai)) {
            throw new IllegalArgumentException("Không thể tạo chỉ số cho tháng tương lai (" + thang + "/" + nam + ").");
        }
        if (thangDuoc.isBefore(thangHienTai.minusMonths(12))) {
            throw new IllegalArgumentException("Không thể tạo chỉ số cho tháng quá 12 tháng trước (" + thang + "/" + nam + ").");
        }

        // Ngày ghi nhận: ngày cuối tháng (hoặc hôm nay nếu là tháng hiện tại)
        LocalDate ngayGhiNhan;
        if (thang == ngayHienTai.getMonthValue() && nam == ngayHienTai.getYear()) {
            ngayGhiNhan = ngayHienTai;
        } else {
            ngayGhiNhan = LocalDate.of(nam, thang, 1)
                .withDayOfMonth(LocalDate.of(nam, thang, 1).lengthOfMonth());
        }

        LocalDate firstDayOfMonth = LocalDate.of(nam, thang, 1);
        LocalDate lastDayOfMonth  = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        java.util.Random rng = new java.util.Random();
        // Chỉ tạo chỉ số cho căn hộ có cư dân đang ở, bỏ qua căn hộ trống
        List<com.sync.itk65.entity.CanHo> tatCaCanHo = canHoRepository.findCanHoCoDanDangO();
        if (tatCaCanHo == null || tatCaCanHo.isEmpty()) {
            throw new IllegalArgumentException("Không có căn hộ nào đang có người ở trong hệ thống.");
        }

        KetQuaTaoHangLoat ketQua = new KetQuaTaoHangLoat();

        for (com.sync.itk65.entity.CanHo canHo : tatCaCanHo) {
            String maCanHo = canHo.getMaCanHo() != null ? canHo.getMaCanHo() : "ID:" + canHo.getId();

            // Kiểm tra đã có chỉ số chưa → bỏ qua
            try {
                boolean daCoChiSo = chiSoHangThangRepository
                    .findFirstByCanHoIdAndNgayGhiNhanBetweenOrderByNgayGhiNhanDescIdDesc(
                        canHo.getId(), firstDayOfMonth, lastDayOfMonth)
                    .isPresent();
                if (daCoChiSo) {
                    ketQua.getDanhSachBoQua().add(maCanHo);
                    continue;
                }
            } catch (Exception e) {
                System.out.println("WARN: Không kiểm tra được chỉ số cho " + maCanHo + ": " + e.getMessage());
            }

            // Sinh chỉ số ngẫu nhiên hợp lý
            try {
                // Điện: 80–350 kWh (thực tế hộ gia đình)
                double dienTieuThu = Math.round((80 + rng.nextDouble() * 270) * 10.0) / 10.0;
                // Nước: 8–45 m³
                double nuocTieuThu = Math.round((8 + rng.nextDouble() * 37) * 10.0) / 10.0;

                ChiSoHangThang chiSo = new ChiSoHangThang();
                chiSo.setCanHo(canHo);
                chiSo.setDienTieuThu(dienTieuThu);
                chiSo.setNuocTieuThu(nuocTieuThu);
                chiSo.setNgayGhiNhan(ngayGhiNhan);

                // Lưu trực tiếp (bypass validation ngày phức tạp khi tạo hàng loạt)
                chiSoHangThangRepository.save(chiSo);
                ketQua.taoThanhCong();
                System.out.println("INFO [ChiSoHangLoat] " + maCanHo
                    + " | Điện=" + dienTieuThu + " kWh | Nước=" + nuocTieuThu + " m³"
                    + " | Ngày=" + ngayGhiNhan);
            } catch (Exception e) {
                String msg = e.getMessage() != null
                    ? e.getMessage().replaceAll("\\r?\\n", " ").trim()
                    : "Lỗi không xác định";
                if (msg.length() > 200) msg = msg.substring(0, 197) + "...";
                ketQua.getDanhSachLoi().add(maCanHo + ": " + msg);
                System.out.println("WARN [ChiSoHangLoat] Không tạo được chỉ số cho " + maCanHo + ": " + msg);
            }
        }

        System.out.println("INFO [ChiSoHangLoat] Kết quả tháng " + thang + "/" + nam
            + " | Thành công: " + ketQua.getSoThanhCong()
            + " | Bỏ qua: " + ketQua.getSoBoQua()
            + " | Lỗi: " + ketQua.getSoLoi());

        return ketQua;
    }
}
