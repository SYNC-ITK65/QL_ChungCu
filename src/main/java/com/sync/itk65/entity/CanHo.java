package com.sync.itk65.entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "can_ho")
public class CanHo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_can_ho")
    private String maCanHo;

    @Column(name = "dien_tich")
    private Double dienTich;

    @Column(name = "tang")
    private Integer tang;

    @Column(name = "loai")
    private String loai;

    @Column(name = "trang_thai")
    private String trangThai;

    @OneToMany(mappedBy = "canHo", cascade = CascadeType.ALL)
    private List<CuDan> danhSachCuDan;

    public CanHo() {
    }

    public CanHo(Long id, String trangThai, String loai, Double dienTich, String maCanHo, int tang) {
        this.id = id;
        this.trangThai = trangThai;
        this.loai = loai;
        this.dienTich = dienTich;
        this.maCanHo = maCanHo;
        this.tang = tang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public Double getDienTich() {
        return dienTich;
    }

    public void setDienTich(Double dienTich) {
        this.dienTich = dienTich;
    }

    public Integer getTang() {
        return tang;
    }

    public void setTang(Integer tang) {
        this.tang = tang;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public List<CuDan> getDanhSachCuDan() {
        return danhSachCuDan;
    }

    public void setDanhSachCuDan(List<CuDan> danhSachCuDan) {
        this.danhSachCuDan = danhSachCuDan;
    }
}