package com.sync.itk65.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.PhanAnh;

@Repository
public interface PhanAnhRepository extends JpaRepository<PhanAnh, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PhanAnh p WHERE LOWER(p.trangThai) LIKE 'chờ%'")
    long countChoXuLy();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PhanAnh p WHERE LOWER(p.trangThai) LIKE 'đang%'")
    long countDangXuLy();

    List<PhanAnh> findByCanHoIdOrderByNgayGuiDesc(Long canHoId);
}
