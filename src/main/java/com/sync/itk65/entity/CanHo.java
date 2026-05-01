package com.sync.itk65.entity;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
// @Entity: Đánh dấu class này là bảng trong DB, kiểu gì cũng phải có tên trong
// danh sách bảng

@Table(name = "can_ho")
// @Table: Để đặt cái tên cho bảng nếu không muốn dùng tên mặc định của class
public class CanHo {

    @Id // để đánh dấu là khóa chính (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // để trường id tăng tự động
    private Long id;

    // Ràng buộc giá trị rỗng cho mã căn hộ, nếu rỗng sẽ báo lỗi về form
    @NotBlank(message = "Mã căn hộ không được để trống") // @NotBlank: để không cho phép rỗng
    @Column(name = "ma_can_ho") // @Column: để đặt cái tên cho cột
    private String maCanHo;

    // Diện tích căn hộ nhỏ nhất phải >1m2
    @Min(value = 1, message = "Diện tích căn hộ phải lớn hơn 0 m2") // @Min: để giới hạn giá trị nhỏ nhất
    @Column(name = "dien_tich") // @Column: để đặt cái tên cho cột
    private Double dienTich;

    // Số tầng của chung cư phải >= 1 và <= 100
    @Min(value = 1, message = "Số tầng tối thiểu phải từ tầng 1 trở lên") // @Min: để giới hạn giá trị nhỏ nhất
    @Max(value = 100, message = "Số tầng vượt quá mức tối đa của chung cư (100 tầng)") // @Max: để giới hạn giá trị lớn
                                                                                       // nhất
    @Column(name = "tang")
    private Integer tang;

    @Column(name = "loai")
    private String loai;

    @Column(name = "trang_thai")
    private String trangThai;

    // 1 căn hộ có thể có nhiều cư dân sinh sống
    @OneToMany(mappedBy = "canHo", cascade = CascadeType.ALL) // @OneToMany: 1 căn hộ có thể có nhiều cư dân (1,n)
    private List<CuDan> danhSachCuDan; //

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