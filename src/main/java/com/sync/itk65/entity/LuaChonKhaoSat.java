package com.sync.itk65.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lua_chon_khao_sat")
@Data
public class LuaChonKhaoSat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String noiDungLuaChon;

    private Integer soLuotBinhChon = 0;

    @ManyToOne
    @JoinColumn(name = "khao_sat_id")
    private KhaoSat khaoSat;

    public Integer getSoLuotBinhChon() {
        return soLuotBinhChon == null ? 0 : soLuotBinhChon;
    }
}