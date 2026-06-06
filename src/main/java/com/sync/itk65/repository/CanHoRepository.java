package com.sync.itk65.repository;

import com.sync.itk65.entity.CanHo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CanHoRepository extends JpaRepository<CanHo, Long> {

       Optional<CanHo> findByMaCanHo(String maCanHo);

       Optional<CanHo> findByMaCanHoIgnoreCase(String maCanHo);

       /**
        * Lấy danh sách căn hộ có ít nhất một cư dân đang ở (trangThai LIKE 'Đang Ở').
        * Dùng native query để tránh vấn đề collation Unicode tiếng Việt trên SQL Server.
        * Dùng cho tạo chỉ số hàng loạt và form tạo chỉ số thủ công.
        */
       @Query(value = "SELECT DISTINCT c.* FROM can_ho c " +
              "JOIN cu_dan cd ON cd.ma_can_ho = c.id " +
              "WHERE cd.trang_thai LIKE N'Đang Ở' OR cd.trang_thai LIKE N'Đang ở'",
              nativeQuery = true)
       java.util.List<CanHo> findCanHoCoDanDangO();

       // Lấy tổng số lượng tất cả căn hộ trong hệ thống
       @Query("SELECT COUNT(canHo) FROM CanHo canHo")
       long countTotalCanHo();

       // Lấy tổng số lượng căn hộ có trạng thái trống (chưa có người ở) để thống kê
       @Query(value = "SELECT COUNT(*) FROM can_ho WHERE trang_thai IS NULL OR trang_thai = N'Trống' OR LOWER(trang_thai) LIKE N'%trống%'", nativeQuery = true)
       long countVacantCanHo();

       // Truy vấn danh sách căn hộ có kết hợp tìm kiếm và phân trang
       // - Tìm kiếm LIKE trên trường trangThai (không phân biệt hoa/thường)
       // - Lọc chính xác theo loai (nếu giá trị loai được truyền vào)
       // - Lọc chính xác theo tầng (nếu giá trị tang được truyền vào)
       // Sử dụng Page<CanHo> kết hợp tham số Pageable của Spring Data phân trang kết
       // quả
       @Query("SELECT canHo FROM CanHo canHo WHERE " +
                      "(:trangThai IS NULL OR :trangThai = '' OR LOWER(canHo.trangThai) LIKE LOWER(CONCAT('%', :trangThai, '%'))) AND " +
                      "(:loai IS NULL OR :loai = '' OR canHo.loai = :loai) AND " +
                      "(:tang IS NULL OR canHo.tang = :tang)")
       Page<CanHo> timKiemCanHo(@Param("trangThai") String trangThai,
                      @Param("loai") String loai,
                      @Param("tang") Integer tang,
                      Pageable pageable);
}