package com.sync.itk65.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.sync.itk65.entity.ThongBao;

public interface ThongBaoService {
    List<ThongBao> getAllThongBao();

    Page<ThongBao> getAllThongBao(int page, int size);

    Page<ThongBao> searchThongBao(String tuKhoa, Integer loai, int page, int size);

    List<ThongBao> getThongBaoByLoai(Integer loai);

    ThongBao getThongBaoById(Long id);

    ThongBao saveThongBao(ThongBao thongBao);

    void deleteThongBao(Long id);

    /**
     * Lọc thông báo theo đối tượng cư dân (maCanHo, tang) có phân trang.
     * Nếu maCanHo = null → chỉ lấy thông báo ALL.
     */
    Page<ThongBao> locThongBaoTheoCuDan(Integer loai, String maCanHo, String tang, int page, int size);
}
