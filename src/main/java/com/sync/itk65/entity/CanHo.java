package com.sync.itk65.entity;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "can_ho")
public class CanHo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ràng buộc giá trị rỗng cho mã căn hộ, nếu rỗng sẽ báo lỗi về form
    @NotBlank(message = "Mã căn hộ không được để trống")
    @Column(name = "ma_can_ho")
    private String maCanHo;

    // Giới hạn giá trị nhỏ nhất cho diện tích để đảm bảo dữ liệu hợp lý
    @NotNull(message = "Diện tích không được để trống")
    @Min(value = 1, message = "Diện tích căn hộ phải lớn hơn 0 m2")
    @Column(name = "dien_tich")
    private Double dienTich;

    // Đảm bảo tầng nằm ở trong giới hạn (từ 1 đến 100) của chung cư
    @NotNull(message = "Số tầng không được để trống")
    @Min(value = 1, message = "Số tầng tối thiểu phải từ tầng 1 trở lên")
    @Max(value = 100, message = "Số tầng vượt quá mức tối đa của chung cư (100 tầng)")
    @Column(name = "tang")
    private Integer tang;

    @NotBlank(message = "Loại căn hộ không được để trống")
    @Column(name = "loai")
    private String loai;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @OneToMany(mappedBy = "canHo", cascade = CascadeType.ALL)
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
        if ("Đang sửa chữa".equals(this.trangThai)) {
            return this.trangThai;
        }
        boolean hasOwner = false;
        boolean hasResident = false;
        if (this.danhSachCuDan != null && !this.danhSachCuDan.isEmpty()) {
            for (CuDan cd : this.danhSachCuDan) {
                if ("Chủ Hộ".equalsIgnoreCase(cd.getMoiQuanHe()) || "Chủ hộ".equalsIgnoreCase(cd.getMoiQuanHe())) {
                    hasOwner = true;
                }
                if ("Đang Ở".equalsIgnoreCase(cd.getTrangThai()) || "Đang ở".equalsIgnoreCase(cd.getTrangThai())) {
                    hasResident = true;
                }
            }
        }
        if (hasResident) {
            return "Đã có chủ";
        } else if (hasOwner || (this.danhSachCuDan != null && !this.danhSachCuDan.isEmpty())) {
            return "Chủ chưa đến";
        }
        return "Trống";
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Transient
    public String getTrangThaiHienThi() {
        if ("Đang sửa chữa".equals(this.trangThai)) {
            return "ch.tt.sua_chua";
        }
        boolean hasOwner = false;
        boolean hasResident = false;
        if (this.danhSachCuDan != null && !this.danhSachCuDan.isEmpty()) {
            for (CuDan cd : this.danhSachCuDan) {
                if ("Chủ Hộ".equalsIgnoreCase(cd.getMoiQuanHe()) || "Chủ hộ".equalsIgnoreCase(cd.getMoiQuanHe())) {
                    hasOwner = true;
                }
                if ("Đang Ở".equalsIgnoreCase(cd.getTrangThai()) || "Đang ở".equalsIgnoreCase(cd.getTrangThai())) {
                    hasResident = true;
                }
            }
        }
        if (hasResident) {
            return "ch.tt.da_ban_giao";
        } else if (hasOwner || (this.danhSachCuDan != null && !this.danhSachCuDan.isEmpty())) {
            return "ch.tt.chu_chua_den";
        }
        return "ch.tt.trong";
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public List<CuDan> getDanhSachCuDan() {
        return danhSachCuDan;
    }

    public void setDanhSachCuDan(List<CuDan> danhSachCuDan) {
        this.danhSachCuDan = danhSachCuDan;
    }
}