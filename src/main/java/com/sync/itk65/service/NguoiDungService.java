package com.sync.itk65.service;

import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // 1. Lấy danh sách tất cả người dùng
    public List<NguoiDung> layTatCaNguoiDung() {
        return nguoiDungRepository.findAll();
    }

    // 2. Lưu thông tin người dùng (Dùng cho cả Thêm mới và Sửa)
    public void luuNguoiDung(NguoiDung nguoiDung) {
        nguoiDungRepository.save(nguoiDung);
    }

    // 3. Lấy thông tin người dùng theo ID (Dùng để sửa)
    public NguoiDung layNguoiDungTheoId(Long id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    // 4. Xóa người dùng
    public void xoaNguoiDung(Long id) {
        nguoiDungRepository.deleteById(id);
    }

    // 5. Tìm người dùng theo Tên đăng nhập (Dùng cho đăng nhập)
    public NguoiDung timTheoTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.findByTenDangNhap(tenDangNhap);
    }

    // 6. Tìm người dùng theo Số điện thoại
    public NguoiDung timTheoSoDienThoai(String soDienThoai) {
        return nguoiDungRepository.findBySoDienThoai(soDienThoai);
    }

    // 7. Đổi mật khẩu
    public void doiMatKhau(Long id, String matKhauMoi) {
        NguoiDung user = nguoiDungRepository.findById(id).orElse(null);
        if (user != null) {
            user.setMatKhauMaHoa(matKhauMoi);
            nguoiDungRepository.save(user);
        }
    }

    public byte[] xuatExcelDanhSachNguoiDung() {
        List<NguoiDung> danhSach = layTatCaNguoiDung();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("NguoiDung");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "ID", "Tên đăng nhập", "Họ tên", "Email", "Số điện thoại", "Vai trò" };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (NguoiDung nd : danhSach) {
                var row = sheet.createRow(rowIdx++);
                if (nd.getId() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(nd.getId().doubleValue());
                }
                row.createCell(1).setCellValue(nd.getTenDangNhap() == null ? "" : nd.getTenDangNhap());
                row.createCell(2).setCellValue(nd.getHoTen() == null ? "" : nd.getHoTen());
                row.createCell(3).setCellValue(nd.getEmail() == null ? "" : nd.getEmail());
                row.createCell(4).setCellValue(nd.getSoDienThoai() == null ? "" : nd.getSoDienThoai());
                row.createCell(5).setCellValue(nd.getVaiTro());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất Excel danh sách người dùng", e);
        }
    }

}
