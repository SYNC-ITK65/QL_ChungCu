package com.sync.itk65.service;

import com.sync.itk65.entity.TaiSan;
import com.sync.itk65.repository.TaiSanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaiSanService {

    @Autowired
    private TaiSanRepository taiSanRepository;

    public List<TaiSan> getAllTaiSan() {
        return taiSanRepository.findAll();
    }

    public List<TaiSan> getAlertAssets() {
        return taiSanRepository.findAlertAssets(LocalDate.now().plusDays(7));
    }

    public Optional<TaiSan> getTaiSanById(Long id) {
        return taiSanRepository.findById(id);
    }

    public TaiSan saveTaiSan(TaiSan taiSan) {
        // Initial calculation of ngayBaoTriTiepTheo if not set
        if (taiSan.getId() == null && taiSan.getNgayBaoTriTiepTheo() == null
                && taiSan.getNgayMua() != null && taiSan.getChuKyBaoTri() != null) {
            taiSan.setNgayBaoTriTiepTheo(taiSan.getNgayMua().plusMonths(taiSan.getChuKyBaoTri()));
        }
        return taiSanRepository.save(taiSan);
    }

    public void deleteTaiSan(Long id) {
        taiSanRepository.deleteById(id);
    }

    public long countAlertAssets() {
        return getAlertAssets().size();
    }
}
