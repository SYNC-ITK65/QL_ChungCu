package com.sync.itk65.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sync.itk65.entity.LichSuBaoTri;
import com.sync.itk65.entity.TaiSan;
import com.sync.itk65.repository.LichSuBaoTriRepository;
import com.sync.itk65.repository.TaiSanRepository;

@Service
public class LichSuBaoTriService {

    @Autowired
    private LichSuBaoTriRepository lichSuBaoTriRepository;

    @Autowired
    private TaiSanRepository taiSanRepository;

    public List<LichSuBaoTri> getHistoryByTaiSanId(Long taiSanId) {
        return lichSuBaoTriRepository.findByTaiSanIdOrderByNgayBaoTriDesc(taiSanId);
    }

    public Page<LichSuBaoTri> getHistoryByTaiSanId(Long taiSanId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichSuBaoTriRepository.findByTaiSanIdOrderByNgayBaoTriDesc(taiSanId, pageable);
    }

    @Transactional
    public LichSuBaoTri saveHistory(LichSuBaoTri history) {
        LichSuBaoTri saved = lichSuBaoTriRepository.save(history);

        // Trigger: Update TaiSan
        TaiSan taiSan = history.getTaiSan();
        if (taiSan != null) {
            // Update next maintenance date: ngayBaoTri (vừa nhập) + chuKyBaoTri (số tháng)
            if (taiSan.getChuKyBaoTri() != null) {
                taiSan.setNgayBaoTriTiepTheo(history.getNgayBaoTri().plusMonths(taiSan.getChuKyBaoTri()));
            }
            // Reset status
            taiSan.setTinhTrang("Hoạt động tốt");
            taiSanRepository.save(taiSan);
        }

        return saved;
    }
}
