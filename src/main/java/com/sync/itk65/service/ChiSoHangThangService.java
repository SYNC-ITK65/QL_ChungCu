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
        return chiSoHangThangRepository.findAllOrderByNgayGhiNhanDesc();
    }

    public Page<ChiSoHangThang> layTatCaChiSo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chiSoHangThangRepository.findAll(pageable);
    }

    // Tìm kiếm chỉ số theo nhiều điều kiện
    public List<ChiSoHangThang> timKiemChiSo(String maCanHo, Long canHoId, Integer thang, Integer nam) {
        return chiSoHangThangRepository.searchWithFilters(maCanHo, canHoId, thang, nam);
    }

    // Lấy chỉ số theo ID căn hộ
    public List<ChiSoHangThang> layChiSoTheoCanHo(Long canHoId) {
        return chiSoHangThangRepository.findByCanHoId(canHoId);
    }

    // Lưu chỉ số mới
    public ChiSoHangThang luuChiSo(ChiSoHangThang chiSoMoi) {
        // 1. Tự động gán ngày nếu chưa có
        if (chiSoMoi.getNgayGhiNhan() == null) {
            chiSoMoi.setNgayGhiNhan(LocalDate.now());
        }

        int thang = chiSoMoi.getNgayGhiNhan().getMonthValue();
        int nam = chiSoMoi.getNgayGhiNhan().getYear();

        // 2. Kiểm tra xem Căn hộ này trong Tháng/Năm đó đã có chỉ số chưa
        Optional<ChiSoHangThang> chiSoCu = chiSoHangThangRepository
                .findByCanHoAndThangNam(chiSoMoi.getCanHo().getId(), thang, nam);
        if (chiSoCu.isPresent()) {
            // ĐÃ CÓ DỮ LIỆU -> Ghi đè (Update) số mới lên bản ghi cũ
            ChiSoHangThang cs = chiSoCu.get();
            cs.setDienTieuThu(chiSoMoi.getDienTieuThu());
            cs.setNuocTieuThu(chiSoMoi.getNuocTieuThu());
            cs.setNgayGhiNhan(chiSoMoi.getNgayGhiNhan());

            return chiSoHangThangRepository.save(cs);
        }

        // CHƯA CÓ DỮ LIỆU -> Lưu mới hoàn toàn (Insert)
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

                String ngayGhiNhanStr = formatter.formatCellValue(row.getCell(0)).trim();
                String maCanHo = formatter.formatCellValue(row.getCell(1)).trim();
                String dienTieuThuStr = formatter.formatCellValue(row.getCell(2)).trim();
                String nuocTieuThuStr = formatter.formatCellValue(row.getCell(3)).trim();

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
                } catch (Exception ex) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Chỉ số điện/nước không hợp lệ");
                    continue;
                }

                ChiSoHangThang chiSo = new ChiSoHangThang();
                chiSo.setCanHo(canHo);
                chiSo.setNgayGhiNhan(ngayGhiNhan);
                chiSo.setDienTieuThu(dienTieuThu);
                chiSo.setNuocTieuThu(nuocTieuThu);
                luuChiSo(chiSo);
                soLuongThanhCong++;
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
}