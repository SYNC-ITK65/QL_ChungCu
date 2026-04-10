package com.sync.itk65.repository;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.TamTruTamVang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TamTruTamVangRepository extends JpaRepository<TamTruTamVang, Long> {
    List<TamTruTamVang> findByCuDanOrderByIdDesc(CuDan cuDan);
    List<TamTruTamVang> findByTrangThaiDuyetAndNgayKetThucBefore(String trangThaiDuyet, LocalDate date);
}