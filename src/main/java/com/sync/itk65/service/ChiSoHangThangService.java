package com.sync.itk65.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sync.itk65.entity.ChiSoHangThang;
import com.sync.itk65.repository.ChiSoHangThangRepository;

@Service
public class ChiSoHangThangService {

    @Autowired
    private ChiSoHangThangRepository chiSoHangThangRepository;

    // Lấy danh sách toàn bộ chỉ số, sắp xếp ngày mới nhất lên đầu
    public List<ChiSoHangThang> layTatCaChiSo() {
        return chiSoHangThangRepository.findAllOrderByNgayGhiNhanDesc();
    }

    public Page<ChiSoHangThang> layTatCaChiSo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chiSoHangThangRepository.findAll(pageable);
    }

    // Tìm kiếm chỉ số theo nhiều điều kiện
    public List<ChiSoHangThang> timKiemChiSo(String maCanHo, Long canHoId, Integer thang, Integer nam) {
        return chiSoHangThangRepository.searchWithFilters(maCanHo, canHoId, thang, nam);
    }

    // Lấy chỉ số theo ID căn hộ
    public List<ChiSoHangThang> layChiSoTheoCanHo(Long canHoId) {
        return chiSoHangThangRepository.findByCanHoId(canHoId);
    }

    // Lưu chỉ số mới
    public ChiSoHangThang luuChiSo(ChiSoHangThang chiSoMoi) {
        // 1. Tự động gán ngày nếu chưa có
        if (chiSoMoi.getNgayGhiNhan() == null) {
            chiSoMoi.setNgayGhiNhan(LocalDate.now());
        }

        int thang = chiSoMoi.getNgayGhiNhan().getMonthValue();
        int nam = chiSoMoi.getNgayGhiNhan().getYear();

        // 2. Kiểm tra xem Căn hộ này trong Tháng/Năm đó đã có chỉ số chưa
        Optional<ChiSoHangThang> chiSoCu = chiSoHangThangRepository
                .findByCanHoAndThangNam(chiSoMoi.getCanHo().getId(), thang, nam);
        if (chiSoCu.isPresent()) {
            // ĐÃ CÓ DỮ LIỆU -> Ghi đè (Update) số mới lên bản ghi cũ
            ChiSoHangThang cs = chiSoCu.get();
            cs.setDienTieuThu(chiSoMoi.getDienTieuThu());
            cs.setNuocTieuThu(chiSoMoi.getNuocTieuThu());
            cs.setNgayGhiNhan(chiSoMoi.getNgayGhiNhan());

            return chiSoHangThangRepository.save(cs);
        }

        // CHƯA CÓ DỮ LIỆU -> Lưu mới hoàn toàn (Insert)
        return chiSoHangThangRepository.save(chiSoMoi);
    }

    // Xóa chỉ số
    public void xoaChiSo(Long id) {
        chiSoHangThangRepository.deleteById(id);
    }
}