package com.sync.itk65.repository;
import com.sync.itk65.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LichSuVoteRepository extends JpaRepository<LichSuVote, Long> {
    Optional<LichSuVote> findByCuDanAndKhaoSat(CuDan cuDan, KhaoSat khaoSat);
    List<LichSuVote> findByKhaoSatIdOrderByThoiGianVoteDesc(Long khaoSatId);
}