package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.CuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class CanHoService {

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private CuDanRepository cuDanRepository;

    // Hàm lấy danh sách tất cả căn hộ
    public List<CanHo> layTatCaCanHo() {
        return canHoRepository.findAll();
    }

    // Gọi phương thức từ repository và truyền vào đối tượng Pageable để lấy dữ liệu
    // trang hiện tại
    public Page<CanHo> timKiemCanHo(String trangThai, Double dienTich, Integer tang, int page, int size) {
        // Khởi tạo đối tượng định dạng trang dữ liệu (PageRequest) dựa trên số trang và
        // kích thước
        Pageable pageable = PageRequest.of(page, size);
        return canHoRepository.timKiemCanHo(trangThai, dienTich, tang, pageable);
    }

    // Hàm lưu căn hộ mới với kiểm tra logic nghiệp vụ Validation
    public void luuCanHo(@Valid CanHo canHo) {
        try {
            // Lấy danh sách kiểm tra sự tồn tại của Mã căn hộ
            List<CanHo> tatCaCanHo = layTatCaCanHo();
            for (CanHo item : tatCaCanHo) {
                // Kiểm tra mã trùng (Bỏ qua trường hợp đang update chính mã đó)
                if (item.getMaCanHo() != null && item.getMaCanHo().equals(canHo.getMaCanHo())) {
                    if (canHo.getId() == null || !item.getId().equals(canHo.getId())) {
                        throw new IllegalArgumentException("Mã căn hộ: '" + canHo.getMaCanHo() + "' đã tồn tại! Vui lòng nhập mã khác.");
                    }
                }
            }

            if (canHo.getId() != null) {
                List<CuDan> residents = cuDanRepository.layTatCaCuDanTheoCanHo(canHo.getId());
                boolean hasResident = residents.stream().anyMatch(r -> "Đang Ở".equalsIgnoreCase(r.getTrangThai()) || "Đang ở".equalsIgnoreCase(r.getTrangThai()));
                canHo.setTrangThai(hasResident ? "Đã có chủ" : "Trống");
            } else {
                canHo.setTrangThai("Trống");
            }

            canHoRepository.save(canHo);
        } catch (IllegalArgumentException e) {
            // Ném lại Exception với thông báo tiếng Việt để Controller bắt
            throw e;
        } catch (Exception e) {
            // Bắt lỗi hệ thống cơ sở dữ liệu
            throw new IllegalArgumentException("Hệ thống gặp sự cố trong quá trình lưu Căn hộ. Vui lòng thử lại!");
        }
    }

    // Lấy căn hộ theo ID
    public CanHo layCanHoTheoId(Long id) {
        return canHoRepository.findById(id).orElse(null);
    }

    // Hàm xóa căn hộ
    public void xoaCanHo(Long id) {
        canHoRepository.deleteById(id);
    }

    public byte[] xuatExcelDanhSachCanHo() {
        List<CanHo> danhSachCanHo = layTatCaCanHo();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CanHo");

            int rowIdx = 0;
            Row header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "ID", "Mã căn hộ", "Diện tích (m2)", "Tầng", "Loại", "Trạng thái" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (CanHo canHoItem : danhSachCanHo) {
                Row row = sheet.createRow(rowIdx++);
                if (canHoItem.getId() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(canHoItem.getId().doubleValue());
                }
                row.createCell(1).setCellValue(canHoItem.getMaCanHo() == null ? "" : canHoItem.getMaCanHo());
                row.createCell(2).setCellValue(canHoItem.getDienTich() == null ? 0 : canHoItem.getDienTich());
                row.createCell(3).setCellValue(canHoItem.getTang() == null ? 0 : canHoItem.getTang());
                row.createCell(4).setCellValue(canHoItem.getLoai() == null ? "" : canHoItem.getLoai());
                row.createCell(5).setCellValue(canHoItem.getTrangThai() == null ? "" : canHoItem.getTrangThai());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất Excel danh sách căn hộ", e);
        }
    }

    // Import danh sách căn hộ từ file Excel
    // Cột Excel: Mã căn hộ | Diện tích (m2) | Tầng | Loại
    public String importExcelCanHo(MultipartFile file) {
        List<String> danhSachBoQua = new ArrayList<>();
        int soLuongThanhCong = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua dòng header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String maCanHo = formatter.formatCellValue(row.getCell(0)).trim();
                if (maCanHo.isEmpty()) continue;

                // Kiểm tra trùng mã căn hộ
                if (canHoRepository.findByMaCanHo(maCanHo).isPresent()) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Mã '" + maCanHo + "' đã tồn tại");
                    continue;
                }

                CanHo canHo = new CanHo();
                canHo.setMaCanHo(maCanHo);

                // Đọc diện tích
                try {
                    Cell cellDienTich = row.getCell(1);
                    if (cellDienTich != null && cellDienTich.getCellType() == CellType.NUMERIC) {
                        canHo.setDienTich(cellDienTich.getNumericCellValue());
                    } else {
                        String dtStr = formatter.formatCellValue(cellDienTich).trim();
                        if (!dtStr.isEmpty()) canHo.setDienTich(Double.parseDouble(dtStr));
                    }
                } catch (NumberFormatException e) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Diện tích không hợp lệ");
                    continue;
                }

                // Đọc tầng
                try {
                    Cell cellTang = row.getCell(2);
                    if (cellTang != null && cellTang.getCellType() == CellType.NUMERIC) {
                        canHo.setTang((int) cellTang.getNumericCellValue());
                    } else {
                        String tangStr = formatter.formatCellValue(cellTang).trim();
                        if (!tangStr.isEmpty()) canHo.setTang(Integer.parseInt(tangStr));
                    }
                } catch (NumberFormatException e) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": Số tầng không hợp lệ");
                    continue;
                }

                // Đọc loại
                String loai = formatter.formatCellValue(row.getCell(3)).trim();
                canHo.setLoai(loai.isEmpty() ? "Căn hộ tiêu chuẩn" : loai);

                // Mặc định trạng thái Trống
                canHo.setTrangThai("Trống");

                canHoRepository.save(canHo);
                soLuongThanhCong++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel: " + e.getMessage(), e);
        }

        StringBuilder ketQua = new StringBuilder();
        ketQua.append("Import thành công ").append(soLuongThanhCong).append(" căn hộ.");
        if (!danhSachBoQua.isEmpty()) {
            ketQua.append(" Bỏ qua ").append(danhSachBoQua.size()).append(" dòng: ");
            ketQua.append(String.join("; ", danhSachBoQua));
        }
        return ketQua.toString();
    }
}