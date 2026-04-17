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
}