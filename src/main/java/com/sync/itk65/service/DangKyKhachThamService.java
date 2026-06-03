package com.sync.itk65.service;
import com.sync.itk65.entity.DangKyKhachTham;
import com.sync.itk65.repository.DangKyKhachThamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class DangKyKhachThamService {
    @Autowired private DangKyKhachThamRepository repository;
    public void luu(DangKyKhachTham khach) { repository.save(khach); }
    public Page<DangKyKhachTham> layTatCa(int page, int size) { return repository.findAllByOrderByThoiGianDuKienDesc(PageRequest.of(page, size)); }

    public Page<DangKyKhachTham> timKiemKhachTham(String tuKhoa, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.timKiemVaLocKhachTham(tuKhoa, trangThai, pageable);
    }

    public List<DangKyKhachTham> layTatCa() { return repository.findAll(); }
    public Page<DangKyKhachTham> layLichSuCuaCuDan(Long cuDanId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCuDanIdOrderByThoiGianDuKienDesc(cuDanId, pageable);
    }
    public DangKyKhachTham timTheoId(Long id) { return repository.findById(id).orElse(null); }
    public void xoa(Long id) {
        repository.deleteById(id);
    }

    public byte[] xuatExcelKhachTham(String tuKhoa, String trangThai) {
        Page<DangKyKhachTham> trangDuLieu;
        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) || (trangThai != null && !trangThai.trim().isEmpty())) {
            trangDuLieu = timKiemKhachTham(tuKhoa, trangThai, 0, Integer.MAX_VALUE);
        } else {
            trangDuLieu = layTatCa(0, Integer.MAX_VALUE);
        }

        List<DangKyKhachTham> danhSach = trangDuLieu.getContent();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DanhSachKhachTham");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = {"Cư dân", "Khách", "Thời gian đến", "Thời gian đi", "Trạng thái", "Thời gian duyệt"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
            for (DangKyKhachTham k : danhSach) {
                var row = sheet.createRow(rowIdx++);
                String cuDan = k.getCuDan() != null ? k.getCuDan().getHoTen() : "";
                
                row.createCell(0).setCellValue(cuDan);
                row.createCell(1).setCellValue(k.getTenKhach() != null ? k.getTenKhach() : "");
                row.createCell(2).setCellValue(k.getThoiGianDuKien() != null ? k.getThoiGianDuKien().format(formatter) : "");
                row.createCell(3).setCellValue(k.getNgayDi() != null ? k.getNgayDi().format(formatter) : "-");
                row.createCell(4).setCellValue(k.getTrangThai() != null ? k.getTrangThai() : "");
                row.createCell(5).setCellValue(k.getThoiGianDuyet() != null ? k.getThoiGianDuyet().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "-");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file Excel danh sách khách thăm", e);
        }
    }
}