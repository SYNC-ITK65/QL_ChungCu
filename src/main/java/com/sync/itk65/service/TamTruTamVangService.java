package com.sync.itk65.service;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.TamTruTamVang;
import com.sync.itk65.repository.TamTruTamVangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TamTruTamVangService {

    @Autowired
    private TamTruTamVangRepository repository;

    public Page<TamTruTamVang> getAll(int page, int size) { return repository.findAllByOrderByIdDesc(PageRequest.of(page, size)); }
    
    public Page<TamTruTamVang> searchTamTruTamVang(String loai, String trangThaiDuyet, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchTamTruTamVang(loai, trangThaiDuyet, pageable);
    }

    public List<TamTruTamVang> getAll() { return repository.findAll(); }
    public List<TamTruTamVang> getByCuDan(CuDan cuDan) { return repository.findByCuDanOrderByIdDesc(cuDan); }
    public TamTruTamVang getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn với ID: " + id));
    }

    @Transactional
    public void create(TamTruTamVang entity) {
        validateDates(entity.getNgayBatDau(), entity.getNgayKetThuc(), true);
        entity.setTrangThaiDuyet("Chờ duyệt");
        repository.save(entity);
    }

    @Transactional
    public void update(Long id, TamTruTamVang data, CuDan cuDan) {
        TamTruTamVang existing = getById(id);
        if (!existing.getCuDan().getId().equals(cuDan.getId())) throw new IllegalArgumentException("Không có quyền!");
        if (!"Chờ duyệt".equals(existing.getTrangThaiDuyet())) throw new IllegalArgumentException("Chỉ sửa khi Chờ duyệt.");

        validateDates(data.getNgayBatDau(), data.getNgayKetThuc(), false);
        existing.setLoai(data.getLoai());
        existing.setNgayBatDau(data.getNgayBatDau());
        existing.setNgayKetThuc(data.getNgayKetThuc());
        existing.setLyDo(data.getLyDo());
        existing.setHoTenKhach(data.getHoTenKhach());
        existing.setCccdKhach(data.getCccdKhach());
        repository.save(existing);
    }

    @Transactional
    public void delete(Long id, CuDan cuDan) {
        TamTruTamVang existing = getById(id);
        if (!existing.getCuDan().getId().equals(cuDan.getId())) throw new IllegalArgumentException("Không có quyền!");
        if (!"Chờ duyệt".equals(existing.getTrangThaiDuyet())) throw new IllegalArgumentException("Chỉ xóa khi Chờ duyệt.");
        repository.delete(existing);
    }

    @Transactional
    public void ketThucSom(Long id, CuDan cuDan) {
        TamTruTamVang existing = getById(id);
        if (!existing.getCuDan().getId().equals(cuDan.getId())) throw new IllegalArgumentException("Không có quyền!");
        if (!"Đã duyệt".equals(existing.getTrangThaiDuyet())) throw new IllegalArgumentException("Đơn chưa duyệt.");

        existing.setNgayKetThuc(LocalDate.now());
        existing.setTrangThaiDuyet("Đã hoàn thành");
        repository.save(existing);
    }

    @Transactional
    public void processByAdmin(Long id, String action, String lyDoTuChoi) {
        TamTruTamVang existing = getById(id);
        if (!"Chờ duyệt".equals(existing.getTrangThaiDuyet())) throw new IllegalArgumentException("Đơn đã xử lý.");

        if ("approve".equals(action)) {
            existing.setTrangThaiDuyet("Đã duyệt");
        } else if ("reject".equals(action)) {
            if (lyDoTuChoi == null || lyDoTuChoi.trim().isEmpty()) throw new IllegalArgumentException("Nhập lý do từ chối.");
            existing.setTrangThaiDuyet("Từ chối");
            existing.setLyDoTuChoi(lyDoTuChoi);
        }
        repository.save(existing);
    }

    @Transactional
    public void undoByAdmin(Long id) {
        TamTruTamVang existing = getById(id);
        if ("Chờ duyệt".equals(existing.getTrangThaiDuyet()) ||
                "Đã hoàn thành".equals(existing.getTrangThaiDuyet()) ||
                "Quá hạn duyệt".equals(existing.getTrangThaiDuyet())) {
            throw new IllegalArgumentException("Không thể hoàn tác trạng thái này.");
        }
        existing.setTrangThaiDuyet("Chờ duyệt");
        existing.setLyDoTuChoi(null);
        repository.save(existing);
    }

    private void validateDates(LocalDate start, LocalDate end, boolean isCreate) {
        if (start == null || end == null) throw new IllegalArgumentException("Nhập đủ ngày tháng.");
        if (isCreate && start.isBefore(LocalDate.now())) throw new IllegalArgumentException("Ngày bắt đầu >= hôm nay.");
        if (!end.isAfter(start)) throw new IllegalArgumentException("Ngày kết thúc > ngày bắt đầu.");
    }

    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void autoUpdateOverdueStatus() {
        LocalDate today = LocalDate.now();

        List<TamTruTamVang> daDuyet = repository.findByTrangThaiDuyetAndNgayKetThucBefore("Đã duyệt", today);
        daDuyet.forEach(d -> d.setTrangThaiDuyet("Đã hoàn thành"));
        repository.saveAll(daDuyet);

        List<TamTruTamVang> choDuyet = repository.findByTrangThaiDuyetAndNgayKetThucBefore("Chờ duyệt", today);
        choDuyet.forEach(d -> d.setTrangThaiDuyet("Quá hạn duyệt"));
        repository.saveAll(choDuyet);
    }
}