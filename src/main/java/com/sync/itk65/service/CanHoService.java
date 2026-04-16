package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.repository.CanHoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CanHoService {

    @Autowired
    private CanHoRepository canHoRepository;

    // Hàm lấy danh sách tất cả căn hộ
    public List<CanHo> layTatCaCanHo() {
        return canHoRepository.findAll();
    }

    // Hàm tìm kiếm và phân trang
    public Page<CanHo> timKiemCanHo(String trangThai, Double dienTich, Integer tang, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return canHoRepository.timKiemCanHo(trangThai, dienTich, tang, pageable);
    }

    // Hàm lưu căn hộ mới
    public void luuCanHo(CanHo canHo) {
        canHoRepository.save(canHo);
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
        List<CanHo> danhSach = layTatCaCanHo();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CanHo");

            int rowIdx = 0;
            Row header = sheet.createRow(rowIdx++);
            String[] headers = new String[] {"ID", "Mã căn hộ", "Diện tích (m2)", "Tầng", "Loại", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (CanHo ch : danhSach) {
                Row row = sheet.createRow(rowIdx++);
                if (ch.getId() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(ch.getId().doubleValue());
                }
                row.createCell(1).setCellValue(ch.getMaCanHo() == null ? "" : ch.getMaCanHo());
                row.createCell(2).setCellValue(ch.getDienTich() == null ? 0 : ch.getDienTich());
                row.createCell(3).setCellValue(ch.getTang() == null ? 0 : ch.getTang());
                row.createCell(4).setCellValue(ch.getLoai() == null ? "" : ch.getLoai());
                row.createCell(5).setCellValue(ch.getTrangThai() == null ? "" : ch.getTrangThai());
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