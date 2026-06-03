package com.sync.itk65.repository;

import com.sync.itk65.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LichSuVoteRepository extends JpaRepository<LichSuVote, Long> {
    Optional<LichSuVote> findByCuDanAndKhaoSat(CuDan cuDan, KhaoSat khaoSat);
    List<LichSuVote> findByKhaoSatIdOrderByThoiGianVoteDesc(Long khaoSatId);

    // HÀM MỚI: Tìm kiếm theo Tên HOẶC Mã Căn Hộ, và Lọc theo Phương án đã chọn
    @Query("SELECT l FROM LichSuVote l WHERE l.khaoSat.id = :khaoSatId " +
            "AND (:luaChonId IS NULL OR l.luaChonDaNgan.id = :luaChonId) " +
            "AND (:tuKhoa IS NULL " +
            "     OR LOWER(l.cuDan.hoTen) LIKE LOWER(:tuKhoa) " +
            "     OR CAST(l.cuDan.canHo.id AS string) LIKE :tuKhoa) " +
            "ORDER BY l.thoiGianVote DESC")
    Page<LichSuVote> timKiemVaPhanTrang(
            @Param("khaoSatId") Long khaoSatId,
            @Param("luaChonId") Long luaChonId,
            @Param("tuKhoa") String tuKhoa,
            Pageable pageable);
}