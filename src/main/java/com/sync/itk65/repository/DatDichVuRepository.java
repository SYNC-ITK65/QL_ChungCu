package com.sync.itk65.repository;

import com.sync.itk65.entity.DatDichVu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DatDichVuRepository extends JpaRepository<DatDichVu, Long> {
    Page<DatDichVu> findAllByOrderByNgayDatDesc(Pageable pageable);

    @Query(value = "SELECT ddv.* FROM dat_dich_vu ddv " +
                   "INNER JOIN cu_dan c ON ddv.cu_dan_id = c.id " +
                   "WHERE c.ma_can_ho = :canHoId " +
                   "AND MONTH(ddv.ngay_dat) = :thang " +
                   "AND YEAR(ddv.ngay_dat) = :nam " +
                   "AND ddv.trang_thai IN (N'Đã duyệt', N'Đã sử dụng', N'Đã hoàn thành') " +
                   "ORDER BY ddv.ngay_dat DESC", nativeQuery = true)
    List<DatDichVu> findDichVuCuaCanHoTrongThang(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);

    // Dùng @Query để lấy lịch sử đặt dịch vụ, sắp xếp ngày mới nhất lên đầu
    @Query("SELECT d FROM DatDichVu d WHERE d.cuDan.id = :cuDanId ORDER BY d.ngayDat DESC")
    List<DatDichVu> layLichSuDatDichVu(@Param("cuDanId") Long cuDanId);

    @Query("SELECT d FROM DatDichVu d WHERE " +
           "(:tuKhoa IS NULL OR LOWER(d.cuDan.hoTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) " +
           "OR CAST(d.cuDan.canHo.id AS string) LIKE CONCAT('%', :tuKhoa, '%') " +
           "OR LOWER(d.dichVu.ten) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))) " +
           "AND (:trangThai IS NULL OR LOWER(d.trangThai) = LOWER(:trangThai)) " +
           "ORDER BY d.ngayDat DESC")
    Page<DatDichVu> timKiemVaLocDonDatDichVu(@Param("tuKhoa") String tuKhoa, @Param("trangThai") String trangThai, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE dat_dich_vu SET trang_thai = N'Chờ duyệt' WHERE trang_thai IS NULL OR TRIM(trang_thai) = '' OR trang_thai = N'Đăng ký mới'", nativeQuery = true)
    int chuanHoaTrangThaiChoDuyet();
}