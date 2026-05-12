package com.sync.itk65.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tai_san")
public class TaiSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenTaiSan;

    @Column(nullable = false, unique = true)
    private String maTaiSan;

    private String viTri;

    private LocalDate ngayMua;

    private String tinhTrang; // Hoạt động tốt, Đang hỏng, Chờ thanh lý

    private Integer chuKyBaoTri; // số tháng

    private LocalDate ngayBaoTriTiepTheo;

    @PrePersist
    protected void onCreate() {
        if (tinhTrang == null || tinhTrang.isEmpty()) {
            tinhTrang = "Hoạt động tốt";
        }
        if (ngayBaoTriTiepTheo == null && ngayMua != null && chuKyBaoTri != null) {
            ngayBaoTriTiepTheo = ngayMua.plusMonths(chuKyBaoTri);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenTaiSan() {
        return tenTaiSan;
    }

    public void setTenTaiSan(String tenTaiSan) {
        this.tenTaiSan = tenTaiSan;
    }

    public String getMaTaiSan() {
        return maTaiSan;
    }

    public void setMaTaiSan(String maTaiSan) {
        this.maTaiSan = maTaiSan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public LocalDate getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(LocalDate ngayMua) {
        this.ngayMua = ngayMua;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Integer getChuKyBaoTri() {
        return chuKyBaoTri;
    }

    public void setChuKyBaoTri(Integer chuKyBaoTri) {
        this.chuKyBaoTri = chuKyBaoTri;
    }

    public LocalDate getNgayBaoTriTiepTheo() {
        return ngayBaoTriTiepTheo;
    }

    public void setNgayBaoTriTiepTheo(LocalDate ngayBaoTriTiepTheo) {
        this.ngayBaoTriTiepTheo = ngayBaoTriTiepTheo;
    }
}
