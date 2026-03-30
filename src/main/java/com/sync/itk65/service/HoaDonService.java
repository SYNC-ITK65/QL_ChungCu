package com.sync.itk65.service;

import com.sync.itk65.entity.HoaDon;
import com.sync.itk65.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // Lấy danh sách toàn bộ hóa đơn
    public List<HoaDon> layTatCaHoaDon() {
        return hoaDonRepository.findAll();
    }

    // Tạo hóa đơn mới
    public void luuHoaDon(HoaDon hoaDon) {
        // Tạm thời set trạng thái mặc định
        if(hoaDon.getTrangThaiThanhToan() == null) {
            hoaDon.setTrangThaiThanhToan("Chưa đóng");
        }
        hoaDonRepository.save(hoaDon);
    }
}