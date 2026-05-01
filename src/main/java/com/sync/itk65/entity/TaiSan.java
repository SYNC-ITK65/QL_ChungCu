package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tai_san")
public class TaiSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_tai_san")
    @NotBlank(message = "Tên tài sản không được để trống")
    private String tenTaiSan;

    @Column(name = "ma_tai_san")
    @NotBlank(message = "Mã tài sản không được để trống")
    private String maTaiSan;

    @Column(name = "vi_tri")
    @NotBlank(message = "Vị trí không được để trống")
    private String viTri;

    @Column(name = "ngay_mua")
    @NotBlank(message = "Ngày mua không được để trống")
    private LocalDate ngayMua;

    @Column(name = "tinh_trang")
    @NotBlank(message = "Tình trạng không được để trống")
    private String tinhTrang; // Hoạt động tốt, Đang hỏng, Chờ thanh lý

    @Column(name = "chu_ky_bao_tri")
    @NotBlank(message = "Chu kỳ bảo trì không được để trống")
    private Integer chuKyBaoTri; // số tháng

    @Column(name = "ngay_bao_tri_tiep_theo")
    @NotBlank(message = "Ngày bảo trì tiếp theo không được để trống")
    private LocalDate ngayBaoTriTiepTheo;

    @PrePersist
    protected void onCreate() {
        if (tinhTrang == null || tinhTrang.isEmpty()) {
            tinhTrang = "Hoạt động tốt";
        }
        if (ngayBaoTriTiepTheo == null && ngayMua != null && chuKyBaoTri != null) {
            ngayBaoTriTiepTheo = ngayMua.plusMonths(chuKyBaoTri);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenTaiSan() {
        return tenTaiSan;
    }

    public void setTenTaiSan(String tenTaiSan) {
        this.tenTaiSan = tenTaiSan;
    }

    public String getMaTaiSan() {
        return maTaiSan;
    }

    public void setMaTaiSan(String maTaiSan) {
        this.maTaiSan = maTaiSan;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public LocalDate getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(LocalDate ngayMua) {
        this.ngayMua = ngayMua;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Integer getChuKyBaoTri() {
        return chuKyBaoTri;
    }

    public void setChuKyBaoTri(Integer chuKyBaoTri) {
        this.chuKyBaoTri = chuKyBaoTri;
    }

    public LocalDate getNgayBaoTriTiepTheo() {
        return ngayBaoTriTiepTheo;
    }

    public void setNgayBaoTriTiepTheo(LocalDate ngayBaoTriTiepTheo) {
        this.ngayBaoTriTiepTheo = ngayBaoTriTiepTheo;
    }
}
