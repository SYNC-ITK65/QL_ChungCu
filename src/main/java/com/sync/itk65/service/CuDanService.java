package com.sync.itk65.service;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.repository.CuDanRepository;
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
import java.util.List;

@Service
public class CuDanService {

    @Autowired
    private CuDanRepository cuDanRepository;

    // 1. Lấy danh sách tất cả cư dân
    public List<CuDan> layTatCaCuDan() {
        return cuDanRepository.findAll();
    }

    // 2. Lưu thông tin cư dân (Đã kế thừa từ Người Dùng)
    public void luuCuDan(CuDan cuDan) {
        cuDanRepository.save(cuDan);
    }

    // 3. Lấy thông tin cư dân theo ID (ID này trùng với ID Người dùng)
    public CuDan layCuDanTheoId(Long id) {
        return cuDanRepository.findById(id).orElse(null);
    }

    // 4. Xóa cư dân
    public void xoaCuDan(Long id) {
        cuDanRepository.deleteById(id);
    }

    // Sửa các hàm hỗ trợ tìm kiếm phân trang
    public Page<CuDan> timKiemCuDan(String tuKhoa, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuDanRepository.timKiemCuDan(tuKhoa, trangThai, pageable);
    }

    public Page<CuDan> timKiemTheoCanHo(Long canHoId, String tuKhoa, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuDanRepository.layDanhSachCuDanTheoCanHo(canHoId, tuKhoa, trangThai, pageable);
    }

    // Hàm cũ dùng xuất excel
    public List<CuDan> timTheoCanHoThongThuong(Long canHoId) {
        return cuDanRepository.layTatCaCuDanTheoCanHo(canHoId);
    }

    public byte[] xuatExcelDanhSachCuDan(Long canHoId) {
        List<CuDan> danhSach = (canHoId == null) ? layTatCaCuDan() : timTheoCanHoThongThuong(canHoId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CuDan");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "ID", "Họ tên", "Số điện thoại", "Căn hộ", "Mối quan hệ", "Trạng thái" };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (CuDan cd : danhSach) {
                var row = sheet.createRow(rowIdx++);
                if (cd.getId() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(cd.getId().doubleValue());
                }
                row.createCell(1).setCellValue(cd.getHoTen() == null ? "" : cd.getHoTen());
                row.createCell(2).setCellValue(cd.getSoDienThoai() == null ? "" : cd.getSoDienThoai());
                row.createCell(3).setCellValue(
                        (cd.getCanHo() != null && cd.getCanHo().getMaCanHo() != null) ? cd.getCanHo().getMaCanHo()
                                : "");
                row.createCell(4).setCellValue(cd.getMoiQuanHe() == null ? "" : cd.getMoiQuanHe());
                row.createCell(5).setCellValue(cd.getTrangThai() == null ? "" : cd.getTrangThai());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất Excel danh sách cư dân", e);
        }
    }

}
