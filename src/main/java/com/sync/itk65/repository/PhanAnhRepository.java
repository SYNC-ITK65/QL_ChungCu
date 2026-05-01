package com.sync.itk65.repository;

import com.sync.itk65.entity.PhanAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhanAnhRepository extends JpaRepository<PhanAnh, Long> {
    @Query("SELECT COUNT(pa) FROM PhanAnh pa WHERE LOWER(pa.trangThai) LIKE 'chờ%'")
    long countChoXuLy();

    @Query("SELECT COUNT(pa) FROM PhanAnh pa WHERE LOWER(pa.trangThai) LIKE 'đang%'")
    long countDangXuLy();

    List<PhanAnh> findByCanHoIdOrderByNgayGuiDesc(Long canHoId);
}
