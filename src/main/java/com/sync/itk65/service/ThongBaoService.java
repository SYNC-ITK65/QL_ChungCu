package com.sync.itk65.service;

import com.sync.itk65.entity.ThongBao;
import org.springframework.data.domain.Page;
import java.util.List;

public interface ThongBaoService {
    List<ThongBao> getAllThongBao();

    Page<ThongBao> getAllThongBao(int page, int size);

    List<ThongBao> getThongBaoByLoai(Integer loai);

    ThongBao getThongBaoById(Long id);

    ThongBao saveThongBao(ThongBao thongBao);

    void deleteThongBao(Long id);
}
