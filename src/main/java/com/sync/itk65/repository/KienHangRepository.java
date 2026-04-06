package com.sync.itk65.repository;

import com.sync.itk65.entity.KienHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KienHangRepository extends JpaRepository<KienHang, Long> {
    List<KienHang> findByCanHoId(Long canHoId);
    List<KienHang> findByTrangThai(String trangThai);
}
