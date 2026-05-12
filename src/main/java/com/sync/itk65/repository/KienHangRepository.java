package com.sync.itk65.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.KienHang;

@Repository
public interface KienHangRepository extends JpaRepository<KienHang, Long> {
    List<KienHang> findByCanHoId(Long canHoId);

    List<KienHang> findByTrangThai(String trangThai);

    @Query("SELECT k FROM KienHang k WHERE " +
            "(:maCanHo IS NULL OR LOWER(k.canHo.maCanHo) LIKE LOWER(CONCAT('%', :maCanHo, '%'))) AND " +
            "(:trangThai IS NULL OR :trangThai = '' OR k.trangThai = :trangThai)")
    Page<KienHang> searchKienHang(@Param("maCanHo") String maCanHo, @Param("trangThai") String trangThai, Pageable pageable);
}
