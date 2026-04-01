package com.sync.itk65.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phuong_tien")
public class PhuongTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bienSoXe;
    private String loaiXe; // Ô tô hoặc Xe máy
    private String mauXe;
//liên kết khóa ngoại là id của cư dân
    @ManyToOne
    @JoinColumn(name = "id_cu_dan")
//    private CuDan cuDan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public void setBienSoXe(String bienSoXe) {
        this.bienSoXe = bienSoXe;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getMauXe() {
        return mauXe;
    }

    public void setMauXe(String mauXe) {
        this.mauXe = mauXe;
    }

//    public CuDan getCuDan() {
//        return cuDan;
//    }
//
//    public void setCuDan(CuDan cuDan) {
//        this.cuDan = cuDan;
//    }

}
