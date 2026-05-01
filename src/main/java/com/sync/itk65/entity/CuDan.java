package com.sync.itk65.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cu_dan")
@PrimaryKeyJoinColumn(name = "id") // THÊM DÒNG NÀY ĐỂ BÁO ĐÂY LÀ KẾT NỐI TỪ BẢNG CHA
public class CuDan extends NguoiDung {

    // 1 căn hộ có thể có nhiều cư dân sinh sống
    // Mã căn hộ là khóa ngoại liên kết đến bảng căn hộ
    @ManyToOne
    @JoinColumn(name = "ma_can_ho")
    private CanHo canHo;

    // Mối quan hệ với chủ hộ bắt buộc phải có để quản lý nhân khẩu
    // @NotBlank: để không cho phép rỗng
    @NotBlank(message = "Mối quan hệ với chủ hộ không được để trống")
    @Column(name = "moi_quan_he")
    private String moiQuanHe;

    // Trạng thái hiện tại cư dân (Đang ở, Đã chuyển đi, ...)
    @Column(name = "trang_thai")
    private String trangThai;

    // Ngày sinh cư dân
    // @DateTimeFormat: để định dạng ngày tháng
    @Column(name = "ngay_sinh")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;

    // Số định danh cá nhân / Căn cước công dân yêu cầu đúng chuẩn định dạng 12 chữ
    @NotBlank(message = "Căn cước công dân không được để trống") // @NotBlank: để không cho phép rỗng
    @Pattern(regexp = "^\\d{12}$", message = "Căn cước công dân (CCCD) phải bao gồm đúng 12 chữ số")
    // @Pattern: để giới hạn giá trị theo định dạng

    @Column(name = "cccd", length = 12)
    private String cccd;

    public CanHo getCanHo() {
        return canHo;
    }

    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }

    public String getMoiQuanHe() {
        return moiQuanHe;
    }

    public void setMoiQuanHe(String moiQuanHe) {
        this.moiQuanHe = moiQuanHe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

}
