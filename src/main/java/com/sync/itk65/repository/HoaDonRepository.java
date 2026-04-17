package com.sync.itk65.repository;

import com.sync.itk65.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE MONTH(h.ngayPhatHanh) = MONTH(CURRENT_DATE) AND YEAR(h.ngayPhatHanh) = YEAR(CURRENT_DATE)")
    Double sumRevenueCurrentMonth();

    // Lấy danh sách hóa đơn của một căn hộ
    @Query("SELECT h FROM HoaDon h WHERE h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> findByCanHoId(@Param("canHoId") Long canHoId);

    // Lấy hóa đơn chưa thanh toán
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Chưa đóng' AND h.canHo.id = :canHoId")
    List<HoaDon> findUnpaidByCanHoId(@Param("canHoId") Long canHoId);

    // Lấy hóa đơn đã thanh toán
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Đã đóng' AND h.canHo.id = :canHoId")
    List<HoaDon> findPaidByCanHoId(@Param("canHoId") Long canHoId);

    // Lấy doanh thu 6 tháng gần nhất
    @Query("SELECT MONTH(h.ngayPhatHanh), YEAR(h.ngayPhatHanh), COALESCE(SUM(h.tongTien), 0) " +
           "FROM HoaDon h " +
           "WHERE h.ngayPhatHanh >= :sixMonthsAgo " +
           "GROUP BY YEAR(h.ngayPhatHanh), MONTH(h.ngayPhatHanh) " +
           "ORDER BY YEAR(h.ngayPhatHanh) ASC, MONTH(h.ngayPhatHanh) ASC")
    List<Object[]> getRevenueLast6Months(@Param("sixMonthsAgo") java.time.LocalDate sixMonthsAgo);
}