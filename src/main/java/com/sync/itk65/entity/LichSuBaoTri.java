package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lich_su_bao_tri")
public class LichSuBaoTri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_san_id", nullable = false)
    private TaiSan taiSan;

    @Column(nullable = false)
    private LocalDate ngayBaoTri;

    private Double chiPhi;

    private String nguoiThucHien;

    // Sửa TEXT thành NVARCHAR(MAX)
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaiSan getTaiSan() {
        return taiSan;
    }

    public void setTaiSan(TaiSan taiSan) {
        this.taiSan = taiSan;
    }

    public LocalDate getNgayBaoTri() {
        return ngayBaoTri;
    }

    public void setNgayBaoTri(LocalDate ngayBaoTri) {
        this.ngayBaoTri = ngayBaoTri;
    }

    public Double getChiPhi() {
        return chiPhi;
    }

    public void setChiPhi(Double chiPhi) {
        this.chiPhi = chiPhi;
    }

    public String getNguoiThucHien() {
        return nguoiThucHien;
    }

    public void setNguoiThucHien(String nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

}
