package com.sync.itk65.repository;

import com.sync.itk65.entity.LichSuBaoTri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuBaoTriRepository extends JpaRepository<LichSuBaoTri, Long> {

    // List<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(Long taiSanId);
    @Query(value = "SELECT * FROM lich_su_bao_tri WHERE tai_san_id = :taiSanId ORDER BY ngay_bao_tri DESC", nativeQuery = true)
    List<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(@Param("taiSanId") Long taiSanId);

    // Page<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(Long taiSanId,
    // Pageable pageable);
    @Query(value = "SELECT * FROM lich_su_bao_tri WHERE tai_san_id = :taiSanId ORDER BY ngay_bao_tri DESC", nativeQuery = true)
    Page<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(@Param("taiSanId") Long taiSanId, Pageable pageable);
}
