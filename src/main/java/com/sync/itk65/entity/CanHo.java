package com.sync.itk65.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "can_ho")
public class CanHo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_can_ho")
    private String maCanHo;

    @Column(name = "dien_tich")
    private Double dienTich;

    @Column(name = "trang_thai")
    private String trangThai;

    // Bôi đen các biến trên, ấn Alt + Insert (hoặc chuột phải chọn Generate) -> Chọn Getter and Setter để nó tự tạo hàm nhé
}