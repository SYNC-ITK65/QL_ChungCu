package com.sync.itk65.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sync.itk65.entity.KhaoSat;

public interface KhaoSatRepository extends JpaRepository<KhaoSat, Long> {
    List<KhaoSat> findAllByOrderByThoiGianBatDauDesc(); // Xếp khảo sát mới nhất lên đầu.
}