package com.sync.itk65;

// ------------------------------------------------
// Khu vực khởi tạo dữ liệu mặc định
// ------------------------------------------------ 

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.DatDichVuRepository;
import com.sync.itk65.repository.NguoiDungRepository;
import com.sync.itk65.repository.PhuongTienRepository;
import com.sync.itk65.service.HopDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private DatDichVuRepository datDichVuRepository;
    @Autowired
    private CanHoRepository canHoRepository;
    @Autowired
    private PhuongTienRepository phuongTienRepository;
    @Autowired
    private HopDongService hopDongService;
    @Autowired
    private javax.sql.DataSource dataSource;

    private void convertVarcharToNvarchar() {
        try (java.sql.Connection conn = dataSource.getConnection()) {
            java.sql.Statement stmt = conn.createStatement();

            // Special handling for unique constraint on tai_san.ma_tai_san
            try {
                java.sql.ResultSet uqRs = stmt.executeQuery(
                        "SELECT tc.CONSTRAINT_NAME " +
                                "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc " +
                                "JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE ccu ON tc.CONSTRAINT_NAME = ccu.CONSTRAINT_NAME "
                                +
                                "WHERE tc.TABLE_NAME = 'tai_san' AND ccu.COLUMN_NAME = 'ma_tai_san' AND tc.CONSTRAINT_TYPE = 'UNIQUE'");
                if (uqRs.next()) {
                    String constraintName = uqRs.getString("CONSTRAINT_NAME");
                    uqRs.close();

                    System.out.println("[DataInitializer] Dropping unique constraint: " + constraintName);
                    stmt.executeUpdate("ALTER TABLE [tai_san] DROP CONSTRAINT [" + constraintName + "]");

                    System.out.println("[DataInitializer] Altering tai_san.ma_tai_san to NVARCHAR(255)");
                    stmt.executeUpdate("ALTER TABLE [tai_san] ALTER COLUMN [ma_tai_san] NVARCHAR(255) NOT NULL");

                    System.out.println("[DataInitializer] Re-adding unique constraint: " + constraintName);
                    stmt.executeUpdate(
                            "ALTER TABLE [tai_san] ADD CONSTRAINT [" + constraintName + "] UNIQUE ([ma_tai_san])");
                } else {
                    uqRs.close();
                }
            } catch (Exception e) {
                System.err
                        .println("[DataInitializer] Custom unique constraint migration for tai_san.ma_tai_san failed: "
                                + e.getMessage());
            }

            java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT TABLE_NAME, COLUMN_NAME, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE " +
                            "FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE DATA_TYPE = 'varchar' AND TABLE_SCHEMA = 'dbo'");

            java.util.List<String> alterQueries = new java.util.ArrayList<>();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                int length = rs.getInt("CHARACTER_MAXIMUM_LENGTH");
                String isNullable = rs.getString("IS_NULLABLE");

                String lenStr = (length == -1) ? "MAX" : String.valueOf(length);
                String nullStr = "NO".equalsIgnoreCase(isNullable) ? "NOT NULL" : "NULL";

                String sql = String.format("ALTER TABLE [%s] ALTER COLUMN [%s] NVARCHAR(%s) %s",
                        tableName, columnName, lenStr, nullStr);
                alterQueries.add(sql);
            }
            rs.close();
            stmt.close();

            for (String query : alterQueries) {
                try (java.sql.Statement alterStmt = conn.createStatement()) {
                    System.out.println("[DataInitializer] Migrating column: " + query);
                    alterStmt.executeUpdate(query);
                } catch (Exception e) {
                    System.err
                            .println("[DataInitializer] Migration failed for: " + query + ". Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("[DataInitializer] Varchar to NVARCHAR migration failed: " + e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        convertVarcharToNvarchar();
        try {
            hopDongService.tuDongHetHanHopDong();
            System.out.println("[DataInitializer] Đã chạy tự động hết hạn hợp đồng thành công.");
        } catch (Exception e) {
            System.err.println("[DataInitializer] Chạy tự động hết hạn hợp đồng thất bại: " + e.getMessage());
        }
        int soDonDichVuDuocChuanHoa = datDichVuRepository.chuanHoaTrangThaiChoDuyet();
        int soXeDuocChuanHoa = phuongTienRepository.chuanHoaTrangThaiChoDuyet();
        if (soDonDichVuDuocChuanHoa > 0 || soXeDuocChuanHoa > 0) {
            System.out.println("[DataInitializer] Đã chuẩn hóa trạng thái chờ duyệt: " +
                    soDonDichVuDuocChuanHoa + " đơn dịch vụ, " + soXeDuocChuanHoa + " phương tiện.");
        }

        // Kiểm tra tài khoản admin đã tồn tại chưa
        if (nguoiDungRepository.findByTenDangNhap("admin") == null) {

            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap("admin");
            admin.setMatKhauMaHoa("1234");
            admin.setHoTen("Quản Trị Viên Hệ Thống");
            admin.setEmail("admin@sync.com");
            admin.setVaiTro(1);
            admin.setSoDienThoai("0123456789");
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
            staff.setSoDienThoai("0123456789");
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
            cudan.setTrangThai("Đang ở");
            cudan.setNgaySinh(java.time.LocalDate.of(2000, 1, 1));
            cudan.setCccd("001000000000");

            // Assign CanHo to satisfy NotNull validation constraint
            var apartments = canHoRepository.findAll();
            if (!apartments.isEmpty()) {
                cudan.setCanHo(apartments.get(0));
            } else {
                CanHo dummyCanHo = new CanHo();
                dummyCanHo.setMaCanHo("A101");
                dummyCanHo.setDienTich(75.0);
                dummyCanHo.setTang(1);
                dummyCanHo.setLoai("Căn hộ tiêu chuẩn");
                dummyCanHo.setTrangThai("Trống");
                dummyCanHo = canHoRepository.save(dummyCanHo);
                cudan.setCanHo(dummyCanHo);
            }

            nguoiDungRepository.save(cudan);
            System.err.println("USER: cudan");
            System.err.println("PASS: 1234");
        }

        // Tạo tài khoản nhân viên lễ tân
        if (nguoiDungRepository.findByTenDangNhap("letan") == null) {
            NguoiDung letan = new NguoiDung();
            letan.setTenDangNhap("letan");
            letan.setMatKhauMaHoa("1234");
            letan.setHoTen("Nhân Viên Lễ Tân");
            letan.setEmail("letan@sync.com");
            letan.setVaiTro(4);
            letan.setSoDienThoai("0123456789");
            nguoiDungRepository.save(letan);
            System.err.println("USER: letan");
            System.err.println("PASS: 1234");
        }

        // Tạo tài khoản nhân viên bảo vệ
        if (nguoiDungRepository.findByTenDangNhap("baove") == null) {
            NguoiDung baove = new NguoiDung();
            baove.setTenDangNhap("baove");
            baove.setMatKhauMaHoa("1234");
            baove.setHoTen("Nhân Viên Bảo Vệ");
            baove.setEmail("baove@sync.com");
            baove.setVaiTro(5);
            baove.setSoDienThoai("0123456789");
            nguoiDungRepository.save(baove);
            System.err.println("USER: baove");
            System.err.println("PASS: 1234");
        }
    }
}