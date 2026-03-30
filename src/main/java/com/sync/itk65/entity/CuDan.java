package com.sync.itk65.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cu_dan")
public class CuDan extends NguoiDung {

    // MÃ CĂN HỘ LÀ KHÓA NGOẠI LIÊN KẾT ĐẾN BẢNG CĂN HỘ
    @ManyToOne
    @JoinColumn(name = "ma_can_ho")
    private CanHo canHo;

    @Column(name = "moi_quan_he")
    private String moiQuanHe;

    @Column(name = "trang_thai")
    private String trangThai;

    public CanHo getCanHo() {
        return canHo;
    }

    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }

    public String getMoiQuanHe() {
        return moiQuanHe;
    }

    public void setMoiQuanHe(String moiQuanHe) {
        this.moiQuanHe = moiQuanHe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}