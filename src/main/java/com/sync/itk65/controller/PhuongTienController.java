package com.sync.itk65.controller;

import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.service.PhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Khai báo trả về dữ liệu JSON (REST API)
@RequestMapping("/api/phuong-tien") // Địa chỉ mặt tiền để khách tìm đến
public class PhuongTienController {

    @Autowired
    private PhuongTienService phuongTienService;

    // Khách dùng phương thức GET để lấy dữ liệu
    @GetMapping("/danh-sach")
    public List<PhuongTien> layDanhSach() {
        return phuongTienService.danhSachXe();
    }

    // Khách dùng phương thức POST để gửi dữ liệu lên
    @PostMapping("/dang-ky")
    public ResponseEntity<?> dangKy(@RequestBody PhuongTien xe) {
        try {
            // Nhờ Service đăng ký. Nếu thành công thì trả về thông tin chiếc xe đó kèm mã 200 (OK)
            PhuongTien xeMoi = phuongTienService.dangKyXe(xe);
            return ResponseEntity.ok(xeMoi);
        } catch (RuntimeException e) {
            // Nếu Service báo lỗi (trùng biển số), thì trả về câu lỗi đó cho khách kèm mã 400 (Bad Request)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Khách dùng phương thức DELETE truyền kèm cái ID xe muốn xóa trên đường dẫn
    @DeleteMapping("/huy/{id}")
    public ResponseEntity<?> huyXe(@PathVariable Long id) {
        try {
            phuongTienService.huyGuiXe(id);
            return ResponseEntity.ok("Đã hủy gửi xe thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}