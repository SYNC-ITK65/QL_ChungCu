package com.sync.itk65.repository;

import com.sync.itk65.entity.CuDan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuDanRepository extends JpaRepository<CuDan, Long> {

    // Tìm danh sách cư dân dựa theo ID của Căn Hộ
    List<CuDan> findByCanHo_Id(Long canHoId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(c) FROM CuDan c WHERE c.trangThai = 'Đang cư trú'")
    long countResidentResiding();

    // Lưu ý: ID của CuDan bây giờ chính là ID của NguoiDung
}
