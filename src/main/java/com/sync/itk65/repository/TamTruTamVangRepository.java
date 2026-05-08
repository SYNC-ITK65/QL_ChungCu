package com.sync.itk65.repository;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.TamTruTamVang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TamTruTamVangRepository extends JpaRepository<TamTruTamVang, Long> {
    Page<TamTruTamVang> findAllByOrderByIdDesc(Pageable pageable);

    List<TamTruTamVang> findByCuDanOrderByIdDesc(CuDan cuDan);

    List<TamTruTamVang> findByTrangThaiDuyetAndNgayKetThucBefore(String trangThaiDuyet, LocalDate date);

    @Query("SELECT t FROM TamTruTamVang t WHERE " +
            "(:loai IS NULL OR :loai = '' OR t.loai = :loai) AND " +
            "(:trangThaiDuyet IS NULL OR :trangThaiDuyet = '' OR t.trangThaiDuyet = :trangThaiDuyet) " +
            "ORDER BY t.id DESC")
    Page<TamTruTamVang> searchTamTruTamVang(@Param("loai") String loai, @Param("trangThaiDuyet") String trangThaiDuyet, Pageable pageable);
}