package com.sync.itk65.repository;

import com.sync.itk65.entity.PhuongTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhuongTienRepository extends JpaRepository<PhuongTien, Long> {
    // Hàm tự động kiểm tra xem biển số xe đã tồn tại chưa
    boolean existsByBienSoXe(String bienSoXe);
}
