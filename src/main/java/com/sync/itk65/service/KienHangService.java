package com.sync.itk65.service;

import com.sync.itk65.entity.KienHang;
import com.sync.itk65.repository.KienHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class KienHangService {

    @Autowired
    private KienHangRepository kienHangRepository;

    public List<KienHang> layTatCaKienHang() {
        return kienHangRepository.findAll();
    }

    public List<KienHang> layKienHangTheoCanHo(Long canHoId) {
        return kienHangRepository.findByCanHoId(canHoId);
    }

    public KienHang luuKienHang(KienHang kienHang) {
        if (kienHang.getNgayNhan() == null) {
            kienHang.setNgayNhan(LocalDate.now());
        }
        if (kienHang.getTrangThai() == null) {
            kienHang.setTrangThai("Chờ nhận");
        }
        return kienHangRepository.save(kienHang);
    }

    public void xacNhanDaNhan(Long id) {
        KienHang kienHang = kienHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kiện hàng!"));
        kienHang.setTrangThai("Đã nhận");
        kienHangRepository.save(kienHang);
    }

    public KienHang layTheoId(Long id) {
        return kienHangRepository.findById(id).orElse(null);
    }
}
