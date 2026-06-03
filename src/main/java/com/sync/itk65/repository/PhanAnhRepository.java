package com.sync.itk65.repository;

import com.sync.itk65.entity.PhanAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PhanAnhRepository extends JpaRepository<PhanAnh, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PhanAnh p WHERE LOWER(p.trangThai) LIKE 'chờ%'")
    long countChoXuLy();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PhanAnh p WHERE LOWER(p.trangThai) LIKE 'đang%'")
    long countDangXuLy();
    List<PhanAnh> findByCanHoIdOrderByNgayGuiDesc(Long canHoId);
    @Query("SELECT p FROM PhanAnh p WHERE " +
            "(:tuKhoa IS NULL OR :tuKhoa = '' OR LOWER(p.tieuDe) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR LOWER(p.canHo.maCanHo) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))) AND " +
            "(:trangThai IS NULL OR :trangThai = '' OR p.trangThai = :trangThai) " +
            "ORDER BY p.ngayGui DESC")
    Page<PhanAnh> timKiemVaLocPhanAnh(@Param("tuKhoa") String tuKhoa,
                                      @Param("trangThai") String trangThai,
                                      Pageable pageable);
}
