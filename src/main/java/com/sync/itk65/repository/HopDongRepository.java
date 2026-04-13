package com.sync.itk65.repository;

import com.sync.itk65.entity.HopDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HopDongRepository extends JpaRepository<HopDong, Long> {

    @Query("SELECT h FROM HopDong h ORDER BY h.id DESC")
    List<HopDong> findAllOrderByIdDesc();

    @Query("SELECT h FROM HopDong h WHERE h.cuDan.id = :cuDanId ORDER BY h.ngayBatDau DESC, h.id DESC")
    List<HopDong> findByCuDanId(@Param("cuDanId") Long cuDanId);

    @Query("SELECT h FROM HopDong h WHERE h.id = :id AND h.cuDan.id = :cuDanId")
    Optional<HopDong> findByIdAndCuDanId(@Param("id") Long id, @Param("cuDanId") Long cuDanId);

    @Query("""
            SELECT COUNT(h) > 0 FROM HopDong h
            WHERE h.canHo.id = :canHoId
              AND h.trangThai = 'ACTIVE'
              AND h.ngayBatDau <= :ngayKetThuc
              AND (h.ngayKetThuc IS NULL OR h.ngayKetThuc >= :ngayBatDau)
            """)
    boolean existsActiveOverlapWithEndDate(@Param("canHoId") Long canHoId,
                                           @Param("ngayBatDau") java.time.LocalDate ngayBatDau,
                                           @Param("ngayKetThuc") java.time.LocalDate ngayKetThuc);

    @Query("""
            SELECT COUNT(h) > 0 FROM HopDong h
            WHERE h.canHo.id = :canHoId
              AND h.trangThai = 'ACTIVE'
              AND (h.ngayKetThuc IS NULL OR h.ngayKetThuc >= :ngayBatDau)
            """)
    boolean existsActiveOverlapOpenEnded(@Param("canHoId") Long canHoId,
                                         @Param("ngayBatDau") java.time.LocalDate ngayBatDau);

    @Query("SELECT h FROM HopDong h WHERE h.trangThai = 'ACTIVE' AND h.ngayKetThuc IS NOT NULL AND h.ngayKetThuc < :today")
    List<HopDong> findExpiredActiveContracts(@Param("today") java.time.LocalDate today);
}
