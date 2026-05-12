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
@Table(name = "chi_so_hang_thang")
public class ChiSoHangThang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // tự động tăng ID
    private Long id;

    @ManyToOne
    @JoinColumn(name = "can_ho_id", nullable = false)
    private CanHo canHo;

    @Column(name = "dien_tieu_thu")
    private Double dienTieuThu;

    @Column(name = "nuoc_tieu_thu")
    private Double nuocTieuThu;

    @Column(name = "ngay_ghi_nhan")
    private LocalDate ngayGhiNhan;

    // --- GETTERS & SETTERS ---

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

    public Double getDienTieuThu() {
        return dienTieuThu;
    }

    public void setDienTieuThu(Double dienTieuThu) {
        this.dienTieuThu = dienTieuThu;
    }

    public Double getNuocTieuThu() {
        return nuocTieuThu;
    }

    public void setNuocTieuThu(Double nuocTieuThu) {
        this.nuocTieuThu = nuocTieuThu;
    }

    public LocalDate getNgayGhiNhan() {
        return ngayGhiNhan;
    }

    public void setNgayGhiNhan(LocalDate ngayGhiNhan) {
        this.ngayGhiNhan = ngayGhiNhan;
    }
}