package com.sync.itk65.service;

import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.repository.DatDichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class DatDichVuService {

    @Autowired
    private DatDichVuRepository datDichVuRepository;

    // 1. Dành cho Cư dân: Đăng ký một dịch vụ mới (Hồ bơi, BBQ...)
    public void dangKyDichVu(DatDichVu datDichVu) {
        // Nếu ở Controller chưa đặt ngày thì mới đặt là hôm nay
        if (datDichVu.getNgayDat() == null) {
            datDichVu.setNgayDat(LocalDate.now());
        }
        // Chỉ đặt mặc định nếu ở Controller chưa truyền trạng thái vào
        if (datDichVu.getTrangThai() == null) {
            datDichVu.setTrangThai("Chờ duyệt");
        }

        datDichVuRepository.save(datDichVu);
    }

    public void duyetDonDatDichVu(Long id) {
        DatDichVu donDat = datDichVuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt dịch vụ ID: " + id));
        donDat.setTrangThai("Đã duyệt");
        datDichVuRepository.save(donDat);
    }

    public void tuChoiDonDatDichVu(Long id) {
        DatDichVu donDat = datDichVuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt dịch vụ ID: " + id));
        donDat.setTrangThai("Từ chối");
        datDichVuRepository.save(donDat);
    }

    public void huyDuyetDonDatDichVu(Long id) {
        DatDichVu donDat = datDichVuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt dịch vụ ID: " + id));
        donDat.setTrangThai("Hủy duyệt");
        datDichVuRepository.save(donDat);
    }

    public void suaLaiTrangThaiChoDuyet(Long id) {
        DatDichVu donDat = datDichVuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt dịch vụ ID: " + id));
        donDat.setTrangThai("Chờ duyệt");
        datDichVuRepository.save(donDat);
    }

    // 2. Lấy danh sách tất cả các dịch vụ đã được đặt (Dành cho Admin quản lý)
    public Page<DatDichVu> layTatCaDonDatDichVu(int page, int size) {
        Page<DatDichVu> ds = datDichVuRepository.findAllByOrderByNgayDatDesc(PageRequest.of(page, size));
        ds.forEach(d -> d.setTrangThai(chuanHoaTrangThai(d.getTrangThai())));
        return ds;
    }

    public Page<DatDichVu> timKiemDonDatDichVu(String tuKhoa, String trangThai, int page, int size) {
        String tuKhoaParam = (tuKhoa != null && !tuKhoa.trim().isEmpty()) ? tuKhoa.trim() : null;
        String trangThaiParam = (trangThai != null && !trangThai.trim().isEmpty()) ? trangThai.trim() : null;
        Page<DatDichVu> ds = datDichVuRepository.timKiemVaLocDonDatDichVu(tuKhoaParam, trangThaiParam, PageRequest.of(page, size));
        ds.forEach(d -> d.setTrangThai(chuanHoaTrangThai(d.getTrangThai())));
        return ds;
    }

    public List<DatDichVu> layTatCaDonDatDichVu() {
        List<DatDichVu> ds = datDichVuRepository.findAll();
        ds.forEach(d -> d.setTrangThai(chuanHoaTrangThai(d.getTrangThai())));
        return ds;
    }

    // 3. Xóa/Hủy đặt dịch vụ
    public void huyDatDichVu(Long id) {
        datDichVuRepository.deleteById(id);
    }
    public List<DatDichVu> layLichSuDatCuaCuDan(Long cuDanId) {
        List<DatDichVu> ds = datDichVuRepository.layLichSuDatDichVu(cuDanId);
        ds.forEach(d -> d.setTrangThai(chuanHoaTrangThai(d.getTrangThai())));
        return ds;
    }

    private String chuanHoaTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isBlank()) return "Chờ duyệt";
        String raw = trangThai.trim();
        String normalized = boDau(raw).toLowerCase();
        if (normalized.contains("huy") && normalized.contains("duyet")) return "Hủy duyệt";
        if (normalized.contains("tu choi")) return "Từ chối";
        if ((normalized.contains("cho") && normalized.contains("duyet")) || normalized.contains("dang ky moi")) return "Chờ duyệt";
        if (normalized.contains("da") && normalized.contains("duyet")) return "Đã duyệt";
        if (normalized.contains("da su dung")) return "Đã sử dụng";
        if (normalized.contains("hoan thanh")) return "Đã hoàn thành";
        if (normalized.contains("da huy")) return "Đã hủy";
        return raw;
    }

    private String boDau(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.replace('đ', 'd').replace('Đ', 'D');
    }
    public DatDichVu findById(Long id) {
        return datDichVuRepository.findById(id).orElse(null);
    }

    public void luu(DatDichVu datDichVu) {
        datDichVuRepository.save(datDichVu);
    }

    public byte[] xuatExcelDonDatDichVu(String tuKhoa, String trangThai) {
        Page<DatDichVu> trangDuLieu;
        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) || (trangThai != null && !trangThai.trim().isEmpty())) {
            trangDuLieu = timKiemDonDatDichVu(tuKhoa, trangThai, 0, Integer.MAX_VALUE);
        } else {
            trangDuLieu = layTatCaDonDatDichVu(0, Integer.MAX_VALUE);
        }

        List<DatDichVu> danhSach = trangDuLieu.getContent();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DanhSachDangKyDichVu");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = {"ID", "Cư Dân", "Mã Căn Hộ", "Dịch Vụ", "Ngày Đăng Ký", "Ghi Chú", "Trạng Thái", "Thời Gian Duyệt"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dtfDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (DatDichVu d : danhSach) {
                var row = sheet.createRow(rowIdx++);
                
                String tenCuDan = d.getCuDan() != null ? d.getCuDan().getHoTen() : "";
                String maCanHo = (d.getCuDan() != null && d.getCuDan().getCanHo() != null) ? String.valueOf(d.getCuDan().getCanHo().getId()) : "Trống";
                String tenDichVu = d.getDichVu() != null ? d.getDichVu().getTen() : "";
                String ngayDat = d.getNgayDat() != null ? d.getNgayDat().format(dtfDate) : "";
                String tgDuyet = d.getThoiGianDuyet() != null ? d.getThoiGianDuyet().format(dtfDateTime) : "";

                row.createCell(0).setCellValue(d.getId());
                row.createCell(1).setCellValue(tenCuDan);
                row.createCell(2).setCellValue(maCanHo);
                row.createCell(3).setCellValue(tenDichVu);
                row.createCell(4).setCellValue(ngayDat);
                row.createCell(5).setCellValue(d.getGhiChu() != null ? d.getGhiChu() : "");
                row.createCell(6).setCellValue(d.getTrangThai() != null ? d.getTrangThai() : "Chờ duyệt");
                row.createCell(7).setCellValue(tgDuyet);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file Excel danh sách đăng ký dịch vụ", e);
        }
    }
}