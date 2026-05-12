package com.sync.itk65.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "phuong_tien")
public class PhuongTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bien_so") // Khớp với lược đồ
    private String bienSoXe;

    @Column(name = "loai") // Khớp với lược đồ
    private String loaiXe;

    @Column(name = "mau_sac") // Khớp với lược đồ
    private String mauXe;

    @Column(name = "trang_thai")
    private String trangThai;

    // Thay đổi ở đây: Liên kết với Căn Hộ theo lược đồ
    @ManyToOne
    @JoinColumn(name = "can_ho_id")
    private CanHo canHo;

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

    public CanHo getCanHo() {
        return canHo;
    }
    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }
    public String getTrangThai() {return trangThai;}

    public void setTrangThai(String trangThai) {this.trangThai = trangThai;}

}
