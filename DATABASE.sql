-- ============================================================
-- DATABASE: QL_ChungCu
-- Phiên bản đồng bộ với các Entity Java (JPA/Hibernate)
-- Thứ tự tạo bảng theo phụ thuộc khóa ngoại
-- ============================================================

USe QL_ChungCu;

CREATE TABLE nguoi_dung (
    id                BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_dang_nhap     NVARCHAR(255) NOT NULL,
    mat_khau_ma_hoa   NVARCHAR(255) NOT NULL,
    ho_ten            NVARCHAR(255) NOT NULL,
    email             NVARCHAR(255) NOT NULL,
    so_dien_thoai     NVARCHAR(255) NOT NULL,
    vai_tro           INT
    -- 1: Admin, 2: Quản lý, 3: Cư dân, 4: Lễ tân, 5: Bảo vệ
);

CREATE TABLE can_ho (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_can_ho   NVARCHAR(255) NOT NULL,
    dien_tich   FLOAT         CHECK (dien_tich >= 1),
    tang        INT           CHECK (tang BETWEEN 1 AND 100),
    loai        NVARCHAR(255) NOT NULL,
    trang_thai  NVARCHAR(255),
    hinh_anh    NVARCHAR(255)
);

CREATE TABLE cu_dan (
    id          BIGINT PRIMARY KEY,
    ma_can_ho   BIGINT,                        -- FK -> can_ho.id
    moi_quan_he NVARCHAR(255) NOT NULL,
    trang_thai  NVARCHAR(255) NOT NULL,
    ngay_sinh   DATE         NOT NULL,
    cccd        NVARCHAR(12)  NOT NULL,
    CONSTRAINT fk_cu_dan_nguoi_dung FOREIGN KEY (id)        REFERENCES nguoi_dung(id),
    CONSTRAINT fk_cu_dan_can_ho     FOREIGN KEY (ma_can_ho) REFERENCES can_ho(id)
);

CREATE TABLE dich_vu (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten         NVARCHAR(255),
    don_gia     FLOAT,
    don_vi_tinh NVARCHAR(255),
    loai        NVARCHAR(255),
    mo_ta       NVARCHAR(255)
);

CREATE TABLE chi_so_hang_thang (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    can_ho_id       BIGINT NOT NULL,
    dien_tieu_thu   FLOAT,
    nuoc_tieu_thu   FLOAT,
    ngay_ghi_nhan   DATE,
    CONSTRAINT fk_csht_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

CREATE TABLE hoa_don (
    id                    BIGINT IDENTITY(1,1) PRIMARY KEY,
    can_ho_id             BIGINT,
    tong_tien             FLOAT,
    ngay_phat_hanh        DATE,
    ngay_den_han          DATE,
    trang_thai_thanh_toan NVARCHAR(255),
    CONSTRAINT fk_hoadon_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

CREATE TABLE thanh_toan (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    hoa_don_id      BIGINT NOT NULL,
    so_tien         FLOAT,
    ngay_thanh_toan DATE,
    phuong_thuc     NVARCHAR(255),
    CONSTRAINT fk_tt_hoa_don       FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id),
    CONSTRAINT uk_thanh_toan_hoa_don UNIQUE (hoa_don_id)
);

CREATE TABLE lich_su_thanh_toan (
    id                    BIGINT IDENTITY(1,1) PRIMARY KEY,
    hoa_don_id            BIGINT        NOT NULL,
    can_ho_id             BIGINT,
    ma_hoa_don            NVARCHAR(255),
    so_tien_thanh_toan    FLOAT         NOT NULL,
    ngay_thanh_toan       DATETIME      NOT NULL,
    phuong_thuc_thanh_toan NVARCHAR(255),
    nguoi_thanh_toan      NVARCHAR(255),
    trang_thai_sua        NVARCHAR(255),
    ghi_chu               NVARCHAR(255),
    CONSTRAINT fk_lstt_hoa_don FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id),
    CONSTRAINT fk_lstt_can_ho  FOREIGN KEY (can_ho_id)  REFERENCES can_ho(id),
    CONSTRAINT uk_hoa_don_id   UNIQUE (hoa_don_id)
);

CREATE TABLE hop_dong (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    can_ho_id        BIGINT         NOT NULL,
    cu_dan_id        BIGINT         NOT NULL,
    loai_hop_dong    NVARCHAR(20),
    gia_tri_hop_dong FLOAT,
    tien_coc         FLOAT,
    tien_thue        FLOAT,
    ngay_bat_dau     DATE,
    ngay_ket_thuc    DATE,
    ben_cho_thue     NVARCHAR(255),
    ben_thue         NVARCHAR(255),
    trang_thai       NVARCHAR(20),
    CONSTRAINT fk_hd_can_ho  FOREIGN KEY (can_ho_id) REFERENCES can_ho(id),
    CONSTRAINT fk_hd_cu_dan  FOREIGN KEY (cu_dan_id) REFERENCES cu_dan(id)
);

CREATE TABLE kien_hang (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    can_ho_id   BIGINT,
    nguoi_gui   NVARCHAR(255),
    nguoi_nhan  NVARCHAR(255),
    ngay_nhan   DATE,
    trang_thai  NVARCHAR(255),
    CONSTRAINT fk_kh_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);

