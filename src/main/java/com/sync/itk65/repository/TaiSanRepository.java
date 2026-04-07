package com.sync.itk65.repository;

import com.sync.itk65.entity.TaiSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaiSanRepository extends JpaRepository<TaiSan, Long> {

    @Query("SELECT t FROM TaiSan t WHERE t.ngayBaoTriTiepTheo <= :date OR t.tinhTrang = 'Đang hỏng'")
    List<TaiSan> findAlertAssets(@Param("date") LocalDate date);

    List<TaiSan> findByTinhTrang(String tinhTrang);
}
