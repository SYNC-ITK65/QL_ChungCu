package com.sync.itk65.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.NguoiDung;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
       // Tìm người dùng theo Tên đăng nhập (Username)
       NguoiDung findByTenDangNhap(String tenDangNhap);

       // Tìm người dùng theo Số điện thoại
       NguoiDung findBySoDienThoai(String soDienThoai);

       // Tìm kiếm người dùng theo nhiều điều kiện
       @Query("SELECT n FROM NguoiDung n WHERE " +
                     "(:tuKhoa IS NULL OR :tuKhoa = '' OR " +
                     "  n.hoTen LIKE %:tuKhoa% OR " +
                     "  n.email LIKE %:tuKhoa% OR " +
                     "  n.soDienThoai LIKE %:tuKhoa% OR " +
                     "  n.tenDangNhap LIKE %:tuKhoa%) AND " +
                     "(:vaiTro IS NULL OR n.vaiTro = :vaiTro) " +
                     "ORDER BY n.id DESC")
       Page<NguoiDung> timKiemNguoiDung(@Param("tuKhoa") String tuKhoa,
                     @Param("vaiTro") Integer vaiTro,
                     Pageable pageable);
}
