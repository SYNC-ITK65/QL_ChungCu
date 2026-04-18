package com.sync.itk65.repository;

import com.sync.itk65.entity.LichSuBaoTri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuBaoTriRepository extends JpaRepository<LichSuBaoTri, Long> {

    List<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(Long taiSanId);
    Page<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(Long taiSanId, Pageable pageable);
}
