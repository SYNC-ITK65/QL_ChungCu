package com.sync.itk65.service;

import com.sync.itk65.entity.DichVu;
import com.sync.itk65.repository.DichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DichVuService {

    @Autowired
    private DichVuRepository dichVuRepository;

    // 1. Lấy danh sách tất cả dịch vụ
    public List<DichVu> layTatCaDichVu() {
        return dichVuRepository.findAll();
    }

    // 2. Lưu dịch vụ (Dùng chung cho cả Thêm mới và Cập nhật)
    public void luuDichVu(DichVu dichVu) {
        dichVuRepository.save(dichVu);
    }

    // 3. Lấy thông tin dịch vụ theo ID (Dùng để lấy dữ liệu đổ lên Form khi muốn Sửa)
    public DichVu layDichVuTheoId(Long id) {
        return dichVuRepository.findById(id).orElse(null);
    }

    // 4. Xóa dịch vụ
    public void xoaDichVu(Long id) {
        dichVuRepository.deleteById(id);
    }
}