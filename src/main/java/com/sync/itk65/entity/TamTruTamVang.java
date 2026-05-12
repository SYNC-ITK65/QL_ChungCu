package com.sync.itk65.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tam_tru_tam_vang")
public class TamTruTamVang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cu_dan_id", nullable = false)
    private CuDan cuDan;

    @Column(nullable = false)
    private String loai; // Lưu : "Tạm trú" hoặc "Tạm vắng"

    @Column(name = "ngay_bat_dau", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayKetThuc;

    private String lyDo;

    @Column(name = "ho_ten_khach")
    private String hoTenKhach;

    @Column(name = "cccd_khach")
    private String cccdKhach;

    @Column(name = "ly_do_tu_choi")
    private String lyDoTuChoi;

    @Column(name = "trang_thai_duyet", nullable = false)
    private String trangThaiDuyet = "Chờ duyệt";


    public TamTruTamVang() {
    }

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

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
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

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getHoTenKhach() {
        return hoTenKhach;
    }

    public void setHoTenKhach(String hoTenKhach) {
        this.hoTenKhach = hoTenKhach;
    }

    public String getCccdKhach() {
        return cccdKhach;
    }

    public void setCccdKhach(String cccdKhach) {
        this.cccdKhach = cccdKhach;
    }

    public String getLyDoTuChoi() {
        return lyDoTuChoi;
    }

    public void setLyDoTuChoi(String lyDoTuChoi) {
        this.lyDoTuChoi = lyDoTuChoi;
    }

    public String getTrangThaiDuyet() {
        return trangThaiDuyet;
    }

    public void setTrangThaiDuyet(String trangThaiDuyet) {
        this.trangThaiDuyet = trangThaiDuyet;
    }
}