package com.sync.itk65.service;

import com.sync.itk65.entity.TaiSan;
import com.sync.itk65.repository.TaiSanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaiSanService {

    @Autowired
    private TaiSanRepository taiSanRepository;

    public List<TaiSan> getAllTaiSan() {
        return taiSanRepository.findAll();
    }

    public Page<TaiSan> getAllTaiSan(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taiSanRepository.findAll(pageable);
    }

    public List<TaiSan> getAlertAssets() {
        return taiSanRepository.findAlertAssets(LocalDate.now().plusDays(7));
    }

    public Page<TaiSan> getAlertAssets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taiSanRepository.findAlertAssets(LocalDate.now().plusDays(7), pageable);
    }

    public Optional<TaiSan> getTaiSanById(Long id) {
        return taiSanRepository.findById(id);
    }

    public TaiSan saveTaiSan(TaiSan taiSan) {
        // Initial calculation of ngayBaoTriTiepTheo if not set
        if (taiSan.getId() == null && taiSan.getNgayBaoTriTiepTheo() == null
                && taiSan.getNgayMua() != null && taiSan.getChuKyBaoTri() != null) {
            taiSan.setNgayBaoTriTiepTheo(taiSan.getNgayMua().plusMonths(taiSan.getChuKyBaoTri()));
        }
        return taiSanRepository.save(taiSan);
    }

    public void deleteTaiSan(Long id) {
        taiSanRepository.deleteById(id);
    }

    public long countAlertAssets() {
        return getAlertAssets().size();
    }

    public byte[] xuatExcelDanhSachTaiSan(boolean chiCanhBao) {
        List<TaiSan> danhSach = chiCanhBao ? getAlertAssets() : getAllTaiSan();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("TaiSan");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "Mã", "Tên tài sản", "Vị trí", "Ngày mua", "Trạng thái",
                    "Bảo trì tiếp theo" };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (TaiSan ts : danhSach) {
                var row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(ts.getMaTaiSan() == null ? "" : ts.getMaTaiSan());
                row.createCell(1).setCellValue(ts.getTenTaiSan() == null ? "" : ts.getTenTaiSan());
                row.createCell(2).setCellValue(ts.getViTri() == null ? "" : ts.getViTri());
                row.createCell(3).setCellValue(ts.getNgayMua() == null ? "" : ts.getNgayMua().toString());
                row.createCell(4).setCellValue(ts.getTinhTrang() == null ? "" : ts.getTinhTrang());
                row.createCell(5).setCellValue(
                        ts.getNgayBaoTriTiepTheo() == null ? "" : ts.getNgayBaoTriTiepTheo().toString());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất Excel danh sách tài sản", e);
        }
    }
}
