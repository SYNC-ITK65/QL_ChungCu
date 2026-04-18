package com.sync.itk65.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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
}