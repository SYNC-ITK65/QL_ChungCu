package com.sync.itk65.repository;

import com.sync.itk65.entity.PhuongTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PhuongTienRepository extends JpaRepository<PhuongTien, Long> {
    // Hàm tự động kiểm tra xem biển số xe đã tồn tại chưa
    boolean existsByBienSoXe(String bienSoXe);
    //  lấy danh sách xe của 1 căn hộ
    List<PhuongTien> findByCanHoId(Long canHoId);
    Page<PhuongTien> findAllByOrderByIdDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE PhuongTien p SET p.trangThai = 'Chờ duyệt' WHERE p.trangThai IS NULL OR TRIM(p.trangThai) = '' OR p.trangThai = 'Đăng ký mới'")
    int chuanHoaTrangThaiChoDuyet();
    @Query("SELECT DISTINCT p FROM PhuongTien p LEFT JOIN p.canHo ch LEFT JOIN ch.danhSachCuDan c WHERE " +
            "(:tuKhoa IS NULL OR LOWER(p.bienSoXe) LIKE LOWER(:tuKhoa) OR LOWER(ch.maCanHo) LIKE LOWER(:tuKhoa) OR LOWER(c.hoTen) LIKE LOWER(:tuKhoa)) AND " +
            "(:trangThai IS NULL OR p.trangThai = :trangThai) AND " +
            "(:loaiXe IS NULL OR p.loaiXe = :loaiXe) " +
            "ORDER BY p.id DESC")
    Page<PhuongTien> timKiemVaLocPhuongTien(@Param("tuKhoa") String tuKhoa,
                                            @Param("trangThai") String trangThai,
                                            @Param("loaiXe") String loaiXe,
                                            Pageable pageable);
}
