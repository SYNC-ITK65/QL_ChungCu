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
    
    @Query("SELECT COUNT(c) FROM CanHo c")
    long countTotalCanHo();

    @Query("SELECT COUNT(c) FROM CanHo c WHERE c.trangThai IS NULL OR c.trangThai LIKE 'Trống%'")
    long countVacantCanHo();

    @Query("SELECT c FROM CanHo c WHERE " +
           "(:trangThai IS NULL OR :trangThai = '' OR LOWER(c.trangThai) LIKE LOWER(CONCAT('%', :trangThai, '%'))) AND " +
           "(:dienTich IS NULL OR c.dienTich = :dienTich) AND " +
           "(:tang IS NULL OR c.tang = :tang)")
    Page<CanHo> timKiemCanHo(@Param("trangThai") String trangThai, 
                             @Param("dienTich") Double dienTich, 
                             @Param("tang") Integer tang, 
                             Pageable pageable);
}