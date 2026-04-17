package com.sync.itk65.service;

import com.sync.itk65.entity.PhuongTien;
import com.sync.itk65.repository.PhuongTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class PhuongTienService {

    @Autowired
    private PhuongTienRepository phuongTienRepository;

    //  Đăng ký xe mới
    public PhuongTien dangKyXe(PhuongTien xe) {
        if (phuongTienRepository.existsByBienSoXe(xe.getBienSoXe())) {
            throw new RuntimeException("Biển số xe đã tồn tại!");
        }
        // Nếu trangThai chưa có (thường là từ form cư dân), mặc định là Chờ duyệt
        if (xe.getTrangThai() == null) {
            xe.setTrangThai("Chờ duyệt");
        }
        return phuongTienRepository.save(xe);
    }

    // 2. Hàm DUYỆT XE (Quan trọng - bạn đang thiếu cái này)
    public void duyetXe(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Đã duyệt");
        phuongTienRepository.save(xe);
    }

    public void tuChoiXe(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Từ chối");
        phuongTienRepository.save(xe);
    }

    public void huyDuyetXe(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Hủy duyệt");
        phuongTienRepository.save(xe);
    }

    public void suaLaiTrangThaiChoDuyet(Long id) {
        PhuongTien xe = phuongTienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe ID: " + id));
        xe.setTrangThai("Chờ duyệt");
        phuongTienRepository.save(xe);
    }

    //Xem danh sách bãi xe ---
    public Page<PhuongTien> danhSachXe(int page, int size) {
        Page<PhuongTien> ds = phuongTienRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));
        ds.forEach(x -> x.setTrangThai(chuanHoaTrangThai(x.getTrangThai())));
        return ds;
    }

    public List<PhuongTien> danhSachXe() {
        List<PhuongTien> ds = phuongTienRepository.findAll();
        ds.forEach(x -> x.setTrangThai(chuanHoaTrangThai(x.getTrangThai())));
        return ds;
    }

    public List<PhuongTien> layXeTheoCanHoId(Long canHoId) {
        List<PhuongTien> ds = phuongTienRepository.findByCanHoId(canHoId);
        ds.forEach(x -> x.setTrangThai(chuanHoaTrangThai(x.getTrangThai())));
        return ds;
    }

    //Hủy gửi xe ---
    public void huyGuiXe(Long id) {
        if (!phuongTienRepository.existsById(id)) {
            throw new RuntimeException("Lỗi: Không tìm thấy ID xe này để xóa!");
        }
        phuongTienRepository.deleteById(id);
    }

    private String chuanHoaTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isBlank()) return "Chờ duyệt";
        String raw = trangThai.trim();
        String normalized = boDau(raw).toLowerCase();
        if (normalized.contains("huy") && normalized.contains("duyet")) return "Hủy duyệt";
        if (normalized.contains("tu choi")) return "Từ chối";
        if ((normalized.contains("cho") && normalized.contains("duyet")) || normalized.contains("dang ky moi")) return "Chờ duyệt";
        if (normalized.contains("da") && normalized.contains("duyet")) return "Đã duyệt";
        return raw;
    }

    private String boDau(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.replace('đ', 'd').replace('Đ', 'D');
    }
}