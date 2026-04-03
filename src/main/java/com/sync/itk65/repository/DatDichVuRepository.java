package com.sync.itk65.repository;

import com.sync.itk65.entity.DatDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatDichVuRepository extends JpaRepository<DatDichVu, Long> {

    @Query("SELECT d FROM DatDichVu d WHERE d.cuDan.canHo.id = :canHoId AND MONTH(d.ngayDat) = :thang AND YEAR(d.ngayDat) = :nam AND d.trangThai = 'Đã sử dụng'")
    List<DatDichVu> findDichVuCuaCanHoTrongThang(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);

    // Dùng @Query để lấy lịch sử đặt dịch vụ, sắp xếp ngày mới nhất lên đầu
    @Query("SELECT d FROM DatDichVu d WHERE d.cuDan.id = :cuDanId ORDER BY d.ngayDat DESC")
    List<DatDichVu> layLichSuDatDichVu(@Param("cuDanId") Long cuDanId);
}