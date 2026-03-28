package com.sync.itk65.repository;

import com.sync.itk65.entity.CanHo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanHoRepository extends JpaRepository<CanHo, Long> {
    // JpaRepository đã làm sẵn hết các hàm save(), findAll(), deleteById() cho bạn rồi!
    // Sau này ai cần tìm kiếm riêng (VD: Tìm căn hộ đang trống) thì viết thêm hàm vào đây.
}