package com.sync.itk65.repository;

import com.sync.itk65.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE MONTH(h.ngayPhatHanh) = MONTH(CURRENT_DATE) AND YEAR(h.ngayPhatHanh) = YEAR(CURRENT_DATE)")
    Double sumRevenueCurrentMonth();
}