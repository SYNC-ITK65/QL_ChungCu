package com.sync.itk65.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hoa_don")
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(name = "thang_nam")
    private String thangNam; // Ví dụ: "10/2026"

    @Column(name = "tong_tien")
    private Double tongTien; //

    @Column(name = "trang_thai_thanh_toan")
    private String trangThaiThanhToan; // "Chưa đóng", "Đã đóng"

    // ĐÂY LÀ KHÓA NGOẠI LIÊN KẾT ĐẾN BẢNG CĂN HỘ CỦA LEADER
    @ManyToOne
    @JoinColumn(name = "can_ho_id")
    private CanHo canHo;

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

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public String getThangNam() {
        return thangNam;
    }

    public void setThangNam(String thangNam) {
        this.thangNam = thangNam;
    }

    // Bấm Alt + Insert -> Generate Getter and Setter cho tất cả các biến
}