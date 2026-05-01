package com.sync.itk65.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "nguoi_dung")
@Inheritance(strategy = InheritanceType.JOINED)
public class NguoiDung {
    @Id // Đánh dấu là khóa chính (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị id
    private Long id;

    // Tên đăng nhập không được để rỗng, yêu cầu chỉ chứa chữ và số từ 4 đến 20 ký
    // tự
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Tên đăng nhập phải từ 4-20 ký tự và không chứa ký tự đặc biệt")
    @Column(name = "ten_dang_nhap")
    private String tenDangNhap;

    // Thông tin bảo mật bắt buộc - Mật khẩu đăng nhập
    @NotBlank(message = "Mật khẩu không được để trống")
    @Column(name = "mat_khau_ma_hoa")
    private String matKhauMaHoa;

    // Tên hiển thị người dùng hoặc Admin không bị rỗng
    @NotBlank(message = "Họ tên không được để trống")
    @Column(name = "ho_ten")
    private String hoTen;

    // Kiểm tra cấu trúc Email đúng chuẩn thư điện tử bằng biểu thức chính quy
    // (Regex)
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Cấu trúc Email không hợp lệ (Ví dụ: abc@gmail.com)")
    @Column(name = "email")
    private String email;

    // Validate giới hạn số điện thoại chỉ gồm chữ số (0-9) và có độ dài chuỗi 10 số
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải bao gồm đúng 10 chữ số")
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "vai_tro")
    private int vaiTro; // 1: Admin, 2: Nhân viên, 3: Cư dân,... (Phát triển thêm)

    public NguoiDung() {
    }

    public NguoiDung(long id, String tenDangNhap, String matKhauMaHoa, String hoTen, String email, int vaiTro,
            String soDienThoai) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhauMaHoa = matKhauMaHoa;
        this.hoTen = hoTen;
        this.email = email;
        this.vaiTro = vaiTro;
        this.soDienThoai = soDienThoai;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhauMaHoa() {
        return matKhauMaHoa;
    }

    public void setMatKhauMaHoa(String matKhauMaHoa) {
        this.matKhauMaHoa = matKhauMaHoa;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

}
