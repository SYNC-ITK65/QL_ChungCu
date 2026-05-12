package com.sync.itk65.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.KhaoSat;
import com.sync.itk65.entity.LichSuVote;

public interface LichSuVoteRepository extends JpaRepository<LichSuVote, Long> {
    Optional<LichSuVote> findByCuDanAndKhaoSat(CuDan cuDan, KhaoSat khaoSat);

    List<LichSuVote> findByKhaoSatIdOrderByThoiGianVoteDesc(Long khaoSatId);
}