package com.sync.itk65.repository;

import com.sync.itk65.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE MONTH(h.ngayPhatHanh) = MONTH(CURRENT_DATE) AND YEAR(h.ngayPhatHanh) = YEAR(CURRENT_DATE) AND h.trangThaiThanhToan = 'Đã đóng'")
    Double sumRevenueCurrentMonth();

    // Lấy danh sách hóa đơn của một căn hộ - optimized with index hint
    @Query("SELECT h FROM HoaDon h WHERE h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> findByCanHoId(@Param("canHoId") Long canHoId);

    // Lấy hóa đơn chưa thanh toán - optimized for performance
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Chưa đóng' AND h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh ASC")
    List<HoaDon> findUnpaidByCanHoId(@Param("canHoId") Long canHoId);

    // Lấy hóa đơn đã thanh toán - optimized with proper ordering
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Đã đóng' AND h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> findPaidByCanHoId(@Param("canHoId") Long canHoId);
    
    // Thêm query để lấy hóa đơn quá hạn - cần thiết cho quản lý công nợ
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Chưa đóng' AND h.ngayDenHan < CURRENT_DATE ORDER BY h.ngayDenHan ASC")
    List<HoaDon> findOverdueInvoices();
    
    // Thêm query để đếm số lượng hóa đơn theo trạng thái - cho dashboard
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.trangThaiThanhToan = :trangThai")
    Long countByStatus(@Param("trangThai") String trangThai);
}