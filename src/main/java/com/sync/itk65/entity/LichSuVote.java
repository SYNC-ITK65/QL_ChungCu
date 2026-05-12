package com.sync.itk65.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "lich_su_vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cu_dan_id", "khao_sat_id"})
})
@Data
public class LichSuVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cu_dan_id")
    private CuDan cuDan;

    @ManyToOne
    @JoinColumn(name = "khao_sat_id")
    private KhaoSat khaoSat;

    @ManyToOne
    @JoinColumn(name = "lua_chon_id")
    private LuaChonKhaoSat luaChonDaNgan;

    private LocalDateTime thoiGianVote = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CuDan getCuDan() {
        return cuDan;
    }

    public void setCuDan(CuDan cuDan) {
        this.cuDan = cuDan;
    }

    public KhaoSat getKhaoSat() {
        return khaoSat;
    }

    public void setKhaoSat(KhaoSat khaoSat) {
        this.khaoSat = khaoSat;
    }

    public LuaChonKhaoSat getLuaChonDaNgan() {
        return luaChonDaNgan;
    }

    public void setLuaChonDaNgan(LuaChonKhaoSat luaChonDaNgan) {
        this.luaChonDaNgan = luaChonDaNgan;
    }

    public LocalDateTime getThoiGianVote() {
        return thoiGianVote;
    }

    public void setThoiGianVote(LocalDateTime thoiGianVote) {
        this.thoiGianVote = thoiGianVote;
    }
}