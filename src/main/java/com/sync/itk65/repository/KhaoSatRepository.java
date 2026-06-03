package com.sync.itk65.repository;
import com.sync.itk65.entity.KhaoSat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface KhaoSatRepository extends JpaRepository<KhaoSat, Long> {
    Page<KhaoSat> findAllByOrderByThoiGianBatDauDesc(Pageable pageable); // Xếp khảo sát mới nhất lên đầu.
}