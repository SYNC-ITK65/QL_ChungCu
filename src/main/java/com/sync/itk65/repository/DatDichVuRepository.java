package com.sync.itk65.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sync.itk65.entity.DatDichVu;

@Repository
public interface DatDichVuRepository extends JpaRepository<DatDichVu, Long> {
    Page<DatDichVu> findAllByOrderByNgayDatDesc(Pageable pageable);

    @Query("SELECT d FROM DatDichVu d WHERE d.cuDan.canHo.id = :canHoId AND MONTH(d.ngayDat) = :thang AND YEAR(d.ngayDat) = :nam AND d.trangThai IN ('Đã duyệt', 'Đã sử dụng', 'Đã hoàn thành') ORDER BY d.ngayDat DESC")
    List<DatDichVu> findDichVuCuaCanHoTrongThang(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);

    // Dùng @Query để lấy lịch sử đặt dịch vụ, sắp xếp ngày mới nhất lên đầu
    @Query("SELECT d FROM DatDichVu d WHERE d.cuDan.id = :cuDanId ORDER BY d.ngayDat DESC")
    List<DatDichVu> layLichSuDatDichVu(@Param("cuDanId") Long cuDanId);

    @Modifying
    @Transactional
    @Query("UPDATE DatDichVu d SET d.trangThai = 'Chờ duyệt' WHERE d.trangThai IS NULL OR TRIM(d.trangThai) = '' OR d.trangThai = 'Đăng ký mới'")
    int chuanHoaTrangThaiChoDuyet();
}