package com.sync.itk65.repository;

import com.sync.itk65.entity.CanHo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CanHoRepository extends JpaRepository<CanHo, Long> {
    
    // Lấy tổng số lượng tất cả căn hộ trong hệ thống
    @Query("SELECT COUNT(canHo) FROM CanHo canHo")
    long countTotalCanHo();

    // Lấy tổng số lượng căn hộ có trạng thái trống (chưa có người ở) để thống kê
    @Query("SELECT COUNT(canHo) FROM CanHo canHo WHERE canHo.trangThai IS NULL OR LOWER(canHo.trangThai) LIKE '%trống%'")
    long countVacantCanHo();

    // Truy vấn danh sách căn hộ có kết hợp tìm kiếm và phân trang
    // - Tìm kiếm LIKE trên trường trangThai (không phân biệt hoa/thường)
    // - Lọc chính xác theo dienTich (nếu giá trị dienTich được truyền vào)
    // - Lọc chính xác theo tầng (nếu giá trị tang được truyền vào)
    // Sử dụng Page<CanHo> kết hợp tham số Pageable của Spring Data phân trang kết quả
    @Query("SELECT canHo FROM CanHo canHo WHERE " +
           "(:trangThai IS NULL OR :trangThai = '' OR LOWER(canHo.trangThai) LIKE LOWER(CONCAT('%', :trangThai, '%'))) AND " +
           "(:dienTich IS NULL OR canHo.dienTich = :dienTich) AND " +
           "(:tang IS NULL OR canHo.tang = :tang)")
    Page<CanHo> timKiemCanHo(@Param("trangThai") String trangThai, 
                             @Param("dienTich") Double dienTich, 
                             @Param("tang") Integer tang, 
                             Pageable pageable);
}