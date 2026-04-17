package com.sync.itk65.repository;

import com.sync.itk65.entity.ThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    // Custom query để tìm các lịch sử thanh toán của một hóa đơn
    List<ThanhToan> findByHoaDonId(Long hoaDonId);

    // Lấy lịch sử thanh toán theo căn hộ (thông qua hóa đơn), mới nhất trước.
    List<ThanhToan> findByHoaDonCanHoIdOrderByNgayThanhToanDescIdDesc(Long canHoId);
    Page<ThanhToan> findByHoaDonCanHoIdOrderByNgayThanhToanDescIdDesc(Long canHoId, Pageable pageable);
}