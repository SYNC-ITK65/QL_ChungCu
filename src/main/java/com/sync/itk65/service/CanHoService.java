package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.repository.CanHoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanHoService {

    @Autowired
    private CanHoRepository canHoRepository;

    // Hàm lấy danh sách tất cả căn hộ
    public List<CanHo> layTatCaCanHo() {
        return canHoRepository.findAll();
    }

    // Hàm lưu căn hộ mới
    public void luuCanHo(CanHo canHo) {
        canHoRepository.save(canHo);
    }
}