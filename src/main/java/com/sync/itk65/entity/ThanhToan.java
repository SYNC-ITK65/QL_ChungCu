package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "thanh_toan")
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết Khóa ngoại đến bảng HOA_DON
    @ManyToOne
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "so_tien")
    private Double soTien;

    @Column(name = "ngay_thanh_toan")
    private LocalDate ngayThanhToan;

    @Column(name = "phuong_thuc")
    private String phuongThuc; // Ví dụ: "Tiền mặt", "Chuyển khoản", "Momo"

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Double getSoTien() {
        return soTien;
    }

    public void setSoTien(Double soTien) {
        this.soTien = soTien;
    }

    public LocalDate getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDate ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }
}