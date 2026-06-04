
-- 1. NGUOI_DUNG
CREATE TABLE nguoi_dung (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dang_nhap     VARCHAR(255) NOT NULL,
    mat_khau_ma_hoa   VARCHAR(255) NOT NULL,
    ho_ten            VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    so_dien_thoai     VARCHAR(255) NOT NULL,
    vai_tro           INT
    -- 1: Admin, 2: Quản lý, 3: Cư dân, 4: Lễ tân, 5: Bảo vệ
);

-- 2. CAN_HO
CREATE TABLE can_ho (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_can_ho   VARCHAR(255) NOT NULL,
    dien_tich   DOUBLE        CHECK (dien_tich >= 1),
    tang        INT           CHECK (tang BETWEEN 1 AND 100),
    loai        VARCHAR(255) NOT NULL,
    trang_thai  VARCHAR(255),
    hinh_anh    VARCHAR(255)
);

-- 3. CU_DAN
CREATE TABLE cu_dan (
    id          BIGINT PRIMARY KEY,
    ma_can_ho   BIGINT,                    
    moi_quan_he VARCHAR(255) NOT NULL,
    trang_thai  VARCHAR(255) NOT NULL,
    ngay_sinh   DATE         NOT NULL,
    cccd        VARCHAR(12)  NOT NULL,
    CONSTRAINT fk_cu_dan_nguoi_dung FOREIGN KEY (id)        REFERENCES nguoi_dung(id),
    CONSTRAINT fk_cu_dan_can_ho     FOREIGN KEY (ma_can_ho) REFERENCES can_ho(id)
);

-- 4. DICH_VU
CREATE TABLE dich_vu (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten         VARCHAR(255),
    don_gia     DOUBLE,
    don_vi_tinh VARCHAR(255),
    loai        VARCHAR(255),
    mo_ta       VARCHAR(255)
);

-- 5. CHI_SO_HANG_THANG
CREATE TABLE chi_so_hang_thang (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    can_ho_id       BIGINT NOT NULL,
    dien_tieu_thu   DOUBLE,
    nuoc_tieu_thu   DOUBLE,
    ngay_ghi_nhan   DATE,
    CONSTRAINT fk_csht_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

-- 6. HOA_DON
CREATE TABLE hoa_don (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    can_ho_id             BIGINT,
    tong_tien             DOUBLE,
    ngay_phat_hanh        DATE,
    ngay_den_han          DATE,
    trang_thai_thanh_toan VARCHAR(255),
    CONSTRAINT fk_hoadon_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

-- 7. THANH_TOAN
CREATE TABLE thanh_toan (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    hoa_don_id      BIGINT NOT NULL,
    so_tien         DOUBLE,
    ngay_thanh_toan DATE,
    phuong_thuc     VARCHAR(255),
    CONSTRAINT fk_tt_hoa_don       FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id),
    CONSTRAINT uk_thanh_toan_hoa_don UNIQUE (hoa_don_id)
);

-- 8. LICH_SU_THANH_TOAN
CREATE TABLE lich_su_thanh_toan (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    hoa_don_id            BIGINT        NOT NULL,
    can_ho_id             BIGINT,
    ma_hoa_don            VARCHAR(255),
    so_tien_thanh_toan    DOUBLE         NOT NULL,
    ngay_thanh_toan       DATETIME      NOT NULL,
    phuong_thuc_thanh_toan VARCHAR(255),
    nguoi_thanh_toan      VARCHAR(255),
    trang_thai_sua        VARCHAR(255),
    ghi_chu               VARCHAR(255),
    CONSTRAINT fk_lstt_hoa_don FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id),
    CONSTRAINT fk_lstt_can_ho  FOREIGN KEY (can_ho_id)  REFERENCES can_ho(id),
    CONSTRAINT uk_hoa_don_id   UNIQUE (hoa_don_id)
);

-- 9. HOP_DONG
CREATE TABLE hop_dong (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    can_ho_id        BIGINT         NOT NULL,
    cu_dan_id        BIGINT         NOT NULL,
    loai_hop_dong    VARCHAR(20),
    gia_tri_hop_dong DOUBLE,
    tien_coc         DOUBLE,
    tien_thue        DOUBLE,
    ngay_bat_dau     DATE,
    ngay_ket_thuc    DATE,
    ben_cho_thue     VARCHAR(255),
    ben_thue         VARCHAR(255),
    trang_thai       VARCHAR(20),
    CONSTRAINT fk_hd_can_ho  FOREIGN KEY (can_ho_id) REFERENCES can_ho(id),
    CONSTRAINT fk_hd_cu_dan  FOREIGN KEY (cu_dan_id) REFERENCES cu_dan(id)
);

