package com.sync.itk65.repository;

import com.sync.itk65.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    // Tìm người dùng theo Tên đăng nhập (Username)
    NguoiDung findByTenDangNhap(String tenDangNhap);

    // Tìm người dùng theo Số điện thoại
    NguoiDung findBySoDienThoai(String soDienThoai);
}
