package com.sync.itk65.service;

import com.sync.itk65.entity.DatDichVu;
import com.sync.itk65.repository.DatDichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 2. Lấy danh sách tất cả các dịch vụ đã được đặt (Dành cho Admin quản lý)
    public List<DatDichVu> layTatCaDonDatDichVu() {
        return datDichVuRepository.findAll();
    }

    // 3. Xóa/Hủy đặt dịch vụ
    public void huyDatDichVu(Long id) {
        datDichVuRepository.deleteById(id);
    }
    public List<DatDichVu> layLichSuDatCuaCuDan(Long cuDanId) {
        // Đổi thành gọi hàm mới
        return datDichVuRepository.layLichSuDatDichVu(cuDanId);
    }
    public DatDichVu findById(Long id) {
        return datDichVuRepository.findById(id).orElse(null);
    }

    public void luu(DatDichVu datDichVu) {
        datDichVuRepository.save(datDichVu);
    }
}