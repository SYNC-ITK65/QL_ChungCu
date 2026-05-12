package com.sync.itk65.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.repository.CuDanRepository;

import jakarta.validation.Valid;

@Service
@Validated
public class CuDanService {

    @Autowired
    private CuDanRepository cuDanRepository;

    // 1. Lấy danh sách tất cả cư dân
    public List<CuDan> layTatCaCuDan() {
        return cuDanRepository.findAll();
    }

    // 2. Lưu thông tin cư dân kèm kiểm tra logic nghiệp vụ Validator
    public void luuCuDan(@Valid CuDan cuDan) {
        try {
            // Kiểm tra trùng lặp Căn Cước Công Dân
            List<CuDan> tatCaCuDan = layTatCaCuDan();
            for (CuDan item : tatCaCuDan) {
                if (item.getCccd() != null && item.getCccd().equals(cuDan.getCccd())) {
                    if (cuDan.getId() == null || !item.getId().equals(cuDan.getId())) {
                        throw new IllegalArgumentException(
                                "Căn cước công dân: '" + cuDan.getCccd() + "' đã được sử dụng!");
                    }
                }
            }
            cuDanRepository.save(cuDan);
        } catch (IllegalArgumentException e) {
            // Trả lỗi cụ thể về phía Validator
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Có lỗi hệ thống xảy ra khi lưu Cư dân. Xin vui lòng thử lại sau!");
        }
    }

    // 3. Lấy thông tin cư dân theo ID (ID này trùng với ID Người dùng)
    public CuDan layCuDanTheoId(Long id) {
        return cuDanRepository.findById(id).orElse(null);
    }

    // 4. Xóa cư dân
    public void xoaCuDan(Long id) {
        cuDanRepository.deleteById(id);
    }

    // Hàm tìm kiếm cư dân với các tiêu chí và phân trang dữ liệu
    public Page<CuDan> timKiemCuDan(String tuKhoa, String trangThai, int page, int size) {
        // Cấu hình đối tượng Pageable với trang hiện tại và số lượng phần tử trên mỗi
        // trang
        Pageable pageable = PageRequest.of(page, size);
        return cuDanRepository.timKiemCuDan(tuKhoa, trangThai, pageable);
    }

    // Hàm tìm kiếm cư dân theo ID Căn hộ cụ thể, kết hợp tìm kiếm từ khóa và phân
    // trang
    public Page<CuDan> timKiemTheoCanHo(Long canHoId, String tuKhoa, String trangThai, int page, int size) {
        // Tạo biến phân trang để hỗ trợ truy vấn cơ sở dữ liệu theo từng phần nhỏ
        Pageable pageable = PageRequest.of(page, size);
        return cuDanRepository.layDanhSachCuDanTheoCanHo(canHoId, tuKhoa, trangThai, pageable);
    }

    // Hàm cũ dùng xuất excel
    public List<CuDan> timTheoCanHoThongThuong(Long canHoId) {
        return cuDanRepository.layTatCaCuDanTheoCanHo(canHoId);
    }

    public byte[] xuatExcelDanhSachCuDan(Long canHoId) {
        List<CuDan> danhSachCuDan = (canHoId == null) ? layTatCaCuDan() : timTheoCanHoThongThuong(canHoId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CuDan");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "ID", "Họ tên", "Số điện thoại", "Căn hộ", "Mối quan hệ", "Trạng thái" };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (CuDan cuDanItem : danhSachCuDan) {
                var row = sheet.createRow(rowIdx++);
                if (cuDanItem.getId() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(cuDanItem.getId().doubleValue());
                }
                row.createCell(1).setCellValue(cuDanItem.getHoTen() == null ? "" : cuDanItem.getHoTen());
                row.createCell(2).setCellValue(cuDanItem.getSoDienThoai() == null ? "" : cuDanItem.getSoDienThoai());
                row.createCell(3).setCellValue(
                        (cuDanItem.getCanHo() != null && cuDanItem.getCanHo().getMaCanHo() != null)
                                ? cuDanItem.getCanHo().getMaCanHo()
                                : "");
                row.createCell(4).setCellValue(cuDanItem.getMoiQuanHe() == null ? "" : cuDanItem.getMoiQuanHe());
                row.createCell(5).setCellValue(cuDanItem.getTrangThai() == null ? "" : cuDanItem.getTrangThai());
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
