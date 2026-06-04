package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dang_ky_khach_tham")
public class DangKyKhachTham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cu_dan_id", nullable = false)
    private CuDan cuDan;

    private String tenKhach;
    private String cmnd;
    private String bienSoXe;
    private LocalDateTime thoiGianDuKien;

    @Column(name = "ngay_di")
    private LocalDateTime ngayDi;

    private String trangThai; // "Chờ duyệt", "Đã duyệt", "Không duyệt"

    @Column(name = "thoi_gian_duyet")
    private LocalDateTime thoiGianDuyet; // Lưu thời gian Admin bấm duyệt

    // --- GETTER & SETTER ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CuDan getCuDan() {
        return cuDan;
    }

    public void setCuDan(CuDan cuDan) {
        this.cuDan = cuDan;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public LocalDateTime getThoiGianDuKien() {
        return thoiGianDuKien;
    }

    public void setThoiGianDuKien(LocalDateTime thoiGianDuKien) {
        this.thoiGianDuKien = thoiGianDuKien;
    }

    public LocalDateTime getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(LocalDateTime ngayDi) {
        this.ngayDi = ngayDi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getThoiGianDuyet() {
        return thoiGianDuyet;
    }

    public void setThoiGianDuyet(LocalDateTime thoiGianDuyet) {
        this.thoiGianDuyet = thoiGianDuyet;
    }
}