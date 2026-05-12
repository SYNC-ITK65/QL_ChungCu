package com.sync.itk65.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "can_ho")
public class CanHo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // @generatedValue tự tăng
    private Long id;

    // Ràng buộc giá trị rỗng cho mã căn hộ, nếu rỗng sẽ báo lỗi về form
    @NotBlank(message = "Mã căn hộ không được để trống")    // @NotBlank không đc để trống
    @Column(name = "ma_can_ho")
    private String maCanHo;

    // Giới hạn giá trị nhỏ nhất cho diện tích để đảm bảo dữ liệu hợp lý
    @Min(value = 1, message = "Diện tích căn hộ phải lớn hơn 0 m2")
    @Column(name = "dien_tich")
    private Double dienTich;

    // Đảm bảo tầng nằm ở trong giới hạn (từ 1 đến 100) của chung cư
    @Min(value = 1, message = "Số tầng tối thiểu phải từ tầng 1 trở lên")
    @Max(value = 100, message = "Số tầng vượt quá mức tối đa của chung cư (100 tầng)")
    @Column(name = "tang")
    private Integer tang;

    @Column(name = "loai")
    private String loai;

    @Column(name = "trang_thai")
    private String trangThai;

    @OneToMany(mappedBy = "canHo", cascade = CascadeType.ALL)  // Một căn hộ có cả list cư dân
    private List<CuDan> danhSachCuDan;

    public CanHo() {
    }

    public CanHo(Long id, String trangThai, String loai, Double dienTich, String maCanHo, int tang) {
        this.id = id;
        this.trangThai = trangThai;
        this.loai = loai;
        this.dienTich = dienTich;
        this.maCanHo = maCanHo;
        this.tang = tang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public Double getDienTich() {
        return dienTich;
    }

    public void setDienTich(Double dienTich) {
        this.dienTich = dienTich;
    }

    public Integer getTang() {
        return tang;
    }

    public void setTang(Integer tang) {
        this.tang = tang;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public List<CuDan> getDanhSachCuDan() {
        return danhSachCuDan;
    }

    public void setDanhSachCuDan(List<CuDan> danhSachCuDan) {
        this.danhSachCuDan = danhSachCuDan;
    }
}