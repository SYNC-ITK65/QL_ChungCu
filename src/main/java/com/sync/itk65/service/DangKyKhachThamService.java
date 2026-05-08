package com.sync.itk65.service;
import com.sync.itk65.entity.DangKyKhachTham;
import com.sync.itk65.repository.DangKyKhachThamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DangKyKhachThamService {
    @Autowired private DangKyKhachThamRepository repository;
    public void luu(DangKyKhachTham khach) { repository.save(khach); }
    public Page<DangKyKhachTham> layTatCa(int page, int size) { return repository.findAllByOrderByThoiGianDuKienDesc(PageRequest.of(page, size)); }
    
    public Page<DangKyKhachTham> searchKhachTham(String tuKhoa, String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchKhachTham(tuKhoa, trangThai, pageable);
    }

    public List<DangKyKhachTham> layTatCa() { return repository.findAll(); }
    public List<DangKyKhachTham> layLichSuCuaCuDan(Long cuDanId) { return repository.findByCuDanIdOrderByThoiGianDuKienDesc(cuDanId); }
    public DangKyKhachTham timTheoId(Long id) { return repository.findById(id).orElse(null); }
    public void xoa(Long id) {
        repository.deleteById(id);
    }
}