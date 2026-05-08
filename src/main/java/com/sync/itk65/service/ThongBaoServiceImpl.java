package com.sync.itk65.service;

import com.sync.itk65.entity.ThongBao;
import com.sync.itk65.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongBaoServiceImpl implements ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    @Override
    public List<ThongBao> getAllThongBao() {
        return thongBaoRepository.findAllByOrderByNgayDangDesc();
    }

    @Override
    public Page<ThongBao> getAllThongBao(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return thongBaoRepository.findAllByOrderByNgayDangDesc(pageable);
    }

    @Override
    public Page<ThongBao> searchThongBao(String tuKhoa, Integer loai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return thongBaoRepository.searchThongBao(tuKhoa, loai, pageable);
    }

    @Override
    public List<ThongBao> getThongBaoByLoai(Integer loai) {
        return thongBaoRepository.findByLoaiOrderByNgayDangDesc(loai);
    }

    @Override
    public ThongBao getThongBaoById(Long id) {
        return thongBaoRepository.findById(id).orElse(null);
    }

    @Override
    public ThongBao saveThongBao(ThongBao thongBao) {
        return thongBaoRepository.save(thongBao);
    }

    @Override
    public void deleteThongBao(Long id) {
        thongBaoRepository.deleteById(id);
    }

    @Override
    public Page<ThongBao> locThongBaoTheoCuDan(Integer loai, String maCanHo, String tang, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Nếu cư dân chưa có căn hộ (maCanHo = null), chỉ lấy thông báo gửi ALL
        if (maCanHo == null || maCanHo.isEmpty()) {
            return thongBaoRepository.locThongBaoChiAll(loai, pageable);
        }

        // Nếu có căn hộ, lọc theo maCanHo và tầng
        String tangStr = (tang != null) ? tang : "";
        return thongBaoRepository.locThongBaoTheoCuDan(loai, maCanHo, tangStr, pageable);
    }
}
