package com.sync.itk65.service;

import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.repository.PhuongTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PhuongTienService {

    @Autowired
    private PhuongTienRepository phuongTienRepository;

    //  Đăng ký xe mới
    public PhuongTien dangKyXe(PhuongTien xe) {
        if (phuongTienRepository.existsByBienSoXe(xe.getBienSoXe())) {
            throw new RuntimeException("Biển số xe đã tồn tại!");
        }
        // Nếu trangThai chưa có (thường là từ form cư dân), mặc định là Chờ duyệt
        if (xe.getTrangThai() == null) {
            xe.setTrangThai("Chờ duyệt");
        }
        return phuongTienRepository.save(xe);
    }

    // 2. Hàm DUYỆT XE (Quan trọng - bạn đang thiếu cái này)
    public void duyetXe(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        // Kiểm tra giới hạn hầm xe có 1000 xe
        long soLuongXeDaDuyet = phuongTienRepository.countByTrangThai("Đã duyệt");
        if (soLuongXeDaDuyet >= 1000) {
            throw new RuntimeException("Hầm xe đã đầy (giới hạn tối đa 1000 xe)! Không thể duyệt thêm phương tiện.");
        }
        xe.setTrangThai("Đã duyệt");
        phuongTienRepository.save(xe);
    }

    public void tuChoiXe(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Từ chối");
        phuongTienRepository.save(xe);
    }

    public void huyDuyetXe(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Hủy duyệt");
        phuongTienRepository.save(xe);
    }

    public void suaLaiTrangThaiChoDuyet(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Chờ duyệt");
        phuongTienRepository.save(xe);
    }

    public long getSoLuongXeDaDuyet() {
        return phuongTienRepository.countByTrangThai("Đã duyệt");
    }

    public long getSoLuongXeChoDuyet() {
        return phuongTienRepository.countByTrangThai("Chờ duyệt");
    }

    public long getTongSoXe() {
        return phuongTienRepository.count();
    }

    //Xem danh sách bãi xe ---
    public Page<PhuongTien> danhSachXe(int page, int size) {
        Page<PhuongTien> ds = phuongTienRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));
        ds.forEach(x -> x.setTrangThai(chuanHoaTrangThai(x.getTrangThai())));
        return ds;
    }

    public List<PhuongTien> danhSachXe() {
        List<PhuongTien> ds = phuongTienRepository.findAll();
        ds.forEach(x -> x.setTrangThai(chuanHoaTrangThai(x.getTrangThai())));
        return ds;
    }

    public List<PhuongTien> layXeTheoCanHoId(Long canHoId) {
        List<PhuongTien> ds = phuongTienRepository.findByCanHoId(canHoId);
        ds.forEach(x -> x.setTrangThai(chuanHoaTrangThai(x.getTrangThai())));
        return ds;
    }

    //Hủy gửi xe ---
    public void huyGuiXe(Long id) {
        if (!phuongTienRepository.existsById(id)) {
            throw new RuntimeException("Lỗi: Không tìm thấy ID xe này để xóa!");
        }
        phuongTienRepository.deleteById(id);
    }

    private String chuanHoaTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isBlank()) return "Chờ duyệt";
        String raw = trangThai.trim();
        String normalized = boDau(raw).toLowerCase();
        if (normalized.contains("huy") && normalized.contains("duyet")) return "Hủy duyệt";
        if (normalized.contains("tu choi")) return "Từ chối";
        if ((normalized.contains("cho") && normalized.contains("duyet")) || normalized.contains("dang ky moi")) return "Chờ duyệt";
        if (normalized.contains("da") && normalized.contains("duyet")) return "Đã duyệt";
        return raw;
    }

    private String boDau(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.replace('đ', 'd').replace('Đ', 'D');
    }
    public Page<PhuongTien> timKiemPhuongTien(String tuKhoa, String trangThai, String loaiXe, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String tuKhoaFilter = (tuKhoa != null && !tuKhoa.trim().isEmpty()) ? "%" + tuKhoa.trim() + "%" : null;
        String trangThaiFilter = (trangThai != null && !trangThai.trim().isEmpty()) ? trangThai.trim() : null;
        String loaiXeFilter = (loaiXe != null && !loaiXe.trim().isEmpty()) ? loaiXe.trim() : null;
        return phuongTienRepository.timKiemVaLocPhuongTien(tuKhoaFilter, trangThaiFilter, loaiXeFilter, pageable);
    }

    public byte[] xuatExcelDanhSachPhuongTien(String tuKhoa, String trangThai, String loaiXe) {
        Page<PhuongTien> trangDuLieu;
        if ((tuKhoa != null && !tuKhoa.trim().isEmpty()) ||
                (trangThai != null && !trangThai.trim().isEmpty()) ||
                (loaiXe != null && !loaiXe.trim().isEmpty())) {
            trangDuLieu = timKiemPhuongTien(tuKhoa, trangThai, loaiXe, 0, Integer.MAX_VALUE);
        } else {
            trangDuLieu = danhSachXe(0, Integer.MAX_VALUE);
        }

        List<PhuongTien> ds = trangDuLieu.getContent();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DanhSachPhuongTien");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = {"ID Xe", "Mã Căn Hộ", "Biển Số", "Loại Xe", "Màu Xe", "Trạng Thái"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (PhuongTien xe : ds) {
                var row = sheet.createRow(rowIdx++);
                String maCanHo = xe.getCanHo() != null ? String.valueOf(xe.getCanHo().getId()) : "Trống";
                
                row.createCell(0).setCellValue(xe.getId());
                row.createCell(1).setCellValue(maCanHo);
                row.createCell(2).setCellValue(xe.getBienSoXe() != null ? xe.getBienSoXe() : "");
                row.createCell(3).setCellValue(xe.getLoaiXe() != null ? xe.getLoaiXe() : "");
                row.createCell(4).setCellValue(xe.getMauXe() != null ? xe.getMauXe() : "");
                
                String tt = xe.getTrangThai() == null || xe.getTrangThai().isEmpty() ? "Chờ duyệt" : xe.getTrangThai();
                row.createCell(5).setCellValue(tt);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file Excel danh sách phương tiện", e);
        }
    }
}