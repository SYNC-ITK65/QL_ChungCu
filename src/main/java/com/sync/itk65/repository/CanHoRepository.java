package com.sync.itk65.repository;

import com.sync.itk65.entity.CanHo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CanHoRepository extends JpaRepository<CanHo, Long> {
    
    @Query("SELECT COUNT(c) FROM CanHo c")
    long countTotalCanHo();

    @Query("SELECT COUNT(c) FROM CanHo c WHERE c.trangThai IS NULL OR c.trangThai LIKE 'Trống%'")
    long countVacantCanHo();
}