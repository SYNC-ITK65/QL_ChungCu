package com.sync.itk65.repository;
import com.sync.itk65.entity.KhaoSat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KhaoSatRepository extends JpaRepository<KhaoSat, Long> {
    List<KhaoSat> findAllByOrderByThoiGianBatDauDesc(); // Xếp khảo sát mới nhất lên đầu.
    @Query("SELECT k FROM KhaoSat k WHERE " +
            "(:tuKhoa IS NULL OR LOWER(k.tieuDe) LIKE LOWER(:tuKhoa)) AND " +
            "(:trangThai IS NULL OR " +
            "  (:trangThai = 1 AND k.thoiGianBatDau > :now) OR " +
            "  (:trangThai = 2 AND k.thoiGianBatDau <= :now AND k.thoiGianKetThuc >= :now) OR " +
            "  (:trangThai = 3 AND k.thoiGianKetThuc < :now) " +
            ") ORDER BY k.id DESC")
    Page<KhaoSat> timKiemVaPhanTrang(
            @Param("tuKhoa") String tuKhoa,
            @Param("trangThai") Integer trangThai,
            @Param("now") java.time.LocalDateTime now,
            Pageable pageable);
}