CREATE TABLE khao_sat (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    tieu_de             NVARCHAR(255)  NOT NULL,
    mo_ta               NVARCHAR(MAX),
    thoi_gian_bat_dau   DATETIME       NOT NULL,
    thoi_gian_ket_thuc  DATETIME       NOT NULL
);

CREATE TABLE lua_chon_khao_sat (
    id                 BIGINT IDENTITY(1,1) PRIMARY KEY,
    khao_sat_id        BIGINT,
    noi_dung_lua_chon  NVARCHAR(255) NOT NULL,
    so_luot_binh_chon  INT DEFAULT 0,
    CONSTRAINT fk_lcks_khao_sat FOREIGN KEY (khao_sat_id) REFERENCES khao_sat(id)
);

CREATE TABLE lich_su_vote (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    cu_dan_id       BIGINT,
    khao_sat_id     BIGINT,
    lua_chon_id     BIGINT,
    thoi_gian_vote  DATETIME,
    CONSTRAINT fk_lsv_cu_dan        FOREIGN KEY (cu_dan_id)   REFERENCES cu_dan(id),
    CONSTRAINT fk_lsv_khao_sat      FOREIGN KEY (khao_sat_id) REFERENCES khao_sat(id),
    CONSTRAINT fk_lsv_lua_chon      FOREIGN KEY (lua_chon_id) REFERENCES lua_chon_khao_sat(id),
    CONSTRAINT uq_lsv_cu_dan_khaosat UNIQUE (cu_dan_id, khao_sat_id)
);

CREATE TABLE tai_san (
    id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_tai_san              NVARCHAR(255) NOT NULL UNIQUE,
    ten_tai_san             NVARCHAR(255) NOT NULL,
    vi_tri                  NVARCHAR(255) NOT NULL,
    tinh_trang              NVARCHAR(255) NOT NULL,
    ngay_mua                DATE          NOT NULL,
    chu_ky_bao_tri          INT           NOT NULL CHECK (chu_ky_bao_tri >= 1),
    ngay_bao_tri_tiep_theo  DATE
);

CREATE TABLE lich_su_bao_tri (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    tai_san_id      BIGINT         NOT NULL,
    ngay_bao_tri    DATE           NOT NULL,
    noi_dung        NVARCHAR(MAX),
    chi_phi         FLOAT,
    nguoi_thuc_hien NVARCHAR(255),
    CONSTRAINT fk_lsbt_tai_san FOREIGN KEY (tai_san_id) REFERENCES tai_san(id)
);

CREATE TABLE phan_anh (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    can_ho_id       BIGINT         NOT NULL,
    tieu_de         NVARCHAR(255)  NOT NULL,
    noi_dung        NVARCHAR(MAX),
    ngay_gui        DATETIME       NOT NULL,
    phan_hoi        NVARCHAR(MAX),
    ngay_phan_hoi   DATETIME,
    trang_thai      NVARCHAR(255)  NOT NULL,
    hinh_anh        NVARCHAR(255),
    CONSTRAINT fk_pa_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);


CREATE TABLE phuong_tien (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    can_ho_id   BIGINT,
    bien_so     NVARCHAR(255),
    loai        NVARCHAR(255),
    mau_sac     NVARCHAR(255),
    trang_thai  NVARCHAR(255),
    hinh_anh    NVARCHAR(255),
    CONSTRAINT fk_pt_can_ho FOREIGN KEY (can_ho_id) REFERENCES can_ho(id)
);


CREATE TABLE dang_ky_khach_tham (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    cu_dan_id           BIGINT         NOT NULL,
    ten_khach           NVARCHAR(255),
    cmnd                NVARCHAR(255),
    bien_so_xe          NVARCHAR(255),
    thoi_gian_du_kien   DATETIME,
    thoi_gian_duyet     DATETIME,
    trang_thai          NVARCHAR(255),
    ngay_di             DATETIME,
    CONSTRAINT fk_dkkt_cu_dan FOREIGN KEY (cu_dan_id) REFERENCES cu_dan(id)
);

-- 19. DAT_DICH_VU
--     @Entity @Table(name="dat_dich_vu")
CREATE TABLE dat_dich_vu (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    cu_dan_id       BIGINT         NOT NULL,
    dich_vu_id      BIGINT         NOT NULL,
    ngay_dat        DATE,
    ngay_ket_thuc   DATE,
    thoi_gian_duyet DATETIME,
    trang_thai      NVARCHAR(255),
    ghi_chu         NVARCHAR(255),
    CONSTRAINT fk_ddv_cu_dan  FOREIGN KEY (cu_dan_id)  REFERENCES cu_dan(id),
    CONSTRAINT fk_ddv_dich_vu FOREIGN KEY (dich_vu_id) REFERENCES dich_vu(id)
);

-- 20. THONG_BAO
--     @Entity @Table(name="thong_bao")
--     @Column(columnDefinition="NVARCHAR(MAX)") for noi_dung
CREATE TABLE thong_bao (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    tieu_de             NVARCHAR(255)  NOT NULL,
    noi_dung            NVARCHAR(MAX)  NOT NULL,
    loai                INT            NOT NULL,
    ngay_dang           DATETIME,
    doi_tuong_gui       NVARCHAR(255)  NOT NULL,
    gia_tri_doi_tuong   NVARCHAR(255)
    -- loai: 1=Bảng tin chung, 2=Cẩm nang
    -- doi_tuong_gui: ALL, HO_GIA_DINH, NHIEU_HO, TANG
);