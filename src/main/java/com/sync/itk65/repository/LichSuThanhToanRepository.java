package com.sync.itk65.repository;

import com.sync.itk65.entity.LichSuThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LichSuThanhToanRepository extends JpaRepository<LichSuThanhToan, Long> {

    // Lý danh sách toàn b l ch s thanh toán
    List<LichSuThanhToan> findAllByOrderByNgayThanhToanDesc();

    // Lý l ch s thanh toán theo c n h
    List<LichSuThanhToan> findByCanHoIdOrderByNgayThanhToanDesc(Long canHoId);

    // Tìm l ch s theo ID hóa ð n
    Optional<LichSuThanhToan> findByHoaDonId(Long hoaDonId);

    // Ki m tra hóa ð n ñã thanh toán ch a
    boolean existsByHoaDonId(Long hoaDonId);

    // Lý l ch s theo kho ng th i gian
    @Query("SELECT lstt FROM LichSuThanhToan lstt WHERE lstt.ngayThanhToan BETWEEN :startDate AND :endDate ORDER BY lstt.ngayThanhToan DESC")
    List<LichSuThanhToan> findByNgayThanhToanBetween(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);

    // Lý l ch s theo ph ng th c thanh toán
    List<LichSuThanhToan> findByPhuongThucThanhToanOrderByNgayThanhToanDesc(String phuongThuc);

    // Tìm ki m theo mã hóa ð n ho c tên c n h
    @Query("SELECT lstt FROM LichSuThanhToan lstt WHERE " +
           "LOWER(lstt.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(lstt.canHo.maCanHo) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY lstt.ngayThanhToan DESC")
    List<LichSuThanhToan> searchByKeyword(@Param("keyword") String keyword);
}
