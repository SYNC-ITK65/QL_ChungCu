package com.sync.itk65.repository;

import com.sync.itk65.entity.PhanAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhanAnhRepository extends JpaRepository<PhanAnh, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PhanAnh p WHERE UPPER(p.trangThai) LIKE 'CHỜ%'")
    long countChoXuLy();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PhanAnh p WHERE UPPER(p.trangThai) LIKE 'ĐANG%'")
    long countDangXuLy();
    List<PhanAnh> findByCanHoIdOrderByNgayGuiDesc(Long canHoId);
}