package com.sync.itk65.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_thanh_toan",
       uniqueConstraints = @UniqueConstraint(columnNames = "hoa_don_id", name = "uk_hoa_don_id"))
public class LichSuThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hoa_don_id", nullable = false)
    private Long hoaDonId;

    @Column(name = "ngay_thanh_toan", nullable = false)
    private LocalDateTime ngayThanhToan;

    @Column(name = "so_tien_thanh_toan", nullable = false)
    private Double soTienThanhToan;

    @Column(name = "phuong_thuc_thanh_toan")
    private String phuongThucThanhToan; // "Tiên m t", "Chuy n kho n", "Th "

    @Column(name = "nguoi_thanh_toan")
    private String nguoiThanhToan;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "can_ho_id")
    private CanHo canHo;

    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @Column(name = "trang_thai_sua")
    private String trangThaiSua; // "Bình th ng", "Ðã s a", "Yêu c u s a"

    // --- GETTERS & SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHoaDonId() { return hoaDonId; }
    public void setHoaDonId(Long hoaDonId) { this.hoaDonId = hoaDonId; }

    public LocalDateTime getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public Double getSoTienThanhToan() { return soTienThanhToan; }
    public void setSoTienThanhToan(Double soTienThanhToan) { this.soTienThanhToan = soTienThanhToan; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public String getNguoiThanhToan() { return nguoiThanhToan; }
    public void setNguoiThanhToan(String nguoiThanhToan) { this.nguoiThanhToan = nguoiThanhToan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public CanHo getCanHo() { return canHo; }
    public void setCanHo(CanHo canHo) { this.canHo = canHo; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getTrangThaiSua() { return trangThaiSua; }
    public void setTrangThaiSua(String trangThaiSua) { this.trangThaiSua = trangThaiSua; }
}
