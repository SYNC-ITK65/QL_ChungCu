package com.sync.itk65.service;

import com.sync.itk65.entity.*;
import com.sync.itk65.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class KhaoSatService {

    @Autowired private KhaoSatRepository khaoSatRepo;
    @Autowired private LuaChonKhaoSatRepository luaChonRepo;
    @Autowired private LichSuVoteRepository lichSuVoteRepo;

    public List<KhaoSat> layTatCa() { return khaoSatRepo.findAllByOrderByThoiGianBatDauDesc(Pageable.unpaged()).getContent(); } // Keep for backward compatibility if needed

    public Page<KhaoSat> layTatCa(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return khaoSatRepo.findAllByOrderByThoiGianBatDauDesc(pageable);
    }
    public KhaoSat findById(Long id) { return khaoSatRepo.findById(id).orElse(null); }

    @Transactional
    public void luuKhaoSat(KhaoSat khaoSat) {
        if (khaoSat.getDanhSachLuaChon() != null) {
            for (LuaChonKhaoSat lc : khaoSat.getDanhSachLuaChon()) {
                lc.setKhaoSat(khaoSat); // Set khóa ngoại
                if (lc.getSoLuotBinhChon() == null) {
                    lc.setSoLuotBinhChon(0);
                }
            }
        }
        khaoSatRepo.save(khaoSat);
    }

    @Transactional
    public String xoaKhaoSat(Long id) {
        KhaoSat ks = findById(id);
        if (ks == null) return "Không tìm thấy!";
        if (ks.getThoiGianKetThuc().isBefore(LocalDateTime.now())) return "Không thể xóa khảo sát đã quá hạn!";
        if (ks.getTongSoVote() > 0) return "Không thể xóa vì đã có người bình chọn!";
        khaoSatRepo.delete(ks);
        return "SUCCESS";
    }

    @Transactional
    public void ketThucSom(Long id) {
        KhaoSat ks = findById(id);
        if (ks != null && ks.getTrangThaiHienTai().equals("ĐANG MỞ")) {
            ks.setThoiGianKetThuc(LocalDateTime.now());
            khaoSatRepo.save(ks);
        }
    }

    // Luồng Cư Dân Vote (Bảo mật 100%)
    @Transactional
    public String thucHienBinhChon(CuDan cuDan, Long khaoSatId, Long luaChonMoiId) {
        KhaoSat ks = findById(khaoSatId);
        LocalDateTime now = LocalDateTime.now();

        if (ks == null || !ks.getTrangThaiHienTai().equals("ĐANG MỞ")) {
            return "Khảo sát hiện không mở để bình chọn!";
        }

        LuaChonKhaoSat lcMoi = luaChonRepo.findById(luaChonMoiId).orElse(null);
        if (lcMoi == null || !lcMoi.getKhaoSat().getId().equals(khaoSatId)) return "Lựa chọn không hợp lệ!";

        Optional<LichSuVote> lichSuOpt = lichSuVoteRepo.findByCuDanAndKhaoSat(cuDan, ks);

        if (lichSuOpt.isEmpty()) {
            // Vote lần đầu
            lcMoi.setSoLuotBinhChon(lcMoi.getSoLuotBinhChon() + 1);
            LichSuVote ls = new LichSuVote();
            ls.setCuDan(cuDan); ls.setKhaoSat(ks); ls.setLuaChonDaNgan(lcMoi);
            lichSuVoteRepo.save(ls);
            return "SUCCESS_VOTE";
        } else {
            // Đổi vote
            LichSuVote ls = lichSuOpt.get();
            LuaChonKhaoSat lcCu = ls.getLuaChonDaNgan();
            if (lcCu.getId().equals(luaChonMoiId)) return "SUCCESS_NO_CHANGE"; // Không đổi ý

            lcCu.setSoLuotBinhChon(lcCu.getSoLuotBinhChon() - 1);
            lcMoi.setSoLuotBinhChon(lcMoi.getSoLuotBinhChon() + 1);
            ls.setLuaChonDaNgan(lcMoi); ls.setThoiGianVote(now);

            return "SUCCESS_UPDATE";
        }
    }
}