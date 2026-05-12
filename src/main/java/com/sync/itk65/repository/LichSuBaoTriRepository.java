package com.sync.itk65.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.LichSuBaoTri;

@Repository
public interface LichSuBaoTriRepository extends JpaRepository<LichSuBaoTri, Long> {

    List<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(Long taiSanId);
    Page<LichSuBaoTri> findByTaiSanIdOrderByNgayBaoTriDesc(Long taiSanId, Pageable pageable);

    // Xóa toàn bộ lịch sử bảo trì của một tài sản (dùng trước khi xóa tài sản)
    void deleteByTaiSanId(Long taiSanId);
}
