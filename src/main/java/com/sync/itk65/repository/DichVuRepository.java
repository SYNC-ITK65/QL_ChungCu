package com.sync.itk65.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sync.itk65.entity.DichVu;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, Long> {
}