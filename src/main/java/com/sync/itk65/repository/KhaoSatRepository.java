package com.sync.itk65.repository;
import com.sync.itk65.entity.KhaoSat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KhaoSatRepository extends JpaRepository<KhaoSat, Long> {
    List<KhaoSat> findAllByOrderByThoiGianBatDauDesc(); // Xếp khảo sát mới nhất lên đầu.
}