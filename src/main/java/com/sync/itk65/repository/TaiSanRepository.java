package com.sync.itk65.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.TaiSan;

@Repository
public interface TaiSanRepository extends JpaRepository<TaiSan, Long> {

    @Query("SELECT t FROM TaiSan t WHERE t.ngayBaoTriTiepTheo <= :date OR t.tinhTrang = 'Đang hỏng'")
    List<TaiSan> findAlertAssets(@Param("date") LocalDate date);

    @Query("SELECT t FROM TaiSan t WHERE t.ngayBaoTriTiepTheo <= :date OR t.tinhTrang = 'Đang hỏng'")
    Page<TaiSan> findAlertAssets(@Param("date") LocalDate date, Pageable pageable);

    List<TaiSan> findByTinhTrang(String tinhTrang);

    @Query("SELECT t FROM TaiSan t WHERE " +
            "(:tuKhoa IS NULL OR LOWER(t.maTaiSan) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR LOWER(t.tenTaiSan) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))) AND "
            +
            "(:tinhTrang IS NULL OR :tinhTrang = '' OR t.tinhTrang = :tinhTrang)")
    Page<TaiSan> searchTaiSan(@Param("tuKhoa") String tuKhoa, @Param("tinhTrang") String tinhTrang, Pageable pageable);
}
