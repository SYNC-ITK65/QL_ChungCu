package com.sync.itk65;

// ------------------------------------------------
// Khu vực khởi tạo dữ liệu mặc định
// ------------------------------------------------ 

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public void run(String... args) throws Exception {

        // Kiểm tra tài khoản admin đã tồn tại chưa
        if (nguoiDungRepository.findByTenDangNhap("admin") == null) {

            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap("admin");
            admin.setMatKhauMaHoa("1234");
            admin.setHoTen("Quản Trị Viên Hệ Thống");
            admin.setEmail("admin@sync.com");
            admin.setVaiTro(1);

            nguoiDungRepository.save(admin);

            System.err.println("USER: admin");
            System.err.println("PASS: 1234");

        }

        // Kiểm tra tài khoản nhân viên mặc định đã tồn tại chưa
        if (nguoiDungRepository.findByTenDangNhap("staff") == null) {

            NguoiDung staff = new NguoiDung();
            staff.setTenDangNhap("staff");
            staff.setMatKhauMaHoa("1234");
            staff.setHoTen("Nhân Viên Quản Lý");
            staff.setEmail("staff@sync.com");
            staff.setVaiTro(2);

            nguoiDungRepository.save(staff);

            System.err.println("USER: staff");
            System.err.println("PASS: 1234");

        }

        // Kiểm tra tài khoản cư dân mặc định đã tồn tại chưa
        if (nguoiDungRepository.findByTenDangNhap("cudan") == null) {

            CuDan cudan = new CuDan();
            cudan.setTenDangNhap("cudan");
            cudan.setMatKhauMaHoa("1234");
            cudan.setHoTen("Cư Dân Mặc Định");
            cudan.setEmail("cudan@sync.com");
            cudan.setVaiTro(3);
            cudan.setSoDienThoai("0123456789");
            cudan.setMoiQuanHe("Chủ hộ");
            cudan.setTrangThai("Đang cư trú");
            cudan.setNgaySinh(java.time.LocalDate.of(2000, 1, 1));
            cudan.setCccd("001000000000");

            nguoiDungRepository.save(cudan);

            System.err.println("USER: cudan");
            System.err.println("PASS: 1234");
        }

    }
}