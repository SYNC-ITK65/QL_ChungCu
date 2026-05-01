package com.sync.itk65.repository;

import com.sync.itk65.entity.CuDan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuDanRepository extends JpaRepository<CuDan, Long> {

       // Truy vấn danh sách cư dân thuộc một căn hộ cụ thể có kết hợp tìm kiếm và phân
       // trang
       // - Lọc chính xác theo id của căn hộ
       // - Tìm kiếm LIKE (từ khóa) trên các trường: hoTen, soDienThoai, cccd
       // - Tìm kiếm LIKE trên trường trangThai
       // Trả về đối tượng Page<CuDan> dùng cho phân trang trên giao diện
       @Query("SELECT c FROM CuDan c WHERE c.canHo.id = :canHoId " +
                     "AND (:tuKhoa IS NULL OR :tuKhoa = '' OR LOWER(c.hoTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) " +
                     "OR c.soDienThoai LIKE CONCAT('%', :tuKhoa, '%') " +
                     "OR c.cccd LIKE CONCAT('%', :tuKhoa, '%')) " +
                     "AND (:trangThai IS NULL OR :trangThai = '' OR LOWER(c.trangThai) LIKE LOWER(CONCAT('%', :trangThai, '%')))")
       Page<CuDan> layDanhSachCuDanTheoCanHo(@Param("canHoId") Long canHoId,
                     @Param("tuKhoa") String tuKhoa,
                     @Param("trangThai") String trangThai,
                     Pageable pageable);

       // Truy vấn danh sách tất cả cư dân có kết hợp tìm kiếm và phân trang
       // - Tìm kiếm LIKE (từ khóa) trên các trường: hoTen, soDienThoai, cccd
       // - Tìm kiếm LIKE trên trường trangThai
       // Trả về Page<CuDan> hỗ trợ hiển thị phân trang
       @Query("SELECT c FROM CuDan c WHERE " +
                     "(:tuKhoa IS NULL OR :tuKhoa = '' OR LOWER(c.hoTen) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) " +
                     "OR c.soDienThoai LIKE CONCAT('%', :tuKhoa, '%') " +
                     "OR c.cccd LIKE CONCAT('%', :tuKhoa, '%')) AND " +
                     "(:trangThai IS NULL OR :trangThai = '' OR LOWER(c.trangThai) LIKE LOWER(CONCAT('%', :trangThai, '%')))")
       Page<CuDan> timKiemCuDan(@Param("tuKhoa") String tuKhoa,
                     @Param("trangThai") String trangThai,
                     Pageable pageable);

       // Lấy tất cả danh sách cư dân thuộc một căn hộ
       // (Dùng cho chức năng xuất dữ liệu Excel, không phân trang)
       @Query("SELECT c FROM CuDan c WHERE c.canHo.id = :canHoId")
       List<CuDan> layTatCaCuDanTheoCanHo(@Param("canHoId") Long canHoId);

       // Đếm tổng số lượng cư dân đang có trạng thái bắt đầu bằng chữ 'ĐANG' (ví dụ:
       // 'Đang ở')
       @Query("SELECT COUNT(c) FROM CuDan c WHERE UPPER(c.trangThai) LIKE 'ĐANG%'")
       long countResidentResiding();
}
