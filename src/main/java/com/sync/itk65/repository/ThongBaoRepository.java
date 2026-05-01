package com.sync.itk65.repository;

import com.sync.itk65.entity.ThongBao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    // List<ThongBao> findByLoaiOrderByNgayDangDesc(Integer loai);
    @Query(value = "SELECT * FROM thong_bao WHERE loai = :loai ORDER BY ngay_dang DESC", nativeQuery = true)
    List<ThongBao> findByLoaiOrderByNgayDangDesc(@Param("loai") Integer loai);

    // List<ThongBao> findAllByOrderByNgayDangDesc();
    @Query(value = "SELECT * FROM thong_bao ORDER BY ngay_dang DESC", nativeQuery = true)
    List<ThongBao> findAllByOrderByNgayDangDesc();

    // Page<ThongBao> findAllByOrderByNgayDangDesc(Pageable pageable);
    @Query(value = "SELECT * FROM thong_bao ORDER BY ngay_dang DESC", nativeQuery = true)
    Page<ThongBao> findAllByOrderByNgayDangDesc(Pageable pageable);
}
