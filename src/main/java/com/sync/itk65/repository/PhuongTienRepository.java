package com.sync.itk65.repository;

import com.sync.itk65.entity.PhuongTien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PhuongTienRepository extends JpaRepository<PhuongTien, Long> {
    // Hàm tự động kiểm tra xem biển số xe đã tồn tại chưa
    boolean existsByBienSoXe(String bienSoXe);
    //  lấy danh sách xe của 1 căn hộ
    List<PhuongTien> findByCanHoId(Long canHoId);

    @Modifying
    @Transactional
    @Query("UPDATE PhuongTien p SET p.trangThai = 'Chờ duyệt' WHERE p.trangThai IS NULL OR TRIM(p.trangThai) = '' OR p.trangThai = 'Đăng ký mới'")
    int chuanHoaTrangThaiChoDuyet();
}
