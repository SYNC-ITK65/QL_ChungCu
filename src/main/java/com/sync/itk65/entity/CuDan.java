package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cu_dan")
@PrimaryKeyJoinColumn(name = "id") // THÊM DÒNG NÀY ĐỂ BÁO ĐÂY LÀ KẾT NỐI TỪ BẢNG CHA
public class CuDan extends NguoiDung {

    // MÃ CĂN HỘ LÀ KHÓA NGOẠI LIÊN KẾT ĐẾN BẢNG CĂN HỘ
    @ManyToOne
    @JoinColumn(name = "ma_can_ho")
    private CanHo canHo;

    @Column(name = "moi_quan_he")
    private String moiQuanHe;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_sinh")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;

    @Column(name = "cccd", length = 12)
    private String cccd;

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

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

}
