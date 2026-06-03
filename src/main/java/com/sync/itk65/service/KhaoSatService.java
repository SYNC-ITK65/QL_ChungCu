package com.sync.itk65.service;

import com.sync.itk65.entity.*;
import com.sync.itk65.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class KhaoSatService {

    @Autowired private KhaoSatRepository khaoSatRepo;
    @Autowired private LuaChonKhaoSatRepository luaChonRepo;
    @Autowired private LichSuVoteRepository lichSuVoteRepo;

    public List<KhaoSat> layTatCa() {
        return khaoSatRepo.findAllByOrderByThoiGianBatDauDesc();
    }
    public KhaoSat findById(Long id) {
        return khaoSatRepo.findById(id).orElse(null);
    }

    @Transactional
    public void luuKhaoSat(KhaoSat khaoSat) {
        if (khaoSat.getDanhSachLuaChon() != null) {
            for (LuaChonKhaoSat lc : khaoSat.getDanhSachLuaChon()) {
                lc.setKhaoSat(khaoSat); // Set khóa ngoại
                if (lc.getSoLuotBinhChon() == null) {
                    lc.setSoLuotBinhChon(0);
                }
            }
        }
        khaoSatRepo.save(khaoSat);
    }

    @Transactional
    public String xoaKhaoSat(Long id) {
        KhaoSat ks = findById(id);
        if (ks == null) return "Không tìm thấy!";
        if (ks.getThoiGianKetThuc().isBefore(LocalDateTime.now())) return "Không thể xóa khảo sát đã quá hạn!";
        if (ks.getTongSoVote() > 0) return "Không thể xóa vì đã có người bình chọn!";
        khaoSatRepo.delete(ks);
        return "SUCCESS";
    }

    @Transactional
    public void ketThucSom(Long id) {
        KhaoSat ks = findById(id);
        if (ks != null && ks.getTrangThaiHienTai().equals("ĐANG MỞ")) {
            ks.setThoiGianKetThuc(LocalDateTime.now());
            khaoSatRepo.save(ks);
        }
    }

    // Luồng Cư Dân Vote (Bảo mật 100%)
    @Transactional
    public String thucHienBinhChon(CuDan cuDan, Long khaoSatId, Long luaChonMoiId) {
        KhaoSat ks = findById(khaoSatId);
        LocalDateTime now = LocalDateTime.now();

        if (ks == null || !ks.getTrangThaiHienTai().equals("ĐANG MỞ")) {
            return "Khảo sát hiện không mở để bình chọn!";
        }

        LuaChonKhaoSat lcMoi = luaChonRepo.findById(luaChonMoiId).orElse(null);
        if (lcMoi == null || !lcMoi.getKhaoSat().getId().equals(khaoSatId)) return "Lựa chọn không hợp lệ!";

        Optional<LichSuVote> lichSuOpt = lichSuVoteRepo.findByCuDanAndKhaoSat(cuDan, ks);

        if (lichSuOpt.isEmpty()) {
            // Vote lần đầu
            lcMoi.setSoLuotBinhChon(lcMoi.getSoLuotBinhChon() + 1);
            LichSuVote ls = new LichSuVote();
            ls.setCuDan(cuDan); ls.setKhaoSat(ks); ls.setLuaChonDaNgan(lcMoi);
            lichSuVoteRepo.save(ls);
            return "SUCCESS_VOTE";
        } else {
            // Đổi vote
            LichSuVote ls = lichSuOpt.get();
            LuaChonKhaoSat lcCu = ls.getLuaChonDaNgan();
            if (lcCu.getId().equals(luaChonMoiId)) return "SUCCESS_NO_CHANGE"; // Không đổi ý

            lcCu.setSoLuotBinhChon(lcCu.getSoLuotBinhChon() - 1);
            lcMoi.setSoLuotBinhChon(lcMoi.getSoLuotBinhChon() + 1);
            ls.setLuaChonDaNgan(lcMoi); ls.setThoiGianVote(now);

            return "SUCCESS_UPDATE";
        }
    }
    public org.springframework.data.domain.Page<LichSuVote> timKiemLichSuVote(Long khaoSatId, Long luaChonId, String tuKhoa, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        String tuKhoaFilter = (tuKhoa != null && !tuKhoa.trim().isEmpty()) ? "%" + tuKhoa.trim() + "%" : null;
        return lichSuVoteRepo.timKiemVaPhanTrang(khaoSatId, luaChonId, tuKhoaFilter, pageable);
    }
    public org.springframework.data.domain.Page<KhaoSat> timKiemKhaoSat(String tuKhoa, Integer trangThai, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        String tuKhoaFilter = (tuKhoa != null && !tuKhoa.trim().isEmpty()) ? "%" + tuKhoa.trim() + "%" : null;

        // Lấy thời gian hiện tại để so sánh trạng thái Đang mở / Đã đóng
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        return khaoSatRepo.timKiemVaPhanTrang(tuKhoaFilter, trangThai, now, pageable);
    }

    public byte[] xuatExcelDanhSachKhaoSat(String tuKhoa, Integer trangThai) {
        // Lấy tất cả kết quả thỏa mãn điều kiện lọc
        org.springframework.data.domain.Page<KhaoSat> trangDuLieu = timKiemKhaoSat(tuKhoa, trangThai, 0, Integer.MAX_VALUE);
        List<KhaoSat> danhSach = trangDuLieu.getContent();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DanhSachKhaoSat");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = {"ID", "Tiêu đề", "Mô tả", "Thời gian bắt đầu", "Thời gian kết thúc", "Trạng thái", "Tổng số vote"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (KhaoSat ks : danhSach) {
                var row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(ks.getId());
                row.createCell(1).setCellValue(ks.getTieuDe() == null ? "" : ks.getTieuDe());
                row.createCell(2).setCellValue(ks.getMoTa() == null ? "" : ks.getMoTa());
                row.createCell(3).setCellValue(ks.getThoiGianBatDau() == null ? "" : ks.getThoiGianBatDau().format(formatter));
                row.createCell(4).setCellValue(ks.getThoiGianKetThuc() == null ? "" : ks.getThoiGianKetThuc().format(formatter));
                row.createCell(5).setCellValue(ks.getTrangThaiHienTai() == null ? "" : ks.getTrangThaiHienTai());
                row.createCell(6).setCellValue(ks.getTongSoVote());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file Excel danh sách khảo sát", e);
        }
    }

    public byte[] xuatExcelLichSuVote(Long khaoSatId, Long luaChonId, String tuKhoa) {
        // Lấy tất cả kết quả thỏa mãn
        org.springframework.data.domain.Page<LichSuVote> trangDuLieu = timKiemLichSuVote(khaoSatId, luaChonId, tuKhoa, 0, Integer.MAX_VALUE);
        List<LichSuVote> danhSach = trangDuLieu.getContent();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("LichSuBinhChon");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = {"Cư dân", "Căn hộ", "Phương án đã chọn", "Thời gian bình chọn"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (LichSuVote ls : danhSach) {
                var row = sheet.createRow(rowIdx++);
                String tenCuDan = ls.getCuDan() != null ? ls.getCuDan().getHoTen() : "";
                String maCanHo = (ls.getCuDan() != null && ls.getCuDan().getCanHo() != null) ? ls.getCuDan().getCanHo().getMaCanHo() : "";
                String phuongAn = ls.getLuaChonDaNgan() != null ? ls.getLuaChonDaNgan().getNoiDungLuaChon() : "";
                String thoiGian = ls.getThoiGianVote() != null ? ls.getThoiGianVote().format(formatter) : "";

                row.createCell(0).setCellValue(tenCuDan);
                row.createCell(1).setCellValue(maCanHo);
                row.createCell(2).setCellValue(phuongAn);
                row.createCell(3).setCellValue(thoiGian);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file Excel lịch sử vote", e);
        }
    }
}