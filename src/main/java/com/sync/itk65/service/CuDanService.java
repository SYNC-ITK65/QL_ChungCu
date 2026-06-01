package com.sync.itk65.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.CuDanRepository;
import com.sync.itk65.repository.NguoiDungRepository;

import jakarta.validation.Valid;

@Service
@Validated
public class CuDanService {

    @Autowired
    private CuDanRepository cuDanRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    private void updateCanHoStatus(CanHo canHo) {
        if (canHo == null || canHo.getId() == null) return;
        
        // Cần tải lại thực thể từ DB để không ghi đè các trường khác thành null 
        // (vì canHo từ form chỉ mang theo id)
        CanHo existingCanHo = canHoRepository.findById(canHo.getId()).orElse(null);
        if (existingCanHo == null) return;
        
        List<CuDan> residents = cuDanRepository.layTatCaCuDanTheoCanHo(existingCanHo.getId());
        boolean hasResident = residents.stream().anyMatch(r -> "Đang Ở".equalsIgnoreCase(r.getTrangThai()) || "Đang ở".equalsIgnoreCase(r.getTrangThai()));
        existingCanHo.setTrangThai(hasResident ? "Đã có chủ" : "Trống");
        canHoRepository.save(existingCanHo);
    }

    // 1. Lấy danh sách tất cả cư dân
    public List<CuDan> layTatCaCuDan() {
        return cuDanRepository.findAll();
    }

    // 2. Lưu thông tin cư dân kèm kiểm tra logic nghiệp vụ Validator
    public void luuCuDan(@Valid CuDan cuDan) {
        try {
            // Kiểm tra trùng lặp Tên đăng nhập
            if (cuDan.getTenDangNhap() != null && !cuDan.getTenDangNhap().isEmpty()) {
                NguoiDung existingUser = nguoiDungRepository.findByTenDangNhap(cuDan.getTenDangNhap());
                if (existingUser != null && (cuDan.getId() == null || !existingUser.getId().equals(cuDan.getId()))) {
                    throw new IllegalArgumentException("Tên đăng nhập: '" + cuDan.getTenDangNhap() + "' đã được sử dụng!");
                }
            }

            // Kiểm tra trùng lặp Căn Cước Công Dân
            List<CuDan> tatCaCuDan = layTatCaCuDan();
            for (CuDan item : tatCaCuDan) {
                if (item.getCccd() != null && item.getCccd().equals(cuDan.getCccd())) {
                    if (cuDan.getId() == null || !item.getId().equals(cuDan.getId())) {
                        throw new IllegalArgumentException("Căn cước công dân: '" + cuDan.getCccd() + "' đã được sử dụng!");
                    }
                }
            }

            CanHo oldCanHo = null;
            if (cuDan.getId() != null) {
                CuDan oldCuDan = cuDanRepository.findById(cuDan.getId()).orElse(null);
                if (oldCuDan != null) {
                    oldCanHo = oldCuDan.getCanHo();
                }
            }

            cuDanRepository.save(cuDan);

            // Cập nhật trạng thái cho căn hộ mới/hiện tại
            if (cuDan.getCanHo() != null) {
                updateCanHoStatus(cuDan.getCanHo());
            }
            // Cập nhật trạng thái cho căn hộ cũ (nếu có chuyển căn hộ)
            if (oldCanHo != null && (cuDan.getCanHo() == null || !oldCanHo.getId().equals(cuDan.getCanHo().getId()))) {
                updateCanHoStatus(oldCanHo);
            }
        } catch (IllegalArgumentException e) {
            // Trả lỗi cụ thể về phía Validator
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Có lỗi hệ thống xảy ra khi lưu Cư dân. Xin vui lòng thử lại sau!");
        }
    }

    // 3. Lấy thông tin cư dân theo ID (ID này trùng với ID Người dùng)
    public CuDan layCuDanTheoId(Long id) {
        return cuDanRepository.findById(id).orElse(null);
    }

    // 4. Xóa cư dân
    public void xoaCuDan(Long id) {
        CuDan cuDan = cuDanRepository.findById(id).orElse(null);
        if (cuDan != null) {
            CanHo canHo = cuDan.getCanHo();
            cuDanRepository.deleteById(id);
            if (canHo != null) {
                updateCanHoStatus(canHo);
            }
        }
    }

    // Hàm tìm kiếm cư dân với các tiêu chí và phân trang dữ liệu
    public Page<CuDan> timKiemCuDan(String tuKhoa, String trangThai, int page, int size) {
        // Cấu hình đối tượng Pageable với trang hiện tại và số lượng phần tử trên mỗi trang
        Pageable pageable = PageRequest.of(page, size);
        return cuDanRepository.timKiemCuDan(tuKhoa, trangThai, pageable);
    }

    // Hàm tìm kiếm cư dân theo ID Căn hộ cụ thể, kết hợp tìm kiếm từ khóa và phân trang
    public Page<CuDan> timKiemTheoCanHo(Long canHoId, String tuKhoa, String trangThai, int page, int size) {
        // Tạo biến phân trang để hỗ trợ truy vấn cơ sở dữ liệu theo từng phần nhỏ
        Pageable pageable = PageRequest.of(page, size);
        return cuDanRepository.layDanhSachCuDanTheoCanHo(canHoId, tuKhoa, trangThai, pageable);
    }

    // Hàm cũ dùng xuất excel
    public List<CuDan> timTheoCanHoThongThuong(Long canHoId) {
        return cuDanRepository.layTatCaCuDanTheoCanHo(canHoId);
    }

