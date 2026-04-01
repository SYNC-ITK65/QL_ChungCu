package com.sync.itk65.service;

import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
