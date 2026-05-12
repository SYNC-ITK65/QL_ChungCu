package com.sync.itk65.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "cu_dan")
@PrimaryKeyJoinColumn(name = "id") // Sử dụng cùng ID với NguoiDung để kế thừa
public class CuDan extends NguoiDung {  // CuDan kế thừa từ NguoiDung, không cần định nghĩa lại ID

    // MÃ CĂN HỘ LÀ KHÓA NGOẠI LIÊN KẾT ĐẾN BẢNG CĂN HỘ
    @ManyToOne  // Các cư dân có thể cùng ma_can_ho
    @JoinColumn(name = "ma_can_ho")
    private CanHo canHo;

    // Mối quan hệ với chủ hộ bắt buộc phải có để quản lý nhân khẩu
    @NotBlank(message = "Mối quan hệ với chủ hộ không được để trống")
    @Column(name = "moi_quan_he")
    private String moiQuanHe;

    // Trạng thái hiện tại cư dân (Đang ở, Đã chuyển đi, ...)
    @Column(name = "trang_thai")
    private String trangThai;

    // Ngày sinh cư dân
    @Column(name = "ngay_sinh")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngaySinh;

    // Số định danh cá nhân / Căn cước công dân yêu cầu đúng chuẩn định dạng 12 chữ số
    @NotBlank(message = "Căn cước công dân không được để trống")
    @Pattern(regexp = "^\\d{12}$", message = "Căn cước công dân (CCCD) phải bao gồm đúng 12 chữ số")
    @Column(name = "cccd", length = 12)
    private String cccd;

    // Mối quan hệ 1-N với đăng ký khách thăm - Xóa cư dân sẽ xóa tất cả đăng ký khách thăm
    @OneToMany(mappedBy = "cuDan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DangKyKhachTham> dangKyKhachThams;

    // Mối quan hệ 1-N với hợp đồng - Xóa cư dân sẽ xóa tất cả hợp đồng
    @OneToMany(mappedBy = "cuDan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HopDong> hopDongs;

    // Mối quan hệ 1-N với đặt dịch vụ - Xóa cư dân sẽ xóa tất cả đặt dịch vụ
    @OneToMany(mappedBy = "cuDan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DatDichVu> datDichVus;

    // Mối quan hệ 1-N với tạm trú tạm vắng - Xóa cư dân sẽ xóa tất cả tạm trú tạm vắng
    @OneToMany(mappedBy = "cuDan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TamTruTamVang> tamTruTamVangs;

    // Mối quan hệ 1-N với lịch sử vote - Xóa cư dân sẽ xóa tất cả lịch sử vote
    @OneToMany(mappedBy = "cuDan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LichSuVote> lichSuVotes;

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

    public List<DangKyKhachTham> getDangKyKhachThams() {
        return dangKyKhachThams;
    }

    public void setDangKyKhachThams(List<DangKyKhachTham> dangKyKhachThams) {
        this.dangKyKhachThams = dangKyKhachThams;
    }

    public List<HopDong> getHopDongs() {
        return hopDongs;
    }

    public void setHopDongs(List<HopDong> hopDongs) {
        this.hopDongs = hopDongs;
    }

    public List<DatDichVu> getDatDichVus() {
        return datDichVus;
    }

    public void setDatDichVus(List<DatDichVu> datDichVus) {
        this.datDichVus = datDichVus;
    }

    public List<TamTruTamVang> getTamTruTamVangs() {
        return tamTruTamVangs;
    }

    public void setTamTruTamVangs(List<TamTruTamVang> tamTruTamVangs) {
        this.tamTruTamVangs = tamTruTamVangs;
    }

    public List<LichSuVote> getLichSuVotes() {
        return lichSuVotes;
    }

    public void setLichSuVotes(List<LichSuVote> lichSuVotes) {
        this.lichSuVotes = lichSuVotes;
    }

}
