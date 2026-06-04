package com.sync.itk65.repository;

import com.sync.itk65.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

       @Query(value = "SELECT COALESCE(SUM(h.tong_tien), 0) FROM hoa_don h WHERE MONTH(h.ngay_phat_hanh) = MONTH(GETDATE()) AND YEAR(h.ngay_phat_hanh) = YEAR(GETDATE()) AND h.trang_thai_thanh_toan = N'Đã đóng'", nativeQuery = true)
       Double sumRevenueCurrentMonth();

       // Lấy danh sách hóa đơn của một căn hộ
       @Query("SELECT h FROM HoaDon h WHERE h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
       List<HoaDon> findByCanHoId(@Param("canHoId") Long canHoId);

       @Query("SELECT h FROM HoaDon h WHERE h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
       Page<HoaDon> findByCanHoId(@Param("canHoId") Long canHoId, Pageable pageable);

       // Lấy hóa đơn chưa thanh toán
       @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_thanh_toan = N'Chưa đóng' AND can_ho_id = :canHoId ORDER BY ngay_phat_hanh ASC", nativeQuery = true)
       List<HoaDon> findUnpaidByCanHoId(@Param("canHoId") Long canHoId);

       @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_thanh_toan = N'Chưa đóng' AND can_ho_id = :canHoId ORDER BY ngay_phat_hanh ASC", countQuery = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = N'Chưa đóng' AND can_ho_id = :canHoId", nativeQuery = true)
       Page<HoaDon> findUnpaidByCanHoId(@Param("canHoId") Long canHoId, Pageable pageable);

       // Lấy hóa đơn đã thanh toán
       @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_thanh_toan = N'Đã đóng' AND can_ho_id = :canHoId ORDER BY ngay_phat_hanh DESC", nativeQuery = true)
       List<HoaDon> findPaidByCanHoId(@Param("canHoId") Long canHoId);

       @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_thanh_toan = N'Đã đóng' AND can_ho_id = :canHoId ORDER BY ngay_phat_hanh DESC", countQuery = "SELECT COUNT(*) FROM hoa_don WHERE trang_thai_thanh_toan = N'Đã đóng' AND can_ho_id = :canHoId", nativeQuery = true)
       Page<HoaDon> findPaidByCanHoId(@Param("canHoId") Long canHoId, Pageable pageable);

       // --- Từ nhánh feature/nang-cao ---
       // Lấy doanh thu 6 tháng gần nhất
       @Query(value = "SELECT MONTH(h.ngay_phat_hanh), YEAR(h.ngay_phat_hanh), COALESCE(SUM(h.tong_tien), 0) " +
                     "FROM hoa_don h " +
                     "WHERE h.ngay_phat_hanh >= :sixMonthsAgo " +
                     "AND h.trang_thai_thanh_toan = N'Đã đóng' " +
                     "GROUP BY YEAR(h.ngay_phat_hanh), MONTH(h.ngay_phat_hanh) " +
                     "ORDER BY YEAR(h.ngay_phat_hanh) ASC, MONTH(h.ngay_phat_hanh) ASC", nativeQuery = true)
       List<Object[]> getRevenueLast6Months(@Param("sixMonthsAgo") java.time.LocalDate sixMonthsAgo);

       // --- Từ nhánh main ---
       // Lấy hóa đơn quá hạn - cần thiết cho quản lý công nợ
       @Query(value = "SELECT * FROM hoa_don WHERE trang_thai_thanh_toan = N'Chưa đóng' AND ngay_den_han < GETDATE() ORDER BY ngay_den_han ASC", nativeQuery = true)
       List<HoaDon> findOverdueInvoices();

       // Đếm số lượng hóa đơn theo trạng thái - cho dashboard
       @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.trangThaiThanhToan = :trangThai")
       Long countByStatus(@Param("trangThai") String trangThai);

       // Kiểm tra hóa đơn trùng theo căn hộ, tháng, năm
       @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.canHo.id = :canHoId AND MONTH(h.ngayPhatHanh) = :thang AND YEAR(h.ngayPhatHanh) = :nam")
       Long countByCanHoAndThangNam(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);

       @Query("SELECT COUNT(h) FROM HoaDon h " +
                     "WHERE h.canHo.id = :canHoId " +
                     "AND MONTH(h.ngayPhatHanh) = :thang " +
                     "AND YEAR(h.ngayPhatHanh) = :nam " +
                     "AND h.id <> :excludeId")
       Long countByCanHoAndThangNamExcludingId(
                     @Param("canHoId") Long canHoId,
                     @Param("thang") int thang,
                     @Param("nam") int nam,
                     @Param("excludeId") Long excludeId);

       // Tìm kiếm hóa đơn theo từ khóa (mã căn hộ, trạng thái)
       @Query("SELECT h FROM HoaDon h WHERE " +
                     "LOWER(h.canHo.maCanHo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                     "LOWER(h.trangThaiThanhToan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                     "ORDER BY h.ngayPhatHanh DESC")
       List<HoaDon> searchByKeyword(@Param("keyword") String keyword);

       // Tìm kiếm hóa đơn theo nhiều điều kiện
       @Query("SELECT h FROM HoaDon h WHERE " +
                     "(:maCanHo IS NULL OR :maCanHo = '' OR LOWER(h.canHo.maCanHo) LIKE LOWER(CONCAT('%', :maCanHo, '%'))) AND "
                     +
                     "(:trangThai IS NULL OR :trangThai = '' OR h.trangThaiThanhToan = :trangThai) AND " +
                     "(:thang IS NULL OR MONTH(h.ngayPhatHanh) = :thang) AND " +
                     "(:nam IS NULL OR YEAR(h.ngayPhatHanh) = :nam) " +
                     "ORDER BY h.ngayPhatHanh DESC")
       Page<HoaDon> searchWithFilters(@Param("maCanHo") String maCanHo,
                     @Param("trangThai") String trangThai,
                     @Param("thang") Integer thang,
                     @Param("nam") Integer nam,
                     Pageable pageable);
}