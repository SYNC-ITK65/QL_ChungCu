package com.sync.itk65.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "phan_anh")
public class PhanAnh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tieuDe;

    // Sửa TEXT thành NVARCHAR(MAX)
    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @Column(nullable = false)
    private LocalDateTime ngayGui;

    @Column(nullable = false)
    private String trangThai; // Chờ xử lý, Đang xử lý, Đã xong

    // Khóa ngoại liên kết với Căn hộ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "can_ho_id", nullable = false)
    private CanHo canHo;

    // Sửa TEXT thành NVARCHAR(MAX)
    @Column(columnDefinition = "TEXT")
    private String phanHoi;

    private LocalDateTime ngayPhanHoi;

    // Tự động set ngày giờ hiện tại và trạng thái mặc định khi thêm mới
    @PrePersist
    protected void onCreate() {
        if (ngayGui == null) {
            ngayGui = LocalDateTime.now();
        }
        if (trangThai == null || trangThai.isEmpty()) {
            trangThai = "Chờ xử lý";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDateTime getNgayGui() {
        return ngayGui;
    }

    public void setNgayGui(LocalDateTime ngayGui) {
        this.ngayGui = ngayGui;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getPhanHoi() {
        return phanHoi;
    }

    public void setPhanHoi(String phanHoi) {
        this.phanHoi = phanHoi;
    }

    public LocalDateTime getNgayPhanHoi() {
        return ngayPhanHoi;
    }

    public void setNgayPhanHoi(LocalDateTime ngayPhanHoi) {
        this.ngayPhanHoi = ngayPhanHoi;
    }

    public CanHo getCanHo() {
        return canHo;
    }

    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }
}