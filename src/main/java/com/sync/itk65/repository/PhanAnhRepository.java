package com.sync.itk65.repository;

import com.sync.itk65.entity.PhanAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanAnhRepository extends JpaRepository<PhanAnh, Long> {
}