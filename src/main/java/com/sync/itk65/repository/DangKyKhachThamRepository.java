package com.sync.itk65.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.DangKyKhachTham;

@Repository
public interface DangKyKhachThamRepository extends JpaRepository<DangKyKhachTham, Long> {
    Page<DangKyKhachTham> findAllByOrderByThoiGianDuKienDesc(Pageable pageable);
    List<DangKyKhachTham> findByCuDanIdOrderByThoiGianDuKienDesc(Long cuDanId);

    @Query("SELECT k FROM DangKyKhachTham k WHERE " +
            "(:tuKhoa IS NULL OR LOWER(k.tenKhach) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR LOWER(k.cmnd) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))) AND " +
            "(:trangThai IS NULL OR :trangThai = '' OR k.trangThai = :trangThai) " +
            "ORDER BY k.thoiGianDuKien DESC")
    Page<DangKyKhachTham> searchKhachTham(@Param("tuKhoa") String tuKhoa, @Param("trangThai") String trangThai, Pageable pageable);
}