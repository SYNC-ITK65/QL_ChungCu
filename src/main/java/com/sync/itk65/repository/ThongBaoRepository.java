package com.sync.itk65.repository;

import com.sync.itk65.entity.ThongBao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    List<ThongBao> findByLoaiOrderByNgayDangDesc(Integer loai);

    List<ThongBao> findAllByOrderByNgayDangDesc();

    Page<ThongBao> findAllByOrderByNgayDangDesc(Pageable pageable);

    /**
     * Lọc thông báo theo đối tượng cư dân:
     * - doiTuongGui = 'ALL': Hiển thị cho tất cả
     * - doiTuongGui IN ('HO_GIA_DINH', 'NHIEU_HO') VÀ giaTriDoiTuong chứa maCanHo
     * - doiTuongGui = 'TANG' VÀ giaTriDoiTuong chứa số tầng
     */
    @Query("SELECT tb FROM ThongBao tb WHERE tb.loai = :loai AND (" +
            "tb.doiTuongGui = 'ALL' " +
            "OR (tb.doiTuongGui IN ('HO_GIA_DINH', 'NHIEU_HO') AND tb.giaTriDoiTuong LIKE CONCAT('%', :maCanHo, '%')) " +
            "OR (tb.doiTuongGui = 'TANG' AND tb.giaTriDoiTuong LIKE CONCAT('%', :tang, '%'))" +
            ") ORDER BY tb.ngayDang DESC")
    Page<ThongBao> locThongBaoTheoCuDan(@Param("loai") Integer loai,
            @Param("maCanHo") String maCanHo,
            @Param("tang") String tang,
            Pageable pageable);

    /**
     * Lọc thông báo chỉ lấy loại ALL (dành cho cư dân chưa có căn hộ)
     */
    @Query("SELECT tb FROM ThongBao tb WHERE tb.loai = :loai AND tb.doiTuongGui = 'ALL' ORDER BY tb.ngayDang DESC")
    Page<ThongBao> locThongBaoChiAll(@Param("loai") Integer loai, Pageable pageable);

    @Query("SELECT tb FROM ThongBao tb WHERE " +
            "(:tuKhoa IS NULL OR LOWER(tb.tieuDe) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))) AND " +
            "(:loai IS NULL OR tb.loai = :loai) " +
            "ORDER BY tb.ngayDang DESC")
    Page<ThongBao> searchThongBao(@Param("tuKhoa") String tuKhoa, @Param("loai") Integer loai, Pageable pageable);
}
