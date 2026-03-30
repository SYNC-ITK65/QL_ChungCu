package com.sync.itk65.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dich_vu")
public class DichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(name = "ten_dich_vu")
    private String tenDichVu; // Ví dụ: Tiền điện, Tiền nước, Phí quản lý

    @Column(name = "don_gia")
    private Double donGia; //

    @Column(name = "don_vi_tinh")
    private String donViTinh; // Ví dụ: kWh, m3, tháng

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }
// Bấm Alt + Insert -> Generate Getter and Setter cho tất cả các biến
}