package com.sync.itk65.repository;

import com.sync.itk65.entity.KienHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KienHangRepository extends JpaRepository<KienHang, Long> {
    // List<KienHang> findByCanHoId(Long canHoId);
    @Query(value = "SELECT * FROM kien_hang WHERE can_ho_id = :canHoId", nativeQuery = true)
    List<KienHang> findByCanHoId(@Param("canHoId") Long canHoId);

    // List<KienHang> findByTrangThai(String trangThai);
    @Query(value = "SELECT * FROM kien_hang WHERE trang_thai = :trangThai", nativeQuery = true)
    List<KienHang> findByTrangThai(@Param("trangThai") String trangThai);
}