-- 10. KIEN_HANG
CREATE TABLE kien_hang (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    can_ho_id   BIGINT,
    nguoi_gui   VARCHAR(255),
    nguoi_nhan  VARCHAR(255),
    ngay_nhan   DATE,
    trang_thai  VARCHAR(255),
    CONSTRAINT fk_kh_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

-- 11. KHAO_SAT
CREATE TABLE khao_sat (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    tieu_de             VARCHAR(255)  NOT NULL,
    mo_ta               TEXT,
    thoi_gian_bat_dau   DATETIME       NOT NULL,
    thoi_gian_ket_thuc  DATETIME       NOT NULL
);

-- 12. LUA_CHON_KHAO_SAT
CREATE TABLE lua_chon_khao_sat (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    khao_sat_id        BIGINT,
    noi_dung_lua_chon  VARCHAR(255) NOT NULL,
    so_luot_binh_chon  INT DEFAULT 0,
    CONSTRAINT fk_lcks_khao_sat FOREIGN KEY (khao_sat_id) REFERENCES khao_sat(id)
);

-- 13. LICH_SU_VOTE
CREATE TABLE lich_su_vote (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cu_dan_id       BIGINT,
    khao_sat_id     BIGINT,
    lua_chon_id     BIGINT,
    thoi_gian_vote  DATETIME,
    CONSTRAINT fk_lsv_cu_dan        FOREIGN KEY (cu_dan_id)   REFERENCES cu_dan(id),
    CONSTRAINT fk_lsv_khao_sat      FOREIGN KEY (khao_sat_id) REFERENCES khao_sat(id),
    CONSTRAINT fk_lsv_lua_chon      FOREIGN KEY (lua_chon_id) REFERENCES lua_chon_khao_sat(id),
    CONSTRAINT uq_lsv_cu_dan_khaosat UNIQUE (cu_dan_id, khao_sat_id)
);

-- 14. TAI_SAN
CREATE TABLE tai_san (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_tai_san              VARCHAR(255) NOT NULL UNIQUE,
    ten_tai_san             VARCHAR(255) NOT NULL,
    vi_tri                  VARCHAR(255) NOT NULL,
    tinh_trang              VARCHAR(255) NOT NULL,
    ngay_mua                DATE          NOT NULL,
    chu_ky_bao_tri          INT           NOT NULL CHECK (chu_ky_bao_tri >= 1),
    ngay_bao_tri_tiep_theo  DATE
);

-- 15. LICH_SU_BAO_TRI
CREATE TABLE lich_su_bao_tri (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    tai_san_id      BIGINT         NOT NULL,
    ngay_bao_tri    DATE           NOT NULL,
    noi_dung        TEXT,
    chi_phi         DOUBLE,
    nguoi_thuc_hien VARCHAR(255),
    CONSTRAINT fk_lsbt_tai_san FOREIGN KEY (tai_san_id) REFERENCES tai_san(id)
);

-- 16. PHAN_ANH
CREATE TABLE phan_anh (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    can_ho_id       BIGINT         NOT NULL,
    tieu_de         VARCHAR(255)  NOT NULL,
    noi_dung        TEXT,
    ngay_gui        DATETIME       NOT NULL,
    phan_hoi        TEXT,
    ngay_phan_hoi   DATETIME,
    trang_thai      VARCHAR(255)  NOT NULL,
    hinh_anh        VARCHAR(255),
    CONSTRAINT fk_pa_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

-- 17. PHUONG_TIEN
CREATE TABLE phuong_tien (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    can_ho_id   BIGINT,
    bien_so     VARCHAR(255),
    loai        VARCHAR(255),
    mau_sac     VARCHAR(255),
    trang_thai  VARCHAR(255),
    hinh_anh    VARCHAR(255),
    CONSTRAINT fk_pt_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

-- 18. DANG_KY_KHACH_THAM
CREATE TABLE dang_ky_khach_tham (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    cu_dan_id           BIGINT         NOT NULL,
    ten_khach           VARCHAR(255),
    cmnd                VARCHAR(255),
    bien_so_xe          VARCHAR(255),
    thoi_gian_du_kien   DATETIME,
    thoi_gian_duyet     DATETIME,
    trang_thai          VARCHAR(255),
    ngay_di             DATETIME,
    CONSTRAINT fk_dkkt_cu_dan FOREIGN KEY (cu_dan_id) REFERENCES cu_dan(id)
);

-- 19. DAT_DICH_VU
CREATE TABLE dat_dich_vu (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cu_dan_id       BIGINT         NOT NULL,
    dich_vu_id      BIGINT         NOT NULL,
    ngay_dat        DATE,
    thoi_gian_duyet DATETIME,
    trang_thai      VARCHAR(255),
    ghi_chu         VARCHAR(255),
    CONSTRAINT fk_ddv_cu_dan  FOREIGN KEY (cu_dan_id)  REFERENCES cu_dan(id),
    CONSTRAINT fk_ddv_dich_vu FOREIGN KEY (dich_vu_id) REFERENCES dich_vu(id)
);

-- 20. THONG_BAO
CREATE TABLE thong_bao (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    tieu_de             VARCHAR(255)  NOT NULL,
    noi_dung            TEXT  NOT NULL,
    loai                INT            NOT NULL,
    ngay_dang           DATETIME,
    doi_tuong_gui       VARCHAR(255)  NOT NULL,
    gia_tri_doi_tuong   VARCHAR(255)
);
