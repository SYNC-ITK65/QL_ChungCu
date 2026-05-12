package com.sync.itk65.entity;

import java.time.LocalDate;

import jakarta.persistence.Column; // Nhớ import cái này
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "hoa_don")
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_phat_hanh")
    private LocalDate ngayPhatHanh;

    @Column(name = "ngay_den_han")
    private LocalDate ngayDenHan;

    @Column(name = "tong_tien")
    private Double tongTien;

    @Column(name = "trang_thai_thanh_toan")
    private String trangThaiThanhToan; // "Chưa đóng", "Đã đóng"

    @ManyToOne
    @JoinColumn(name = "can_ho_id")
    private CanHo canHo;

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(LocalDate ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
    }

    public LocalDate getNgayDenHan() {
        return ngayDenHan;
    }

    public void setNgayDenHan(LocalDate ngayDenHan) {
        this.ngayDenHan = ngayDenHan;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public CanHo getCanHo() {
        return canHo;
    }

    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }
}