package com.sync.itk65.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "khao_sat")
public class KhaoSat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "thoi_gian_bat_dau", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianKetThuc;

    @OneToMany(mappedBy = "khaoSat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LuaChonKhaoSat> danhSachLuaChon = new java.util.ArrayList<>();

    // Default Constructor
    public KhaoSat() {
    }

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LocalDateTime getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public LocalDateTime getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public List<LuaChonKhaoSat> getDanhSachLuaChon() {
        return danhSachLuaChon;
    }

    public void setDanhSachLuaChon(List<LuaChonKhaoSat> danhSachLuaChon) {
        this.danhSachLuaChon = danhSachLuaChon;
    }

    // --- HÀM TIỆN ÍCH UI & LOGIC ---

    public String getTrangThaiHienTai() {
        LocalDateTime now = LocalDateTime.now();
        if (thoiGianBatDau != null && now.isBefore(thoiGianBatDau)) return "SẮP MỞ";
        if (thoiGianKetThuc != null && now.isAfter(thoiGianKetThuc)) return "ĐÃ ĐÓNG";
        return "ĐANG MỞ";
    }

    public int getTongSoVote() {
        if (danhSachLuaChon == null) return 0;
        return danhSachLuaChon.stream()
                .filter(lc -> lc != null && lc.getSoLuotBinhChon() != null) // Lọc bỏ các phần tử lỗi
                .mapToInt(LuaChonKhaoSat::getSoLuotBinhChon)
                .sum();
    }
}