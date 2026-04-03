package com.sync.itk65.repository;

import com.sync.itk65.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    // Custom query để tìm các lịch sử thanh toán của một hóa đơn
    List<ThanhToan> findByHoaDonId(Long hoaDonId);
}