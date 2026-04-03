package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dat_dich_vu")
public class DatDichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cư dân nào đặt dịch vụ này
    @ManyToOne
    @JoinColumn(name = "cu_dan_id", nullable = false)
    private CuDan cuDan;

    // Dịch vụ được đặt là dịch vụ gì (Hồ bơi, BBQ, Gym...)
    @ManyToOne
    @JoinColumn(name = "dich_vu_id", nullable = false)
    private DichVu dichVu;

    // Ngày cư dân sử dụng dịch vụ (Dùng để filter theo Tháng/Năm tính tiền)
    @Column(name = "ngay_dat")
    private LocalDate ngayDat;

    // Trạng thái của dịch vụ. VD: "Đã sử dụng", "Đã hủy", "Đăng ký mới"
    // Hóa đơn sẽ chỉ tính tiền những dịch vụ có trạng thái "Đã sử dụng"
    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ghi_chu")
    private String ghiChu;

    // --- GETTERS & SETTERS ---

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

    public DichVu getDichVu() {
        return dichVu;
    }

    public void setDichVu(DichVu dichVu) {
        this.dichVu = dichVu;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDate ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}