package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "chi_so_hang_thang")
public class ChiSoHangThang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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