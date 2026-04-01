package com.sync.itk65.repository;

import com.sync.itk65.entity.ChiSoHangThang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

@Repository
public interface ChiSoHangThangRepository extends JpaRepository<ChiSoHangThang, Long> {
    // Custom query để tìm tất cả chỉ số của một căn hộ cụ thể
    List<ChiSoHangThang> findByCanHoId(Long canHoId);

    @Query("SELECT c FROM ChiSoHangThang c WHERE c.canHo.id = :canHoId AND MONTH(c.ngayGhiNhan) = :thang AND YEAR(c.ngayGhiNhan) = :nam")
    Optional<ChiSoHangThang> findByCanHoAndThangNam(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);
}