package com.sync.itk65.repository;

import com.sync.itk65.entity.CuDan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuDanRepository extends JpaRepository<CuDan, Long> {

    // Dùng @Query để Spring Boot không phải "đoán" tên biến nữa, đảm bảo hết lỗi 100%
    @Query("SELECT c FROM CuDan c WHERE c.canHo.id = :canHoId")
    List<CuDan> layDanhSachCuDanTheoCanHo(@Param("canHoId") Long canHoId);

}