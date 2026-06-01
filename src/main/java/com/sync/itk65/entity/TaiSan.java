package com.sync.itk65.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "tai_san")
public class TaiSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên tài sản không được để trống")
    @Column(nullable = false)
    private String tenTaiSan;

    @NotBlank(message = "Mã tài sản không được để trống")
    @Column(nullable = false, unique = true)
    private String maTaiSan;

    @NotBlank(message = "Vị trí không được để trống")
    private String viTri;

    @NotNull(message = "Ngày mua không được để trống")
    private LocalDate ngayMua;

    @NotBlank(message = "Tình trạng không được để trống")
    private String tinhTrang; // Hoạt động tốt, Đang hỏng, Chờ thanh lý

    @NotNull(message = "Chu kỳ bảo trì không được để trống")
    @Min(value = 1, message = "Chu kỳ bảo trì phải ít nhất là 1 tháng")
    private Integer chuKyBaoTri; // số tháng

    private LocalDate ngayBaoTriTiepTheo;

    @PrePersist
    protected void onCreate() {
        if (tinhTrang == null || tinhTrang.isEmpty()) {
            tinhTrang = "Hoạt động tốt";
        }
        tinhToanNgayBaoTri();
    }

    @PreUpdate
    protected void onUpdate() {
        tinhToanNgayBaoTri();
    }

    private void tinhToanNgayBaoTri() {
        if (ngayMua != null && chuKyBaoTri != null) {
            this.ngayBaoTriTiepTheo = ngayMua.plusMonths(chuKyBaoTri);
        } else {
            this.ngayBaoTriTiepTheo = null;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenTaiSan() { return tenTaiSan; }
    public void setTenTaiSan(String tenTaiSan) { this.tenTaiSan = tenTaiSan; }

    public String getMaTaiSan() { return maTaiSan; }
    public void setMaTaiSan(String maTaiSan) { this.maTaiSan = maTaiSan; }

    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }

    public LocalDate getNgayMua() { return ngayMua; }
    public void setNgayMua(LocalDate ngayMua) { this.ngayMua = ngayMua; }

    public String getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(String tinhTrang) { this.tinhTrang = tinhTrang; }

    public Integer getChuKyBaoTri() { return chuKyBaoTri; }
    public void setChuKyBaoTri(Integer chuKyBaoTri) { this.chuKyBaoTri = chuKyBaoTri; }

    public LocalDate getNgayBaoTriTiepTheo() { return ngayBaoTriTiepTheo; }
    public void setNgayBaoTriTiepTheo(LocalDate ngayBaoTriTiepTheo) { this.ngayBaoTriTiepTheo = ngayBaoTriTiepTheo; }
}
