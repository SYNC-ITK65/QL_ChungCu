package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    // Sửa TEXT thành NVARCHAR(MAX)
    @Column(name = "noi_dung", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String noiDung;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;

    @Column(name = "loai", nullable = false)
    private Integer loai; // 1: Bảng tin chung, 2: Cẩm nang

    @PrePersist
    protected void onCreate() {
        if (ngayDang == null) {
            ngayDang = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDateTime getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(LocalDateTime ngayDang) {
        this.ngayDang = ngayDang;
    }

    public Integer getLoai() {
        return loai;
    }

    public void setLoai(Integer loai) {
        this.loai = loai;
    }

}