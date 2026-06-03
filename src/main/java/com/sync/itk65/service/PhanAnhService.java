package com.sync.itk65.service;

import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.repository.PhanAnhRepository;
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
public class PhanAnhService {

    @Autowired
    private PhanAnhRepository phanAnhRepository;

    public List<PhanAnh> findAll() {
        return phanAnhRepository.findAll();
    }

    public Page<PhanAnh> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return phanAnhRepository.findAll(pageable);
    }

    public PhanAnh findById(Long id) {
        return phanAnhRepository.findById(id).orElse(null);
    }

    public List<PhanAnh> findByCanHoId(Long canHoId) {
        return phanAnhRepository.findByCanHoIdOrderByNgayGuiDesc(canHoId);
    }

    public PhanAnh save(PhanAnh phanAnh) {
        return phanAnhRepository.save(phanAnh);
    }

    public void deleteById(Long id) {
        phanAnhRepository.deleteById(id);
    }
    public Page<PhanAnh> timKiemPhanAnh(String tuKhoa, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return phanAnhRepository.timKiemVaLocPhanAnh(tuKhoa, trangThai, pageable);
    }

    public byte[] xuatExcelPhanAnh(String tuKhoa, String trangThai) {
        Page<PhanAnh> trangDuLieu;
        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) || (trangThai != null && !trangThai.trim().isEmpty())) {
            trangDuLieu = timKiemPhanAnh(tuKhoa, trangThai, 0, Integer.MAX_VALUE);
        } else {
            trangDuLieu = findAll(0, Integer.MAX_VALUE);
        }

        List<PhanAnh> danhSach = trangDuLieu.getContent();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DanhSachPhanAnh");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = {"Mã Căn Hộ", "Người Đại Diện", "Tiêu Đề", "Nội Dung", "Ngày Gửi", "Trạng Thái", "Phản Hồi Admin", "Ngày Phản Hồi"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (PhanAnh pa : danhSach) {
                var row = sheet.createRow(rowIdx++);
                String maCanHo = pa.getCanHo() != null ? String.valueOf(pa.getCanHo().getId()) : "Trống";
                String tenCuDan = "";
                if (pa.getCanHo() != null && pa.getCanHo().getDanhSachCuDan() != null && !pa.getCanHo().getDanhSachCuDan().isEmpty()) {
                    tenCuDan = pa.getCanHo().getDanhSachCuDan().get(0).getHoTen();
                }
                
                row.createCell(0).setCellValue(maCanHo);
                row.createCell(1).setCellValue(tenCuDan);
                row.createCell(2).setCellValue(pa.getTieuDe() != null ? pa.getTieuDe() : "");
                row.createCell(3).setCellValue(pa.getNoiDung() != null ? pa.getNoiDung() : "");
                row.createCell(4).setCellValue(pa.getNgayGui() != null ? pa.getNgayGui().format(formatter) : "");
                row.createCell(5).setCellValue(pa.getTrangThai() != null ? pa.getTrangThai() : "");
                row.createCell(6).setCellValue(pa.getPhanHoi() != null ? pa.getPhanHoi() : "");
                row.createCell(7).setCellValue(pa.getNgayPhanHoi() != null ? pa.getNgayPhanHoi().format(formatter) : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file Excel danh sách phản ánh", e);
        }
    }
}