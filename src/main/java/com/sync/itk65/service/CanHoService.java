package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.repository.CanHoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Validated
public class CanHoService {

    @Autowired
    private CanHoRepository canHoRepository;

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
}