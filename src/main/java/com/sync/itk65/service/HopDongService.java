package com.sync.itk65.service;

import com.sync.itk65.entity.CanHo;
import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.HopDong;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.CuDanRepository;
import com.sync.itk65.repository.HopDongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HopDongService {

    @Autowired
    private HopDongRepository hopDongRepository;

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private CuDanRepository cuDanRepository;

    public List<HopDong> layTatCaHopDong() {
        return hopDongRepository.findAllOrderByIdDesc();
    }

    public List<HopDong> layHopDongCuaCuDan(Long cuDanId) {
        return hopDongRepository.findByCuDanId(cuDanId);
    }

    public HopDong layHopDongTheoId(Long id) {
        return hopDongRepository.findById(id).orElse(null);
    }

    public Optional<HopDong> layHopDongTheoIdVaCuDan(Long hopDongId, Long cuDanId) {
        return hopDongRepository.findByIdAndCuDanId(hopDongId, cuDanId);
    }

    public HopDong taoHopDong(HopDong hopDong, Long canHoId, Long cuDanId) {
        CanHo canHo = canHoRepository.findById(canHoId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ."));
        CuDan cuDan = cuDanRepository.findById(cuDanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cư dân."));

        if (hopDong.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống.");
        }
        if (hopDong.getNgayBatDau() != null && hopDong.getNgayKetThuc() != null
                && hopDong.getNgayKetThuc().isBefore(hopDong.getNgayBatDau())) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu.");
        }
        if (hopDong.getGiaTriHopDong() == null || hopDong.getGiaTriHopDong() <= 0) {
            throw new RuntimeException("Giá trị hợp đồng không hợp lệ.");
        }
        if (!"Thue".equalsIgnoreCase(hopDong.getLoaiHopDong())
                && !"Mua".equalsIgnoreCase(hopDong.getLoaiHopDong())) {
            throw new RuntimeException("Loại hợp đồng chỉ chấp nhận Thuê hoặc Mua.");
        }
        if (cuDan.getCanHo() == null || !cuDan.getCanHo().getId().equals(canHo.getId())) {
            throw new RuntimeException("Cư dân không thuộc căn hộ đã chọn.");
        }

        hopDong.setCanHo(canHo);
        hopDong.setCuDan(cuDan);

        if ("Thue".equalsIgnoreCase(hopDong.getLoaiHopDong())) {
            hopDong.setLoaiHopDong("Thuê");
        } else {
            hopDong.setLoaiHopDong("Mua");
        }
        if ("Mua".equalsIgnoreCase(hopDong.getLoaiHopDong())) {
            hopDong.setNgayKetThuc(null);
        }

        boolean biTrung;
        if (hopDong.getNgayKetThuc() == null) {
            biTrung = hopDongRepository.existsActiveOverlapOpenEnded(
                    canHo.getId(),
                    hopDong.getLoaiHopDong(),
                    hopDong.getNgayBatDau()
            );
        } else {
            biTrung = hopDongRepository.existsActiveOverlapWithEndDate(
                    canHo.getId(),
                    hopDong.getLoaiHopDong(),
                    hopDong.getNgayBatDau(),
                    hopDong.getNgayKetThuc()
            );
        }
        if (biTrung) {
            throw new RuntimeException("Đã tồn tại hợp đồng ACTIVE trùng thời gian cho căn hộ này.");
        }
        hopDong.setTrangThai("ACTIVE");

        return hopDongRepository.save(hopDong);
    }

    public void capNhatTrangThai(Long hopDongId, String trangThaiMoi) {
        HopDong hopDong = hopDongRepository.findById(hopDongId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng."));
        if (!"ACTIVE".equalsIgnoreCase(trangThaiMoi)
                && !"EXPIRED".equalsIgnoreCase(trangThaiMoi)
                && !"TERMINATED".equalsIgnoreCase(trangThaiMoi)) {
            throw new RuntimeException("Trạng thái hợp đồng không hợp lệ.");
        }
        hopDong.setTrangThai(trangThaiMoi.toUpperCase());
        hopDongRepository.save(hopDong);
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void tuDongHetHanHopDong() {
        List<HopDong> expiredContracts = hopDongRepository.findExpiredActiveContracts(LocalDate.now());
        for (HopDong hopDong : expiredContracts) {
            hopDong.setTrangThai("EXPIRED");
        }
    }
}
