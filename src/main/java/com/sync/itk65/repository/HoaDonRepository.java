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

    @Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE MONTH(h.ngayPhatHanh) = MONTH(CURRENT_DATE) AND YEAR(h.ngayPhatHanh) = YEAR(CURRENT_DATE) AND h.trangThaiThanhToan = 'Đã đóng'")
    Double sumRevenueCurrentMonth();

    // Lấy danh sách hóa đơn của một căn hộ
    @Query("SELECT h FROM HoaDon h WHERE h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> findByCanHoId(@Param("canHoId") Long canHoId);
    @Query("SELECT h FROM HoaDon h WHERE h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    Page<HoaDon> findByCanHoId(@Param("canHoId") Long canHoId, Pageable pageable);

    // Lấy hóa đơn chưa thanh toán
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Chưa đóng' AND h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh ASC")
    List<HoaDon> findUnpaidByCanHoId(@Param("canHoId") Long canHoId);
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Chưa đóng' AND h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh ASC")
    Page<HoaDon> findUnpaidByCanHoId(@Param("canHoId") Long canHoId, Pageable pageable);

    // Lấy hóa đơn đã thanh toán
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Đã đóng' AND h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> findPaidByCanHoId(@Param("canHoId") Long canHoId);
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Đã đóng' AND h.canHo.id = :canHoId ORDER BY h.ngayPhatHanh DESC")
    Page<HoaDon> findPaidByCanHoId(@Param("canHoId") Long canHoId, Pageable pageable);

    // --- Từ nhánh feature/nang-cao ---
    // Lấy doanh thu 6 tháng gần nhất
    @Query("SELECT MONTH(h.ngayPhatHanh), YEAR(h.ngayPhatHanh), COALESCE(SUM(h.tongTien), 0) " +
           "FROM HoaDon h " +
           "WHERE h.ngayPhatHanh >= :sixMonthsAgo " +
           "AND h.trangThaiThanhToan = 'Đã đóng' " +
           "GROUP BY YEAR(h.ngayPhatHanh), MONTH(h.ngayPhatHanh) " +
           "ORDER BY YEAR(h.ngayPhatHanh) ASC, MONTH(h.ngayPhatHanh) ASC")
    List<Object[]> getRevenueLast6Months(@Param("sixMonthsAgo") java.time.LocalDate sixMonthsAgo);

    // --- Từ nhánh main ---
    // Lấy hóa đơn quá hạn - cần thiết cho quản lý công nợ
    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = 'Chưa đóng' AND h.ngayDenHan < CURRENT_DATE ORDER BY h.ngayDenHan ASC")
    List<HoaDon> findOverdueInvoices();
    
    // Đếm số lượng hóa đơn theo trạng thái - cho dashboard
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.trangThaiThanhToan = :trangThai")
    Long countByStatus(@Param("trangThai") String trangThai);

    // Kiểm tra hóa đơn trùng theo căn hộ, tháng, năm
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.canHo.id = :canHoId AND MONTH(h.ngayPhatHanh) = :thang AND YEAR(h.ngayPhatHanh) = :nam")
    Long countByCanHoAndThangNam(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);

    // Tìm kiếm hóa đơn theo từ khóa (mã căn hộ, trạng thái)
    @Query("SELECT h FROM HoaDon h WHERE " +
           "LOWER(h.canHo.maCanHo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.trangThaiThanhToan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> searchByKeyword(@Param("keyword") String keyword);

    // Tìm kiếm hóa đơn theo nhiều điều kiện
    @Query("SELECT h FROM HoaDon h WHERE " +
           "(:maCanHo IS NULL OR :maCanHo = '' OR LOWER(h.canHo.maCanHo) LIKE LOWER(CONCAT('%', :maCanHo, '%'))) AND " +
           "(:trangThai IS NULL OR :trangThai = '' OR h.trangThaiThanhToan = :trangThai) AND " +
           "(:thang IS NULL OR MONTH(h.ngayPhatHanh) = :thang) AND " +
           "(:nam IS NULL OR YEAR(h.ngayPhatHanh) = :nam) " +
           "ORDER BY h.ngayPhatHanh DESC")
    List<HoaDon> searchWithFilters(@Param("maCanHo") String maCanHo,
                                    @Param("trangThai") String trangThai,
                                    @Param("thang") Integer thang,
                                    @Param("nam") Integer nam);

    @Query("SELECT h FROM HoaDon h WHERE " +
           "(:maCanHo IS NULL OR :maCanHo = '' OR LOWER(h.canHo.maCanHo) LIKE LOWER(CONCAT('%', :maCanHo, '%'))) AND " +
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