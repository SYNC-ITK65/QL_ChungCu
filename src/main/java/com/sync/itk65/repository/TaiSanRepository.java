package com.sync.itk65.repository;

import com.sync.itk65.entity.TaiSan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaiSanRepository extends JpaRepository<TaiSan, Long> {

    @Query(value = "SELECT * FROM tai_san WHERE ngay_bao_tri_tiep_theo <= :date OR tinh_trang = 'Đang hỏng'", nativeQuery = true)
    List<TaiSan> findAlertAssets(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM tai_san WHERE ngay_bao_tri_tiep_theo <= :date OR tinh_trang = 'Đang hỏng'", nativeQuery = true)
    Page<TaiSan> findAlertAssets(@Param("date") LocalDate date, Pageable pageable);

    @Query(value = "SELECT * FROM tai_san WHERE tinh_trang = :tinhTrang", nativeQuery = true)
    List<TaiSan> findByTinhTrang(@Param("tinhTrang") String tinhTrang);
}
