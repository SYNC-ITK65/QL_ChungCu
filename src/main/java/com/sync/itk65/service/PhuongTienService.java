package com.sync.itk65.service;

import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.repository.PhuongTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        xe.setTrangThai("Đã duyệt");
        phuongTienRepository.save(xe);
    }

    //Xem danh sách bãi xe ---
    public List<PhuongTien> danhSachXe() {
        return phuongTienRepository.findAll();
    }

    //Hủy gửi xe ---
    public void huyGuiXe(Long id) {
        if (!phuongTienRepository.existsById(id)) {
            throw new RuntimeException("Lỗi: Không tìm thấy ID xe này để xóa!");
        }
        phuongTienRepository.deleteById(id);
    }
}