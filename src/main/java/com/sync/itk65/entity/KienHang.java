package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "kien_hang")
public class KienHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "can_ho_id")
    private CanHo canHo;

    @Column(name = "nguoi_gui")
    private String nguoiGui;

    @Column(name = "nguoi_nhan")
    private String nguoiNhan;

    @Column(name = "ngay_nhan")
    private LocalDate ngayNhan;

    @Column(name = "trang_thai")
    private String trangThai; // VD: "Chờ nhận", "Đã nhận"

    public KienHang() {
    }

    public KienHang(Long id, CanHo canHo, String nguoiGui, String nguoiNhan, LocalDate ngayNhan, String trangThai) {
        this.id = id;
        this.canHo = canHo;
        this.nguoiGui = nguoiGui;
        this.nguoiNhan = nguoiNhan;
        this.ngayNhan = ngayNhan;
        this.trangThai = trangThai;
    }

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

    public String getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(String nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public LocalDate getNgayNhan() {
        return ngayNhan;
    }

    public void setNgayNhan(LocalDate ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
