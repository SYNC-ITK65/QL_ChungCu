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
        // Mặc định ngày đặt là hôm nay
        datDichVu.setNgayDat(LocalDate.now());
        // Mặc định trạng thái là đã đăng ký thành công (hoặc "Đã sử dụng" để tính tiền)
        datDichVu.setTrangThai("Đã sử dụng");

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
}