package com.sync.itk65.repository;
import com.sync.itk65.entity.DangKyKhachTham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DangKyKhachThamRepository extends JpaRepository<DangKyKhachTham, Long> {
    List<DangKyKhachTham> findByCuDanIdOrderByThoiGianDuKienDesc(Long cuDanId);
}