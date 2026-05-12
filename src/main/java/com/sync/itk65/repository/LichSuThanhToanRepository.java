package com.sync.itk65.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.LichSuThanhToan;

@Repository
public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Long> {

    // Lấy tất cả lịch sử thanh toán, sắp xếp theo ngày thanh toán mới nhất
    List<LichSuThanhToan> findAllByOrderByNgayThanhToanDesc();

    // Lấy lịch sử thanh toán của một căn hộ cụ thể, sắp xếp theo ngày thanh toán mới nhất
    List<LichSuThanhToan> findByCanHoIdOrderByNgayThanhToanDesc(Long canHoId);

    // Tìm lịch sử theo ID hóa đơn
    Optional<LichSuThanhToan> findByHoaDonId(Long hoaDonId);

    // Kiểm tra hóa đơn đã thanh toán chưa
    boolean existsByHoaDonId(Long hoaDonId);

    // Lấy lịch sử theo khoảng thời gian
    @Query("SELECT lstt FROM LichSuThanhToan lstt WHERE lstt.ngayThanhToan BETWEEN :startDate AND :endDate ORDER BY lstt.ngayThanhToan DESC")
    List<LichSuThanhToan> findByNgayThanhToanBetween(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);

    // Lấy lịch sử theo phương thức thanh toán
    List<LichSuThanhToan> findByPhuongThucThanhToanOrderByNgayThanhToanDesc(String phuongThuc);

    // Tìm kiếm theo mã hóa đơn hoặc tên căn hộ
    @Query("SELECT lstt FROM LichSuThanhToan lstt WHERE " +
           "LOWER(lstt.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(lstt.canHo.maCanHo) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY lstt.ngayThanhToan DESC")
    List<LichSuThanhToan> searchByKeyword(@Param("keyword") String keyword);
}
