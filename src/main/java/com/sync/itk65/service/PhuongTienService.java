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
            throw new RuntimeException("Lỗi: Biển số xe " + xe.getBienSoXe() + " đã có trong bãi!");
        }
        return phuongTienRepository.save(xe);
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