    public byte[] xuatExcelDanhSachCuDan(Long canHoId) {
        List<CuDan> danhSachCuDan = (canHoId == null) ? layTatCaCuDan() : timTheoCanHoThongThuong(canHoId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("CuDan");

            int rowIdx = 0;
            var header = sheet.createRow(rowIdx++);
            String[] headers = new String[] { "ID", "Họ tên", "Số điện thoại", "Căn hộ", "Mối quan hệ", "Trạng thái" };
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (CuDan cuDanItem : danhSachCuDan) {
                var row = sheet.createRow(rowIdx++);
                if (cuDanItem.getId() == null) {
                    row.createCell(0).setCellValue("");
                } else {
                    row.createCell(0).setCellValue(cuDanItem.getId().doubleValue());
                }
                row.createCell(1).setCellValue(cuDanItem.getHoTen() == null ? "" : cuDanItem.getHoTen());
                row.createCell(2).setCellValue(cuDanItem.getSoDienThoai() == null ? "" : cuDanItem.getSoDienThoai());
                row.createCell(3).setCellValue(
                        (cuDanItem.getCanHo() != null && cuDanItem.getCanHo().getMaCanHo() != null) ? cuDanItem.getCanHo().getMaCanHo()
                                : "");
                row.createCell(4).setCellValue(cuDanItem.getMoiQuanHe() == null ? "" : cuDanItem.getMoiQuanHe());
                row.createCell(5).setCellValue(cuDanItem.getTrangThai() == null ? "" : cuDanItem.getTrangThai());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không thể xuất Excel danh sách cư dân", e);
        }
    }

    // Import danh sách cư dân từ file Excel
    // Cột Excel: Họ tên | SĐT | Email | Tên đăng nhập | Ngày sinh (yyyy-MM-dd) | CCCD | Mã căn hộ | Mối quan hệ | Trạng thái
    public String importExcelCuDan(MultipartFile file) {
        List<String> danhSachBoQua = new ArrayList<>();
        int soLuongThanhCong = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String hoTen = formatter.formatCellValue(row.getCell(0)).trim();
                String soDienThoai = formatter.formatCellValue(row.getCell(1)).trim();
                String email = formatter.formatCellValue(row.getCell(2)).trim();
                String tenDangNhap = formatter.formatCellValue(row.getCell(3)).trim();
                String ngaySinhStr = formatter.formatCellValue(row.getCell(4)).trim();
                String cccd = formatter.formatCellValue(row.getCell(5)).trim();
                String maCanHo = formatter.formatCellValue(row.getCell(6)).trim();
                String moiQuanHe = formatter.formatCellValue(row.getCell(7)).trim();
                String trangThai = formatter.formatCellValue(row.getCell(8)).trim();

                // Bỏ qua dòng rỗng
                if (hoTen.isEmpty() || cccd.isEmpty()) continue;

                // Kiểm tra trùng CCCD
                if (cuDanRepository.findByCccd(cccd).isPresent()) {
                    danhSachBoQua.add("Dòng " + (i + 1) + ": CCCD '" + cccd + "' đã tồn tại");
                    continue;
                }

                // Tìm căn hộ theo mã
                CanHo canHo = null;
                if (!maCanHo.isEmpty()) {
                    canHo = canHoRepository.findByMaCanHo(maCanHo).orElse(null);
                    if (canHo == null) {
                        danhSachBoQua.add("Dòng " + (i + 1) + ": Mã căn hộ '" + maCanHo + "' không tồn tại");
                        continue;
                    }
                }

                CuDan cuDan = new CuDan();
                cuDan.setHoTen(hoTen);
                cuDan.setSoDienThoai(soDienThoai);
                cuDan.setEmail(email.isEmpty() ? hoTen.replaceAll("\\s+", "").toLowerCase() + "@gmail.com" : email);
                cuDan.setTenDangNhap(tenDangNhap.isEmpty() ? cccd : tenDangNhap);
                cuDan.setMatKhauMaHoa("1234"); // Mật khẩu mặc định
                cuDan.setVaiTro(3); // Vai trò: Cư dân
                cuDan.setCccd(cccd);
                cuDan.setCanHo(canHo);
                cuDan.setMoiQuanHe(moiQuanHe.isEmpty() ? "Chủ Hộ" : moiQuanHe);
                cuDan.setTrangThai(trangThai.isEmpty() ? "Đang Ở" : trangThai);

                // Parse ngày sinh
                try {
                    if (!ngaySinhStr.isEmpty()) {
                        cuDan.setNgaySinh(LocalDate.parse(ngaySinhStr));
                    } else {
                        cuDan.setNgaySinh(LocalDate.of(2000, 1, 1));
                    }
                } catch (Exception e) {
                    cuDan.setNgaySinh(LocalDate.of(2000, 1, 1));
                }

                cuDanRepository.save(cuDan);
                soLuongThanhCong++;

                // Cập nhật trạng thái căn hộ
                if (canHo != null) {
                    updateCanHoStatus(canHo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc file Excel: " + e.getMessage(), e);
        }

        StringBuilder ketQua = new StringBuilder();
        ketQua.append("Import thành công ").append(soLuongThanhCong).append(" cư dân.");
        if (!danhSachBoQua.isEmpty()) {
            ketQua.append(" Bỏ qua ").append(danhSachBoQua.size()).append(" dòng: ");
            ketQua.append(String.join("; ", danhSachBoQua));
        }
        return ketQua.toString();
    }

}
