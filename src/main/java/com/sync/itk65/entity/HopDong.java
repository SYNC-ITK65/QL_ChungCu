package com.sync.itk65.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "hop_dong")
public class HopDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "can_ho_id", nullable = false)
    private CanHo canHo;

    @ManyToOne
    @JoinColumn(name = "cu_dan_id", nullable = false)
    private CuDan cuDan;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "gia_tri_hop_dong")
    private Double giaTriHopDong;

    @Column(name = "ben_cho_thue", length = 255)
    private String benChoThue;

    @Column(name = "ben_thue", length = 255)
    private String benThue;

    @Column(name = "tien_thue")
    private Double tienThue;

    @Column(name = "tien_coc")
    private Double tienCoc;

    @Column(name = "loai_hop_dong", length = 20)
    private String loaiHopDong; // Thue / Mua

    @Column(name = "trang_thai", length = 20)
    private String trangThai; // DRAFT / ACTIVE / EXPIRED / TERMINATED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CanHo getCanHo() {
        return canHo;
    }

    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }

    public CuDan getCuDan() {
        return cuDan;
    }

    public void setCuDan(CuDan cuDan) {
        this.cuDan = cuDan;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Double getGiaTriHopDong() {
        return giaTriHopDong;
    }

    public void setGiaTriHopDong(Double giaTriHopDong) {
        this.giaTriHopDong = giaTriHopDong;
    }

    public String getLoaiHopDong() {
        return loaiHopDong;
    }

    public void setLoaiHopDong(String loaiHopDong) {
        this.loaiHopDong = loaiHopDong;
    }

    public String getBenChoThue() {
        return benChoThue;
    }

    public void setBenChoThue(String benChoThue) {
        this.benChoThue = benChoThue;
    }

    public String getBenThue() {
        return benThue;
    }

    public void setBenThue(String benThue) {
        this.benThue = benThue;
    }

    public Double getTienThue() {
        return tienThue;
    }

    public void setTienThue(Double tienThue) {
        this.tienThue = tienThue;
    }

    public Double getTienCoc() {
        return tienCoc;
    }

    public void setTienCoc(Double tienCoc) {
        this.tienCoc = tienCoc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
