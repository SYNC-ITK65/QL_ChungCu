package com.sync.itk65.repository;

import com.sync.itk65.entity.ThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    // Custom query để tìm các lịch sử thanh toán của một hóa đơn
    List<ThanhToan> findByHoaDonId(Long hoaDonId);

    // Lấy lịch sử thanh toán theo căn hộ (thông qua hóa đơn), mới nhất trước.
    List<ThanhToan> findByHoaDonCanHoIdOrderByNgayThanhToanDescIdDesc(Long canHoId);
    Page<ThanhToan> findByHoaDonCanHoIdOrderByNgayThanhToanDescIdDesc(Long canHoId, Pageable pageable);

    // Tìm kiếm lịch sử thanh toán theo nhiều điều kiện
    @Query("SELECT t FROM ThanhToan t WHERE " +
           "(:maCanHo IS NULL OR :maCanHo = '' OR t.hoaDon.canHo.maCanHo LIKE %:maCanHo%) AND " +
           "(:phuongThuc IS NULL OR :phuongThuc = '' OR t.phuongThuc = :phuongThuc) AND " +
           "(:tuNgay IS NULL OR t.ngayThanhToan >= :tuNgay) AND " +
           "(:denNgay IS NULL OR t.ngayThanhToan <= :denNgay) " +
           "ORDER BY t.ngayThanhToan DESC, t.id DESC")
    Page<ThanhToan> timKiemThanhToan(@Param("maCanHo") String maCanHo,
                                      @Param("phuongThuc") String phuongThuc,
                                      @Param("tuNgay") LocalDate tuNgay,
                                      @Param("denNgay") LocalDate denNgay,
                                      Pageable pageable);
}