package com.sync.itk65.repository;

import com.sync.itk65.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    List<ThongBao> findByLoaiOrderByNgayDangDesc(Integer loai);
    List<ThongBao> findAllByOrderByNgayDangDesc();
}
