package com.sync.itk65.service;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.repository.CuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuDanService {

    @Autowired
    private CuDanRepository cuDanRepository;

    // 1. Lấy danh sách tất cả cư dân
    public List<CuDan> layTatCaCuDan() {
        return cuDanRepository.findAll();
    }

    // 2. Lưu thông tin cư dân (Đã kế thừa từ Người Dùng)
    public void luuCuDan(CuDan cuDan) {
        cuDanRepository.save(cuDan);
    }

    // 3. Lấy thông tin cư dân theo ID (ID này trùng với ID Người dùng)
    public CuDan layCuDanTheoId(Long id) {
        return cuDanRepository.findById(id).orElse(null);
    }

    // 4. Xóa cư dân
    public void xoaCuDan(Long id) {
        cuDanRepository.deleteById(id);
    }

    // 5. Lấy danh sách cư dân đang ở trong một căn hộ cụ thể
    public List<CuDan> timTheoCanHo(Long canHoId) {
        return cuDanRepository.findByCanHo_Id(canHoId);
    }

}
