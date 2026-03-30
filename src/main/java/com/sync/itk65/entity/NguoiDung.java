package com.sync.itk65.entity;

public class NguoiDung {
    private int id;
    private String tenDangNhap;
    private String matKhauMaHoa;
    private String hoTen;
    private String email;
    private int vaiTro; // 1: Admin, 2: Chủ hộ, 3: Người thuê


    public NguoiDung() {
    }
    public NguoiDung(int id, String tenDangNhap, String matKhauMaHoa, String hoTen, String email, int vaiTro) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhauMaHoa = matKhauMaHoa;
        this.hoTen = hoTen;
        this.email = email;
        this.vaiTro = vaiTro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
