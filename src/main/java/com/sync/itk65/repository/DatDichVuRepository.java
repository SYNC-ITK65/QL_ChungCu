package com.sync.itk65.repository;

import com.sync.itk65.entity.DatDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatDichVuRepository extends JpaRepository<DatDichVu, Long> {

    // Câu query lấy tất cả dịch vụ "Đã sử dụng" của một căn hộ trong 1 tháng cụ thể
    @Query("SELECT d FROM DatDichVu d " +
            "JOIN d.cuDan c " +
            "WHERE c.canHo.id = :canHoId " +
            "AND MONTH(d.ngayDat) = :thang AND YEAR(d.ngayDat) = :nam " +
            "AND d.trangThai = 'Đã sử dụng'")
    List<DatDichVu> findDichVuCuaCanHoTrongThang(
            @Param("canHoId") Long canHoId,
            @Param("thang") int thang,
            @Param("nam") int nam);
}