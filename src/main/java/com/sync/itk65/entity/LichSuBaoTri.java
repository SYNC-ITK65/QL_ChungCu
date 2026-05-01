package com.sync.itk65.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "lich_su_bao_tri")
public class LichSuBaoTri {

    @Id // Khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 1 tài sản có thể có nhiều lịch sử bảo trì (1,n)
    @JoinColumn(name = "tai_san_id") // Khóa ngoại liên kết đến bảng tài sản
    @NotBlank(message = "Tài sản không được để trống")
    private TaiSan taiSan;

    @Column(name = "ngay_bao_tri")
    @NotBlank(message = "Ngày bảo trì không được để trống")
    private LocalDate ngayBaoTri;

    @Column(name = "chi_phi")
    @NotBlank(message = "Chi phí không được để trống")
    private Double chiPhi;

    @Column(name = "nguoi_thuc_hien")
    @NotBlank(message = "Người thực hiện không được để trống")
    private String nguoiThucHien;

    @Column(name = "noi_dung", columnDefinition = "NVARCHAR(MAX)")
    @NotBlank(message = "Nội dung không được để trống")
    private String noiDung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaiSan getTaiSan() {
        return taiSan;
    }

    public void setTaiSan(TaiSan taiSan) {
        this.taiSan = taiSan;
    }

    public LocalDate getNgayBaoTri() {
        return ngayBaoTri;
    }

    public void setNgayBaoTri(LocalDate ngayBaoTri) {
        this.ngayBaoTri = ngayBaoTri;
    }

    public Double getChiPhi() {
        return chiPhi;
    }

    public void setChiPhi(Double chiPhi) {
        this.chiPhi = chiPhi;
    }

    public String getNguoiThucHien() {
        return nguoiThucHien;
    }

    public void setNguoiThucHien(String nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

}
