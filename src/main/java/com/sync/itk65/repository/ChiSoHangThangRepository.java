package com.sync.itk65.repository;

import com.sync.itk65.entity.ChiSoHangThang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface ChiSoHangThangRepository extends JpaRepository<ChiSoHangThang, Long> {
    // Custom query để tìm tất cả chỉ số của một căn hộ cụ thể
    List<ChiSoHangThang> findByCanHoId(Long canHoId);

    @Query("SELECT c FROM ChiSoHangThang c WHERE c.canHo.id = :canHoId AND MONTH(c.ngayGhiNhan) = :thang AND YEAR(c.ngayGhiNhan) = :nam")
    Optional<ChiSoHangThang> findByCanHoAndThangNam(@Param("canHoId") Long canHoId, @Param("thang") int thang, @Param("nam") int nam);

    Optional<ChiSoHangThang> findFirstByCanHoIdAndNgayGhiNhanBetweenOrderByNgayGhiNhanDescIdDesc(
            Long canHoId,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<ChiSoHangThang> findFirstByCanHoIdAndNgayGhiNhanBetweenOrderByNgayGhiNhanAscIdAsc(
            Long canHoId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<ChiSoHangThang> findByCanHoIdAndNgayGhiNhanBetweenOrderByNgayGhiNhanDescIdDesc(
            Long canHoId,
            LocalDate startDate,
            LocalDate endDate
    );

    // Lấy toàn bộ chỉ số, sắp xếp ngày giảm dần
    @Query("SELECT c FROM ChiSoHangThang c ORDER BY c.ngayGhiNhan DESC")
    List<ChiSoHangThang> findAllOrderByNgayGhiNhanDesc();

    // Tìm kiếm theo nhiều điều kiện, sắp xếp ngày giảm dần
    @Query("SELECT c FROM ChiSoHangThang c WHERE " +
            "(:maCanHo IS NULL OR :maCanHo = '' OR LOWER(c.canHo.maCanHo) LIKE LOWER(CONCAT('%', :maCanHo, '%'))) AND " +
            "(:canHoId IS NULL OR c.canHo.id = :canHoId) AND " +
            "(:thang IS NULL OR MONTH(c.ngayGhiNhan) = :thang) AND " +
            "(:nam IS NULL OR YEAR(c.ngayGhiNhan) = :nam) " +
            "ORDER BY c.ngayGhiNhan DESC")
    List<ChiSoHangThang> searchWithFilters(@Param("maCanHo") String maCanHo,
                                           @Param("canHoId") Long canHoId,
                                           @Param("thang") Integer thang,
                                           @Param("nam") Integer nam);
}