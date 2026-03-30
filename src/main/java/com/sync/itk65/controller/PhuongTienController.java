package com.sync.itk65.controller;

import com.sync.itk65.entity.PhuongTien;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/phuong-tien")
public class PhuongTienController {

    private List<PhuongTien> danhSachXe = new ArrayList<>();
    private Long currentId = 1L;

    @PostMapping("/dang-ky")
    public ResponseEntity<PhuongTien> DangKiGuiXe(@RequestBody PhuongTien phuongTien) {
        phuongTien.setId(currentId++);
        danhSachXe.add(phuongTien);
        return new ResponseEntity<>(phuongTien, HttpStatus.CREATED);
    }

    @DeleteMapping("/huy/{id}")
    public ResponseEntity<Void> HuyGuiXe(@PathVariable Long id) {
        danhSachXe.removeIf(xe -> xe.getId().equals(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/danh-sach")
    public ResponseEntity<List<PhuongTien>> DanhSachBaiXe() {
        return new ResponseEntity<>(danhSachXe, HttpStatus.OK);
    }
}