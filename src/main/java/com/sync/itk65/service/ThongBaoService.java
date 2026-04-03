package com.sync.itk65.service;

import com.sync.itk65.entity.ThongBao;
import java.util.List;

public interface ThongBaoService {
    List<ThongBao> getAllThongBao();

    List<ThongBao> getThongBaoByLoai(Integer loai);

    ThongBao getThongBaoById(Long id);

    ThongBao saveThongBao(ThongBao thongBao);

    void deleteThongBao(Long id);
}
