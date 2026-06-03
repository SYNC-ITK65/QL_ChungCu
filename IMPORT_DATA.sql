-- ============================================================
-- IMPORT_DATA.sql - Dữ liệu mẫu cho hệ thống Quản lý Chung cư
-- Mỗi bảng ~20 bản ghi (trừ bảng tự tạo: hoa_don, thanh_toan, lich_su_thanh_toan)
-- Chạy sau khi đã chạy DATABASE.sql
-- ============================================================

USE QL_ChungCu;
SET NOCOUNT ON;

-- ============================================================
-- 1. NGUOI_DUNG (~20 bản ghi)
-- Vai trò: 1=Admin, 2=Quản lý, 3=Cư dân, 4=Lễ tân, 5=Bảo vệ
-- Mật khẩu mặc định "123456" (sẽ được mã hóa BCrypt qua DataInitializer)
-- ============================================================
INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau_ma_hoa, ho_ten, email, so_dien_thoai, vai_tro) VALUES
('admin',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Nguyễn Quản Trị',       'admin@chungcu.com',         '0901234567', 1),
('quanly01',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Trần Văn Quản Lý',      'quanly01@chungcu.com',      '0912345671', 2),
('quanly02',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Lê Thị Quản Lý',       'quanly02@chungcu.com',      '0912345672', 2),
('letan01',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Phạm Văn Lễ Tân',      'letan01@chungcu.com',       '0912345673', 4),
('letan02',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Hoàng Thị Lễ Tân',     'letan02@chungcu.com',       '0912345674', 4),
('baove01',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Võ Văn Bảo Vệ A',      'baove01@chungcu.com',       '0912345675', 5),
('baove02',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Đặng Văn Bảo Vệ B',    'baove02@chungcu.com',       '0912345676', 5),
-- Cư dân (vai_tro = 3) - id sẽ là 8..20
('levan_thang', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Lê Văn Thắng',        'levan.thang@gmail.com',     '0987654321', 3),
('nguyen_mai',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Nguyễn Thị Mai',      'nguyen.mai@gmail.com',      '0987654322', 3),
('pham_hoang',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Phạm Minh Hoàng',     'pham.hoang@gmail.com',      '0977112233', 3),
('tran_thuy',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Trần Bích Thủy',      'tran.thuy@gmail.com',       '0966445566', 3),
('dang_hung',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Đặng Văn Hùng',       'dang.hung@gmail.com',       '0955889900', 3),
('hoang_anh',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Hoàng Lan Anh',       'hoang.anh@gmail.com',       '0944773322', 3),
('vu_quocanh',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Vũ Quốc Anh',         'vu.quocanh@gmail.com',      '0933221100', 3),
('ngo_thanvan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Ngô Thanh Vân',        'ngo.thanvan@gmail.com',     '0922334455', 3),
('do_minhtuan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Đỗ Minh Tuấn',        'do.minhtuan@gmail.com',     '0911223344', 3),
('bui_thuha',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Bùi Thu Hà',           'bui.thuha@gmail.com',       '0900112233', 3),
('cao_phuong',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Cao Ngọc Phương',     'cao.phuong@gmail.com',      '0889012345', 3),
('ly_kimphi',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Lý Kim Phi',           'ly.kimphi@gmail.com',       '0878123456', 3),
('trinh_son',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVwhSKE2/6', N'Trịnh Văn Sơn',        'trinh.son@gmail.com',       '0867234567', 3);
-- Tổng: 20 bản ghi | IDs: 1-7 = nhân viên, 8-20 = cư dân

-- ============================================================
-- 2. CAN_HO (20 bản ghi)
-- IDs: 1-20
-- trang_thai lưu trong DB nhưng thực tế tính qua CuDan.trangThai
-- ============================================================
INSERT INTO can_ho (ma_can_ho, dien_tich, tang, loai, trang_thai, hinh_anh) VALUES
('A1-01', 75.5,  1,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'a101.jpg'),
('A1-02', 45.0,  1,  N'Studio',             N'Đã có chủ',     'a102.jpg'),
('A1-03', 75.5,  1,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'a103.jpg'),
('A2-01', 80.0,  2,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'a201.jpg'),
('A2-02', 55.0,  2,  N'Studio',             N'Đã có chủ',     'a202.jpg'),
('A3-01', 90.0,  3,  N'Duplex',             N'Đã có chủ',     'a301.jpg'),
('A3-02', 75.5,  3,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'a302.jpg'),
('B1-01', 65.0,  1,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'b101.jpg'),
('B2-01', 60.0,  2,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'b201.jpg'),
('B3-01', 85.0,  3,  N'Duplex',             N'Đã có chủ',     'b301.jpg'),
('B4-01', 70.0,  4,  N'Căn hộ tiêu chuẩn', N'Đã có chủ',     'b401.jpg'),
('C1-01', 100.0, 1,  N'Căn hộ cao cấp',    N'Đã có chủ',     'c101.jpg'),
('C2-01', 95.0,  2,  N'Căn hộ cao cấp',    N'Đã có chủ',     'c201.jpg'),
('C3-01', 110.0, 3,  N'Penthouse',          N'Trống',          'c301.jpg'),
('C4-01', 120.0, 4,  N'Penthouse',          N'Trống',          'c401.jpg'),
('D1-01', 75.5,  1,  N'Căn hộ tiêu chuẩn', N'Đang sửa chữa', 'd101.jpg'),
('D2-01', 80.0,  2,  N'Căn hộ tiêu chuẩn', N'Trống',          'd201.jpg'),
('D3-01', 65.0,  3,  N'Studio',             N'Trống',          'd301.jpg'),
('E1-01', 90.0,  1,  N'Duplex',             N'Trống',          'e101.jpg'),
('E2-01', 75.5,  2,  N'Căn hộ tiêu chuẩn', N'Trống',          'e201.jpg');
-- Tổng: 20 căn hộ | IDs: 1-20

-- ============================================================
-- 3. CU_DAN (13 bản ghi - ứng với 13 cư dân trong nguoi_dung id 8-20)
-- Chú ý: id phải khớp với nguoi_dung.id, ma_can_ho là khóa ngoại
-- Gán các cư dân vào căn hộ đang ở (can_ho_id 1-13)
-- trang_thai CuDan: "Đang ở" / "Đã chuyển đi" (ảnh hưởng tính trangThai CanHo)
-- ============================================================
INSERT INTO cu_dan (id, ma_can_ho, moi_quan_he, trang_thai, ngay_sinh, cccd) VALUES
( 8,  1, N'Chủ hộ',      N'Đang ở',       '1985-05-15', '001085001001'),
( 9,  1, N'Vợ/Chồng',    N'Đang ở',       '1988-08-20', '001088001002'),
(10,  2, N'Chủ hộ',      N'Đang ở',       '1990-10-20', '001090002001'),
(11,  3, N'Chủ hộ',      N'Đang ở',       '1988-03-12', '001088003001'),
(12,  4, N'Chủ hộ',      N'Đang ở',       '1992-07-08', '001092004001'),
(13,  5, N'Chủ hộ',      N'Đang ở',       '1998-12-25', '001098005001'),
(14,  6, N'Chủ hộ',      N'Đang ở',       '1993-02-14', '001093006001'),
(15,  7, N'Chủ hộ',      N'Đang ở',       '1994-06-30', '001094007001'),
(16,  8, N'Chủ hộ',      N'Đang ở',       '1991-11-11', '001091008001'),
(17,  9, N'Chủ hộ',      N'Đang ở',       '1987-04-05', '001087009001'),
(18, 10, N'Chủ hộ',      N'Đang ở',       '1995-09-22', '001095010001'),
(19, 11, N'Chủ hộ',      N'Đang ở',       '1983-01-30', '001083011001'),
(20, 12, N'Chủ hộ',      N'Đang ở',       '1996-07-17', '001096012001');
-- Tổng: 13 cư dân | Căn 1 có 2 cư dân (id 8+9), căn 2-12 mỗi căn 1 cư dân
-- Căn 13-20 = Trống (chưa có cư dân đang ở)

-- ============================================================
-- 4. DICH_VU (20 bản ghi)
-- ============================================================
INSERT INTO dich_vu (ten, don_gia, don_vi_tinh, loai, mo_ta) VALUES
(N'Gửi xe máy',           150000,   N'Chiếc/Tháng',   N'Gửi xe',      N'Phí trông giữ xe máy hàng tháng'),
(N'Gửi ô tô',            1200000,  N'Chiếc/Tháng',   N'Gửi xe',      N'Phí trông giữ ô tô hàng tháng'),
(N'Gửi xe đạp',           50000,   N'Chiếc/Tháng',   N'Gửi xe',      N'Phí trông giữ xe đạp hàng tháng'),
(N'Vệ sinh căn hộ nhỏ',   400000,  N'Lần',           N'Dọn dẹp',     N'Dọn dẹp căn hộ dưới 60m2'),
(N'Vệ sinh căn hộ vừa',   500000,  N'Lần',           N'Dọn dẹp',     N'Dọn dẹp căn hộ 60-90m2'),
(N'Vệ sinh căn hộ lớn',   650000,  N'Lần',           N'Dọn dẹp',     N'Dọn dẹp căn hộ trên 90m2'),
(N'Bảo trì máy lạnh',     200000,  N'Máy/Lần',       N'Sửa chữa',    N'Vệ sinh và nạp gas máy lạnh'),
(N'Sửa chữa điện nước',   300000,  N'Lần',           N'Sửa chữa',    N'Dịch vụ sửa chữa điện nước nhỏ'),
(N'Phí quản lý',           12000,  N'm2/Tháng',       N'Cố định',     N'Phí quản lý vận hành chung cư'),
(N'Internet 100Mbps',     250000,  N'Gói/Tháng',     N'Viễn thông',  N'Gói mạng tốc độ 100Mbps'),
(N'Internet 200Mbps',     350000,  N'Gói/Tháng',     N'Viễn thông',  N'Gói mạng tốc độ 200Mbps'),
(N'Truyền hình cáp',      150000,  N'Gói/Tháng',     N'Viễn thông',  N'Gói truyền hình cáp cơ bản'),
(N'Thẻ Gym tháng',        300000,  N'Người/Tháng',   N'Tiện ích',    N'Thẻ thành viên phòng Gym'),
(N'Thẻ Hồ bơi tháng',    250000,  N'Người/Tháng',   N'Tiện ích',    N'Thẻ thành viên hồ bơi'),
(N'Thẻ Gym + Hồ bơi',    450000,  N'Người/Tháng',   N'Tiện ích',    N'Thẻ thành viên combo Gym & Hồ bơi'),
(N'Đặt phòng BBQ',        200000,  N'Buổi',          N'Tiện ích',    N'Thuê khu BBQ sân thượng (3 tiếng)'),
(N'Đặt phòng họp',        100000,  N'Giờ',           N'Tiện ích',    N'Thuê phòng họp cộng đồng'),
(N'Giữ trẻ theo giờ',     80000,   N'Giờ',           N'Chăm sóc',    N'Dịch vụ giữ trẻ tại nhà'),
(N'Vận chuyển nội khu',   100000,  N'Lần',           N'Vận chuyển',  N'Hỗ trợ vận chuyển đồ trong tòa nhà'),
(N'Lắp đặt thiết bị',     150000,  N'Lần',           N'Lắp đặt',     N'Hỗ trợ lắp đặt thiết bị điện tử');
-- Tổng: 20 dịch vụ | IDs: 1-20

-- ============================================================
-- 5. TAI_SAN (20 bản ghi)
-- Lưu ý: @PrePersist tự tính ngay_bao_tri_tiep_theo = ngay_mua + chu_ky_bao_tri tháng
-- Trong SQL ta nhập thủ công giá trị đã tính sẵn
-- ============================================================
INSERT INTO tai_san (ma_tai_san, ten_tai_san, vi_tri, tinh_trang, ngay_mua, chu_ky_bao_tri, ngay_bao_tri_tiep_theo) VALUES
('TS-TM-A01', N'Thang máy Block A - Số 1',        N'Hành lang Block A, B1-15F',   N'Hoạt động tốt',  '2022-01-15',  6,  '2022-07-15'),
('TS-TM-A02', N'Thang máy Block A - Số 2',        N'Hành lang Block A, B1-15F',   N'Hoạt động tốt',  '2022-01-15',  6,  '2022-07-15'),
('TS-TM-B01', N'Thang máy Block B - Số 1',        N'Hành lang Block B, B1-12F',   N'Hoạt động tốt',  '2022-03-01',  6,  '2022-09-01'),
('TS-TM-C01', N'Thang máy Block C - Số 1',        N'Hành lang Block C, B1-10F',   N'Đang hỏng',      '2021-06-01',  6,  '2022-12-01'),
('TS-MF-01',  N'Máy phát điện dự phòng 1',        N'Tầng hầm B1, phòng kỹ thuật',N'Hoạt động tốt',  '2022-01-01',  12, '2023-01-01'),
('TS-MF-02',  N'Máy phát điện dự phòng 2',        N'Tầng hầm B2, phòng kỹ thuật',N'Hoạt động tốt',  '2022-06-01',  12, '2023-06-01'),
('TS-BP-01',  N'Bơm nước tăng áp tầng cao',       N'Phòng kỹ thuật tầng 10',     N'Hoạt động tốt',  '2023-02-15',  12, '2024-02-15'),
('TS-BP-02',  N'Bơm nước sinh hoạt B1',           N'Tầng hầm B1',                N'Hoạt động tốt',  '2022-08-01',  6,  '2023-02-01'),
('TS-CC-01',  N'Hệ thống camera an ninh Block A', N'Toàn bộ Block A',            N'Hoạt động tốt',  '2022-05-10',  12, '2023-05-10'),
('TS-CC-02',  N'Hệ thống camera an ninh Block B', N'Toàn bộ Block B',            N'Đang hỏng',      '2022-05-10',  12, '2023-05-10'),
('TS-CC-03',  N'Hệ thống camera hầm xe B1',       N'Tầng hầm B1',                N'Hoạt động tốt',  '2023-01-01',  12, '2024-01-01'),
('TS-PCCC-01',N'Hệ thống PCCC Block A',           N'Toàn bộ tòa nhà Block A',    N'Hoạt động tốt',  '2022-01-01',  3,  '2022-04-01'),
('TS-PCCC-02',N'Hệ thống PCCC Block B',           N'Toàn bộ tòa nhà Block B',    N'Hoạt động tốt',  '2022-01-01',  3,  '2022-04-01'),
('TS-DH-01',  N'Điều hòa trung tâm sảnh',         N'Sảnh tầng trệt Block A',     N'Hoạt động tốt',  '2022-06-01',  6,  '2022-12-01'),
('TS-HB-01',  N'Hệ thống lọc nước hồ bơi',        N'Khu vực hồ bơi tầng mái',   N'Hoạt động tốt',  '2023-03-01',  3,  '2023-06-01'),
('TS-BL-01',  N'Bãi đỗ xe tầng B1 - Cổng tự động',N'Tầng hầm B1 - Cổng vào',   N'Hoạt động tốt',  '2022-09-01',  6,  '2023-03-01'),
('TS-BL-02',  N'Bãi đỗ xe tầng B2 - Cổng tự động',N'Tầng hầm B2 - Cổng vào',   N'Chờ thanh lý',   '2020-01-01',  6,  '2020-07-01'),
('TS-SC-01',  N'Sân chơi trẻ em khu A',           N'Tầng trệt Block A - Ngoài trời',N'Hoạt động tốt','2023-05-01', 12, '2024-05-01'),
('TS-TN-01',  N'Trạm sạc xe điện khu A',          N'Tầng hầm B1 - Khu A',        N'Hoạt động tốt', '2024-01-15',  6,  '2024-07-15'),
('TS-MB-01',  N'Máy bơm PCCC áp lực cao',         N'Phòng kỹ thuật B1',          N'Hoạt động tốt', '2022-01-01',  12, '2023-01-01');
-- Tổng: 20 tài sản | IDs: 1-20

-- ============================================================
-- 6. KHAO_SAT (5 bản ghi - khảo sát do admin/quản lý tạo)
-- ============================================================
INSERT INTO khao_sat (tieu_de, mo_ta, thoi_gian_bat_dau, thoi_gian_ket_thuc) VALUES
(N'Khảo sát chất lượng dịch vụ bảo vệ',        N'Chúng tôi muốn biết ý kiến của bạn về đội ngũ bảo vệ tòa nhà trong 6 tháng qua.', '2024-04-01 08:00:00', '2024-05-01 17:00:00'),
(N'Lấy ý kiến tổ chức Trung Thu cho bé',        N'Góp ý về thời gian và địa điểm tổ chức sự kiện Trung Thu 2024.', '2024-08-01 08:00:00', '2024-08-15 17:00:00'),
(N'Khảo sát nhu cầu lắp thêm trạm sạc xe điện', N'Thu thập ý kiến về việc lắp đặt thêm trạm sạc xe điện tại hầm B2.', '2024-05-10 08:00:00', '2024-06-10 17:00:00'),
(N'Đánh giá chất lượng hồ bơi chung cư',        N'Khảo sát mức độ hài lòng của cư dân về hồ bơi và khu tiện ích.', '2024-06-01 08:00:00', '2024-06-30 17:00:00'),
(N'Lấy ý kiến về giờ giới nghiêm âm thanh',     N'Thăm dò ý kiến cư dân về quy định giờ yên lặng trong khu chung cư.', '2024-07-01 08:00:00', '2024-07-20 17:00:00');
-- Tổng: 5 khảo sát | IDs: 1-5

-- ============================================================
-- 7. LUA_CHON_KHAO_SAT (20 bản ghi)
-- ============================================================
INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
-- Khảo sát 1: Chất lượng bảo vệ (4 lựa chọn)
(1, N'Rất hài lòng',       15),
(1, N'Hài lòng',           32),
(1, N'Bình thường',        10),
(1, N'Không hài lòng',      5),
-- Khảo sát 2: Tổ chức Trung Thu (3 lựa chọn)
(2, N'Ngày 14/09/2024',    22),
(2, N'Ngày 15/09/2024',    28),
(2, N'Cả hai ngày đều tốt', 8),
-- Khảo sát 3: Trạm sạc xe điện (3 lựa chọn)
(3, N'Đồng ý, cần lắp gấp', 45),
(3, N'Đồng ý nhưng không gấp', 18),
(3, N'Không cần thiết',      5),
-- Khảo sát 4: Hồ bơi (4 lựa chọn)
(4, N'Rất hài lòng',       12),
(4, N'Hài lòng',           25),
(4, N'Cần cải thiện',      14),
(4, N'Kém chất lượng',      4),
-- Khảo sát 5: Giờ giới nghiêm (3 lựa chọn)
(5, N'22h - 6h sáng',      30),
(5, N'23h - 7h sáng',      18),
(5, N'Giữ nguyên quy định hiện tại', 12);
-- Không thêm: 5 lựa chọn cuối đủ 20 bản ghi
-- Tổng: 16 lựa chọn. Thêm 4 nữa cho đủ 20:
INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
(1, N'Chưa tiếp xúc bảo vệ lần nào',  3),
(2, N'Không muốn tổ chức',             2),
(4, N'Chưa sử dụng hồ bơi',           9),
(5, N'Không quan tâm',                 7);
-- Tổng: 20 lựa chọn | IDs: 1-20

-- ============================================================
-- 8. THONG_BAO (20 bản ghi)
-- loai: 1=Bảng tin chung, 2=Cẩm nang
-- doi_tuong_gui: ALL, HO_GIA_DINH, NHIEU_HO, TANG
-- ============================================================
INSERT INTO thong_bao (tieu_de, noi_dung, loai, ngay_dang, doi_tuong_gui, gia_tri_doi_tuong) VALUES
(N'Thông báo cắt nước định kỳ tháng 5',     N'Tòa nhà sẽ cắt nước để bảo trì bồn chứa vào ngày 10/05/2024 từ 8h00 đến 11h00. Đề nghị cư dân tích trữ nước trước.', 1, '2024-05-01 09:00:00', N'ALL', 'ALL'),
(N'Nhắc nhở đóng phí quản lý tháng 5',     N'Vui lòng thanh toán phí quản lý trước ngày 05/05/2024. Quá hạn sẽ bị tính phí trễ hạn.', 2, '2024-04-25 14:00:00', N'ALL', 'ALL'),
(N'Thông báo phun thuốc muỗi',              N'Ban quản lý sẽ phun thuốc muỗi khu vực hành lang và khuôn viên vào tối thứ 7, ngày 18/05/2024 lúc 20h00.', 1, '2024-05-12 10:00:00', N'ALL', 'ALL'),
(N'Thông báo kiểm tra PCCC định kỳ',       N'Đoàn kiểm tra PCCC sẽ đến kiểm tra tất cả các căn hộ vào ngày 20/05/2024. Đề nghị cư dân tạo điều kiện và có mặt tại nhà.', 1, '2024-05-14 08:00:00', N'ALL', 'ALL'),
(N'Thông báo lịch cúp điện bảo trì',       N'Điện khu vực Block A sẽ bị cắt từ 13h00 đến 15h00 ngày 22/05/2024 để bảo trì tủ điện tổng.', 1, '2024-05-18 10:00:00', N'HO_GIA_DINH', 'A'),
(N'Sự kiện Ngày hội cư dân 2024',           N'Ban quản lý tổ chức Ngày hội cư dân vào Chủ nhật 26/05/2024. Nhiều hoạt động thú vị và quà tặng hấp dẫn!', 1, '2024-05-20 09:00:00', N'ALL', 'ALL'),
(N'Cẩm nang: Quy định sử dụng thang máy',  N'Không chở hàng hóa cồng kềnh giờ cao điểm (7h-9h, 17h-19h). Không hút thuốc trong thang máy. Ưu tiên người già và trẻ em.', 2, '2024-04-01 08:00:00', N'ALL', 'ALL'),
(N'Cẩm nang: Phân loại rác đúng cách',     N'Rác hữu cơ để túi xanh, rác vô cơ túi trắng. Giờ đổ rác: 6h-8h và 18h-20h. Vui lòng không để rác ngoài hành lang.', 2, '2024-04-05 08:00:00', N'ALL', 'ALL'),
(N'Cẩm nang: Quy định sử dụng hồ bơi',    N'Giờ hoạt động: 6h00-21h00. Bắt buộc mặc đồ bơi chuyên dụng. Trẻ em dưới 12 tuổi phải có người lớn đi kèm.', 2, '2024-04-10 08:00:00', N'ALL', 'ALL'),
(N'Cẩm nang: Quy định gửi xe tại hầm',    N'Xe máy gửi hầm B1. Ô tô gửi hầm B2. Xe đạp gửi khu vực riêng. Vui lòng không để xe trên hành lang và lối đi.', 2, '2024-04-15 08:00:00', N'ALL', 'ALL'),
(N'Thông báo nâng cấp thang máy Block A',  N'Thang máy Block A sẽ bảo trì nâng cấp từ 06-08/06/2024. Trong thời gian này xin sử dụng cầu thang bộ hoặc thang máy Block B.', 1, '2024-05-28 09:00:00', N'HO_GIA_DINH', 'A'),
(N'Thông báo họp cư dân quý II/2024',      N'Ban quản lý mời toàn thể cư dân tham dự buổi họp cư dân vào 19h00 ngày 15/06/2024 tại Hội trường tầng 1. Nội dung: tổng kết và kế hoạch quý III.', 1, '2024-06-01 10:00:00', N'ALL', 'ALL'),
(N'Thông báo mất nước đột xuất',           N'Do sự cố đường ống, Block B hiện đang mất nước. Đội kỹ thuật đang khắc phục. Dự kiến có nước trở lại sau 14h00.', 1, '2024-05-25 10:30:00', N'HO_GIA_DINH', 'B'),
(N'Nhắc nhở: Không nuôi thú cưng lớn',    N'Ban quản lý nhắc nhở một số cư dân không tuân thủ quy định nuôi thú cưng. Chỉ được nuôi thú cưng nhỏ (dưới 10kg) và phải đăng ký với ban quản lý.', 2, '2024-05-22 08:00:00', N'ALL', 'ALL'),
(N'Thông báo tăng cường an ninh đêm',     N'Từ 01/06/2024, ban quản lý tăng cường đội bảo vệ đêm. Khách thăm sau 22h phải đăng ký trước với ban quản lý.', 1, '2024-05-30 14:00:00', N'ALL', 'ALL'),
(N'Cẩm nang: Cách xử lý khi có cháy nổ', N'1. Giữ bình tĩnh, nhấn chuông báo cháy. 2. Gọi 114. 3. Thoát theo lối thoát hiểm, không dùng thang máy. 4. Tập trung tại điểm tập kết.', 2, '2024-03-15 08:00:00', N'ALL', 'ALL'),
(N'Thông báo cắt nước tháng 6',            N'Ngày 08/06/2024 từ 8h-12h sẽ tiến hành vệ sinh bồn chứa nước định kỳ. Đề nghị cư dân dự trữ nước từ tối hôm trước.', 1, '2024-06-03 09:00:00', N'ALL', 'ALL'),
(N'Thông báo lắp hệ thống sạc xe điện',   N'Sau kết quả khảo sát, Ban quản lý sẽ lắp 10 cột sạc xe điện tại hầm B1 vào tháng 8/2024. Chi phí sạc: 4.000đ/kWh.', 1, '2024-06-15 10:00:00', N'ALL', 'ALL'),
(N'Thông báo sự kiện Tết Trung Thu 2024',  N'Ban quản lý tổ chức đêm hội Trung Thu vào tối 14/09/2024. Chương trình: múa lân, lồng đèn, bánh Trung Thu cho các bé. Đăng ký tham gia trước 10/09.', 1, '2024-08-20 09:00:00', N'ALL', 'ALL'),
(N'Cẩm nang: Tiết kiệm điện nước',        N'Tắt đèn, điều hòa khi không có người. Không xả nước quá lâu. Báo cáo ngay các sự cố rò rỉ nước/điện đến ban quản lý qua hotline.', 2, '2024-06-20 08:00:00', N'ALL', 'ALL');
-- Tổng: 20 thông báo | IDs: 1-20

-- ============================================================
-- 9. CHI_SO_HANG_THANG (20 bản ghi - chỉ số lũy kế cuối tháng)
-- can_ho_id: chỉ nhập các căn có cư dân (1-13)
-- ============================================================
INSERT INTO chi_so_hang_thang (can_ho_id, dien_tieu_thu, nuoc_tieu_thu, ngay_ghi_nhan) VALUES
-- Tháng 3/2024
(1,  150.5,  12.0, '2024-03-31'),
(2,   80.2,   5.0, '2024-03-31'),
(3,  200.0,  16.0, '2024-03-31'),
(4,  170.0,  13.5, '2024-03-31'),
(5,   95.0,   7.0, '2024-03-31'),
(6,  220.0,  18.0, '2024-03-31'),
(7,  160.0,  12.5, '2024-03-31'),
(8,  130.0,  10.0, '2024-03-31'),
(9,  250.0,  20.0, '2024-03-31'),
(10, 180.0,  14.0, '2024-03-31'),
-- Tháng 4/2024
(1,  295.0,  23.5, '2024-04-30'),
(2,  165.5,  11.2, '2024-04-30'),
(3,  388.0,  31.5, '2024-04-30'),
(4,  335.0,  26.0, '2024-04-30'),
(5,  190.0,  14.5, '2024-04-30'),
(6,  430.0,  35.0, '2024-04-30'),
(7,  315.0,  24.5, '2024-04-30'),
(8,  255.0,  20.0, '2024-04-30'),
(9,  490.0,  39.0, '2024-04-30'),
(10, 355.0,  28.5, '2024-04-30');
-- Tổng: 20 bản ghi | IDs: 1-20

-- ============================================================
-- 10. HOP_DONG (lưu ý: bảng này CÓ THỂ thêm dữ liệu mẫu vì
--     hợp đồng được tạo thủ công khi nhận bàn giao căn hộ)
--     cu_dan_id phải tồn tại trong bảng cu_dan (IDs: 8-20)
-- ============================================================
INSERT INTO hop_dong (can_ho_id, cu_dan_id, loai_hop_dong, gia_tri_hop_dong, tien_coc, tien_thue, ngay_bat_dau, ngay_ket_thuc, ben_cho_thue, ben_thue, trang_thai) VALUES
(1,  8,  N'Mua bán', 2500000000, 500000000, 0,        '2022-06-01', '2072-06-01', N'Chủ đầu tư Sunrise',  N'Lê Văn Thắng',       N'Hiệu lực'),
(2,  10, N'Thuê',    8000000,    16000000,  8000000,  '2024-01-01', '2025-01-01', N'Ban quản lý chung cư',N'Phạm Minh Hoàng',   N'Hiệu lực'),
(3,  11, N'Mua bán', 3200000000, 1000000000,0,        '2022-08-15', '2072-08-15', N'Chủ đầu tư Sunrise',  N'Trần Bích Thủy',     N'Hiệu lực'),
(4,  12, N'Mua bán', 2800000000, 600000000, 0,        '2023-01-10', '2073-01-10', N'Chủ đầu tư Sunrise',  N'Đặng Văn Hùng',     N'Hiệu lực'),
(5,  13, N'Thuê',    9000000,    18000000,  9000000,  '2023-06-01', '2024-06-01', N'Ban quản lý chung cư',N'Hoàng Lan Anh',     N'Hiệu lực'),
(6,  14, N'Mua bán', 3500000000, 700000000, 0,        '2022-10-01', '2072-10-01', N'Chủ đầu tư Sunrise',  N'Vũ Quốc Anh',       N'Hiệu lực'),
(7,  15, N'Mua bán', 2600000000, 500000000, 0,        '2023-03-15', '2073-03-15', N'Chủ đầu tư Sunrise',  N'Ngô Thanh Vân',     N'Hiệu lực'),
(8,  16, N'Thuê',    7500000,    15000000,  7500000,  '2024-02-01', '2025-02-01', N'Ban quản lý chung cư',N'Đỗ Minh Tuấn',      N'Hiệu lực'),
(9,  17, N'Thuê',    8500000,    17000000,  8500000,  '2023-09-01', '2024-09-01', N'Ban quản lý chung cư',N'Bùi Thu Hà',        N'Hiệu lực'),
(10, 18, N'Mua bán', 4200000000, 800000000, 0,        '2021-05-01', '2071-05-01', N'Chủ đầu tư Sunrise',  N'Cao Ngọc Phương',   N'Hiệu lực'),
(11, 19, N'Mua bán', 2900000000, 550000000, 0,        '2022-12-01', '2072-12-01', N'Chủ đầu tư Sunrise',  N'Lý Kim Phi',         N'Hiệu lực'),
(12, 20, N'Thuê',    12000000,   24000000,  12000000, '2024-03-01', '2025-03-01', N'Ban quản lý chung cư',N'Trịnh Văn Sơn',     N'Hiệu lực'),
-- Hợp đồng đã hết hiệu lực (lịch sử)
(5,  13, N'Thuê',    8000000,    16000000,  8000000,  '2023-01-01', '2023-06-01', N'Ban quản lý chung cư',N'Hoàng Lan Anh',     N'Hết hiệu lực');
-- Tổng: 13 hợp đồng | IDs: 1-13

-- ============================================================
-- 11. PHUONG_TIEN (20 bản ghi)
-- can_ho_id phải tồn tại (1-20), chỉ nhập cho căn có cư dân
-- ============================================================
INSERT INTO phuong_tien (can_ho_id, bien_so, loai, mau_sac, trang_thai) VALUES
(1,  '29A-123.45', N'Xe máy',  N'Đỏ',     N'Đã đăng ký'),
(1,  '30H-999.88', N'Ô tô',    N'Trắng',  N'Đã đăng ký'),
(1,  '29C-111.22', N'Xe máy',  N'Đen',    N'Đã đăng ký'),
(2,  '29C-567.89', N'Xe máy',  N'Xanh',   N'Đã đăng ký'),
(3,  '51G-444.22', N'Ô tô',    N'Đen',    N'Đã đăng ký'),
(3,  '29P-222.11', N'Xe máy',  N'Bạc',    N'Đã đăng ký'),
(4,  '30K-555.66', N'Xe máy',  N'Trắng',  N'Đã đăng ký'),
(4,  '29T-777.33', N'Ô tô',    N'Xám',    N'Đã đăng ký'),
(5,  '29D-888.99', N'Xe máy',  N'Xám',    N'Đã đăng ký'),
(6,  '51B-100.10', N'Xe đạp',  N'Xanh lá',N'Đã đăng ký'),
(6,  '29A-200.20', N'Xe máy',  N'Vàng',   N'Đã đăng ký'),
(7,  '30G-300.30', N'Xe máy',  N'Đỏ',     N'Đã đăng ký'),
(8,  '29E-400.40', N'Ô tô',    N'Xanh navy',N'Đã đăng ký'),
(8,  '29K-500.50', N'Xe máy',  N'Trắng',  N'Đã đăng ký'),
(9,  '51H-600.60', N'Xe máy',  N'Đen',    N'Đã đăng ký'),
(10, '29M-700.70', N'Ô tô',    N'Bạc',    N'Đã đăng ký'),
(11, '30A-800.80', N'Xe máy',  N'Đỏ',     N'Đã đăng ký'),
(11, '29N-900.90', N'Xe đạp',  N'Vàng',   N'Đã đăng ký'),
(12, '51K-101.01', N'Ô tô',    N'Trắng',  N'Đã đăng ký'),
(12, '29X-202.02', N'Xe máy',  N'Xám',    N'Đã đăng ký');
-- Tổng: 20 phương tiện | IDs: 1-20

-- ============================================================
-- 12. KIEN_HANG (20 bản ghi)
-- can_ho_id phải tồn tại (dùng căn có cư dân: 1-12)
-- ============================================================
INSERT INTO kien_hang (can_ho_id, nguoi_gui, nguoi_nhan, ngay_nhan, trang_thai) VALUES
(1,  N'Shopee',          N'Lê Văn Thắng',      '2024-04-28', N'Đã nhận'),
(1,  N'Tiki',            N'Nguyễn Thị Mai',    '2024-05-02', N'Đã nhận'),
(2,  N'Lazada',          N'Phạm Minh Hoàng',   '2024-05-05', N'Đã nhận'),
(3,  N'Grab Express',    N'Trần Bích Thủy',    '2024-05-08', N'Đã nhận'),
(4,  N'GHTK',            N'Đặng Văn Hùng',     '2024-05-10', N'Đã nhận'),
(5,  N'J&T Express',     N'Hoàng Lan Anh',     '2024-05-12', N'Chờ nhận'),
(6,  N'Viettel Post',    N'Vũ Quốc Anh',       '2024-05-13', N'Chờ nhận'),
(7,  N'Shopee',          N'Ngô Thanh Vân',     '2024-05-14', N'Chờ nhận'),
(8,  N'Tiki',            N'Đỗ Minh Tuấn',      '2024-05-14', N'Chờ nhận'),
(9,  N'DHL',             N'Bùi Thu Hà',        '2024-05-15', N'Chờ nhận'),
(10, N'FedEx',           N'Cao Ngọc Phương',   '2024-05-15', N'Chờ nhận'),
(11, N'Lazada',          N'Lý Kim Phi',         '2024-05-16', N'Chờ nhận'),
(12, N'Shopee',          N'Trịnh Văn Sơn',     '2024-05-16', N'Chờ nhận'),
(1,  N'Amazon',          N'Lê Văn Thắng',      '2024-05-17', N'Chờ nhận'),
(2,  N'GHTK',            N'Phạm Minh Hoàng',   '2024-05-17', N'Chờ nhận'),
(3,  N'Ninja Van',       N'Trần Bích Thủy',    '2024-04-20', N'Đã nhận'),
(4,  N'SPX',             N'Đặng Văn Hùng',     '2024-04-25', N'Đã nhận'),
(5,  N'Grab Express',    N'Hoàng Lan Anh',     '2024-04-18', N'Đã nhận'),
(6,  N'J&T Express',     N'Vũ Quốc Anh',       '2024-04-15', N'Đã nhận'),
(7,  N'Vietnam Post',    N'Ngô Thanh Vân',     '2024-04-10', N'Đã nhận');
-- Tổng: 20 kiện hàng | IDs: 1-20

-- ============================================================
-- 13. PHAN_ANH (20 bản ghi)
-- can_ho_id phải tồn tại (dùng căn có cư dân: 1-12)
-- trang_thai: "Chờ xử lý", "Đang xử lý", "Đã xử lý"
-- ============================================================
INSERT INTO phan_anh (can_ho_id, tieu_de, noi_dung, ngay_gui, phan_hoi, ngay_phan_hoi, trang_thai, hinh_anh) VALUES
(1,  N'Hành lang tầng 1 bẩn',         N'Hành lang tầng 1 có rác chưa được dọn từ sáng sớm, gây mùi khó chịu.',                 '2024-04-28 09:00:00', N'Đã điều nhân viên vệ sinh kiểm tra và dọn dẹp lúc 10h30.',   '2024-04-28 10:30:00', N'Đã xử lý',  'pa_001.jpg'),
(3,  N'Tiếng ồn ban đêm từ tầng trên',N'Căn hộ tầng trên thường xuyên gây tiếng ồn sau 23h, ảnh hưởng giấc ngủ.',              '2024-04-20 23:30:00', N'Đã nhắc nhở chủ hộ tầng trên tuân thủ nội quy giờ yên lặng.','2024-04-21 09:00:00', N'Đã xử lý',  NULL),
(7,  N'Đèn hành lang bị nhấp nháy',   N'Đèn LED trước cửa căn hộ A1-03 nhấp nháy liên tục từ tuần trước.',                     '2024-05-01 19:00:00', NULL, NULL, N'Đang xử lý', 'pa_003.jpg'),
(2,  N'Thang máy hay bị dừng giữa chừng', N'Thang máy Block A số 1 thường xuyên dừng và rung nhẹ giữa các tầng.',              '2024-05-03 14:00:00', N'Đã báo đơn vị bảo trì thang máy, dự kiến kiểm tra ngày 06/05.','2024-05-03 15:30:00', N'Đang xử lý','pa_004.jpg'),
(5,  N'Bồn hoa lối vào xuống cấp',    N'Bồn hoa trang trí lối vào Block A bị vỡ và cây chết, trông mất thẩm mỹ.',             '2024-05-05 10:00:00', N'Ghi nhận, đã lên kế hoạch cải tạo cảnh quan vào tháng 6.',    '2024-05-06 08:00:00', N'Đã xử lý',  NULL),
(4,  N'Bãi xe đầy, không có chỗ gửi', N'Tầng hầm B1 thường xuyên hết chỗ từ 18h-20h, xe phải gửi bên ngoài tốn thêm chi phí.','2024-05-07 20:00:00', N'Đang nghiên cứu thêm khu vực gửi xe phụ tại tầng hầm B2.',   '2024-05-08 09:00:00', N'Đang xử lý','pa_006.jpg'),
(8,  N'Nước yếu vào giờ cao điểm',    N'Áp lực nước rất yếu vào buổi sáng từ 6h-8h, không đủ dùng sinh hoạt.',                '2024-05-09 07:30:00', NULL, NULL, N'Chờ xử lý',  NULL),
(9,  N'Cửa kính sảnh bị hỏng khóa',  N'Cửa kính sảnh tầng trệt Block B không đóng được chặt, gây mất an ninh.',              '2024-05-10 11:00:00', N'Đã sửa chữa khóa cửa ngay trong ngày.',                        '2024-05-10 15:00:00', N'Đã xử lý',  'pa_008.jpg'),
(6,  N'Hồ bơi nhiều rong rêu',        N'Hồ bơi có váng màu xanh và rong rêu ở góc bể, e ngại vệ sinh.',                      '2024-05-11 16:00:00', N'Đã xử lý nước và vệ sinh hồ bơi, hoạt động bình thường từ 13/5.','2024-05-12 08:00:00', N'Đã xử lý', 'pa_009.jpg'),
(10, N'Wifi tòa nhà chập chờn',        N'Mạng wifi sảnh và hành lang thường xuyên bị mất tín hiệu vào buổi tối.',               '2024-05-12 21:00:00', NULL, NULL, N'Đang xử lý', NULL),
(11, N'Xe đạp bị mất trong bãi xe',   N'Xe đạp điện để ở bãi xe B1 bị mất, không thấy camera ghi lại.',                       '2024-05-13 08:00:00', N'Đã xem lại camera, đang phối hợp với cơ quan công an điều tra.','2024-05-13 10:00:00', N'Đang xử lý','pa_011.jpg'),
(12, N'Mùi hôi từ cống tầng hầm',     N'Tầng hầm B2 có mùi hôi nặng từ hệ thống cống thoát nước.',                           '2024-05-14 09:00:00', N'Đã thông cống và khử trùng hệ thống thoát nước hầm B2.',       '2024-05-15 14:00:00', N'Đã xử lý',  NULL),
(1,  N'Trẻ em chơi gây ồn ào',        N'Nhóm trẻ em chơi đùa trong hành lang và ném đồ vật, nguy hiểm và ồn ào.',             '2024-05-14 15:00:00', N'Đã thông báo đến phụ huynh và nhắc nhở con em tuân thủ nội quy.','2024-05-15 08:00:00', N'Đã xử lý', NULL),
(2,  N'Phòng tập Gym máy hỏng',       N'Máy chạy bộ số 2 và ghế tạ số 1 trong phòng gym bị hỏng từ 2 tuần nay.',             '2024-05-15 10:00:00', N'Đã liên hệ đơn vị bảo trì, dự kiến sửa xong trước 20/05.',     '2024-05-15 14:00:00', N'Đang xử lý', 'pa_014.jpg'),
(3,  N'Chó nuôi không rọ mõm',        N'Một số cư dân dắt chó lớn đi trong tòa nhà không đeo rọ mõm, gây lo ngại cho trẻ em.','2024-05-16 11:00:00', N'Đã nhắc nhở và gửi thông báo về quy định nuôi thú cưng.',      '2024-05-16 14:00:00', N'Đã xử lý',  NULL),
(4,  N'Thùng rác đầy, chưa được dọn', N'Thùng rác khu vực tầng 4 đã đầy từ hôm qua nhưng chưa được dọn.',                    '2024-05-17 08:00:00', NULL, NULL, N'Chờ xử lý',  NULL),
(5,  N'Camera hành lang tầng 5 hỏng', N'Camera an ninh tại hành lang tầng 5 Block A không hoạt động, hình ảnh bị nhiễu.',     '2024-05-17 13:00:00', N'Đã cử kỹ thuật viên kiểm tra, camera bị đứt dây cần thay mới.',  '2024-05-17 16:00:00', N'Đang xử lý','pa_017.jpg'),
(6,  N'Cống thoát nước sân thượng',   N'Cống thoát nước sân thượng tầng mái bị tắc, nước đọng lại ứ đọng.',                   '2024-04-30 10:00:00', N'Đã thông cống và kiểm tra toàn bộ hệ thống thoát nước mái.',   '2024-05-01 09:00:00', N'Đã xử lý',  NULL),
(7,  N'Vết nứt tường hành lang',       N'Tường hành lang tầng 3 Block A xuất hiện vết nứt dài khoảng 30cm.',                  '2024-05-18 09:00:00', NULL, NULL, N'Chờ xử lý',  'pa_019.jpg'),
(8,  N'Ánh đèn hành lang quá tối',    N'Hành lang tầng 8 Block B thiếu sáng vào ban đêm, đi lại không an toàn.',             '2024-05-18 20:00:00', N'Đã thay bóng đèn và kiểm tra toàn bộ hệ thống chiếu sáng.',   '2024-05-19 08:00:00', N'Đã xử lý',  NULL);
-- Tổng: 20 phản ánh | IDs: 1-20

-- ============================================================
-- 14. DANG_KY_KHACH_THAM (20 bản ghi)
-- cu_dan_id phải là ID trong bảng cu_dan (8-20)
-- trang_thai: "Đang chờ", "Đã duyệt", "Không duyệt"
-- ============================================================
INSERT INTO dang_ky_khach_tham (cu_dan_id, ten_khach, cmnd, bien_so_xe, thoi_gian_du_kien, thoi_gian_duyet, trang_thai) VALUES
(8,  N'Nguyễn Văn Bình',   '001090112233', '29F1-000.01', '2024-05-02 19:00:00', '2024-05-01 15:00:00', N'Đã duyệt'),
(10, N'Trần Thị Cúc',      '001095223344', NULL,          '2024-05-05 10:00:00', '2024-05-04 14:00:00', N'Đã duyệt'),
(11, N'Lê Văn Dũng',       '001088334455', '51A-123.00',  '2024-05-06 14:00:00', '2024-05-06 09:00:00', N'Đã duyệt'),
(12, N'Phạm Thị Én',       '001092445566', NULL,          '2024-05-08 09:00:00', NULL,                  N'Đang chờ'),
(13, N'Đặng Văn Giang',    '001091556677', '29H-456.00',  '2024-05-10 15:00:00', '2024-05-10 08:00:00', N'Đã duyệt'),
(14, N'Hoàng Thị Hoa',     '001097667788', NULL,          '2024-05-12 10:00:00', '2024-05-11 16:00:00', N'Đã duyệt'),
(15, N'Vũ Văn Kiên',       '001086778899', '30D-789.00',  '2024-05-15 18:00:00', NULL,                  N'Đang chờ'),
(16, N'Ngô Thị Linh',      '001093889900', NULL,          '2024-05-16 11:00:00', '2024-05-16 09:00:00', N'Đã duyệt'),
(17, N'Đỗ Văn Minh',       '001085990011', '29A-012.00',  '2024-05-18 09:00:00', '2024-05-17 15:00:00', N'Đã duyệt'),
(18, N'Bùi Thị Nhung',     '001096001122', NULL,          '2024-05-19 20:00:00', NULL,                  N'Đang chờ'),
(19, N'Cao Văn Phong',     '001084112233', '51C-234.00',  '2024-05-20 14:00:00', '2024-05-20 08:00:00', N'Đã duyệt'),
(20, N'Lý Thị Quỳnh',      '001097223344', NULL,          '2024-05-21 10:00:00', NULL,                  N'Đang chờ'),
(8,  N'Trịnh Văn Rồng',    '001090334455', '29M-345.00',  '2024-05-22 16:00:00', '2024-05-22 09:00:00', N'Đã duyệt'),
(9,  N'Lê Thị Sương',      '001088445566', NULL,          '2024-05-03 09:00:00', '2024-05-02 16:00:00', N'Đã duyệt'),
(10, N'Phạm Văn Thành',    '001095556677', '30K-567.00',  '2024-05-07 14:00:00', '2024-05-07 10:00:00', N'Đã duyệt'),
(11, N'Đặng Thị Uyên',     '001088667788', NULL,          '2024-04-28 10:00:00', '2024-04-27 16:00:00', N'Đã duyệt'),
(12, N'Hoàng Văn Việt',    '001092778899', '29N-678.00',  '2024-04-30 15:00:00', '2024-04-30 09:00:00', N'Đã duyệt'),
(13, N'Vũ Thị Xuân',       '001091889900', NULL,          '2024-05-13 10:00:00', '2024-05-12 15:00:00', N'Đã duyệt'),
(14, N'Ngô Văn Ý',         '001097990011', '51G-789.00',  '2024-05-14 18:00:00', NULL,                  N'Không duyệt'),
(15, N'Đỗ Thị Zang',       '001086001122', NULL,          '2024-05-17 09:00:00', '2024-05-16 16:00:00', N'Đã duyệt');
-- Tổng: 20 đăng ký khách thăm | IDs: 1-20

-- ============================================================
-- 15. DAT_DICH_VU (20 bản ghi)
-- cu_dan_id: 8-20, dich_vu_id: 1-20
-- trang_thai: "Đang chờ", "Đã duyệt", "Đã hủy", "Đã sử dụng"
-- ============================================================
INSERT INTO dat_dich_vu (cu_dan_id, dich_vu_id, ngay_dat, thoi_gian_duyet, trang_thai, ghi_chu) VALUES
(8,   5, '2024-04-30', '2024-05-01 08:00:00', N'Đã sử dụng', N'Dọn toàn bộ nhà trước ngày lễ'),
(10,  7, '2024-05-03', '2024-05-04 09:00:00', N'Đã sử dụng', N'Máy lạnh phòng ngủ kêu to'),
(11, 13, '2024-05-05', '2024-05-05 10:00:00', N'Đã sử dụng', N'Đăng ký thẻ Gym tháng 5'),
(12, 14, '2024-05-05', '2024-05-05 10:00:00', N'Đã sử dụng', N'Đăng ký thẻ Hồ bơi tháng 5'),
(13,  4, '2024-05-06', '2024-05-07 08:00:00', N'Đã sử dụng', N'Vệ sinh nhà buổi sáng'),
(14, 16, '2024-05-08', '2024-05-08 14:00:00', N'Đã sử dụng', N'Đặt khu BBQ tối thứ 7'),
(15, 10, '2024-05-09', '2024-05-09 09:00:00', N'Đã sử dụng', N'Gói internet tháng 5'),
(16,  8, '2024-05-10', NULL,                  N'Đang chờ',   N'Sửa ống nước bị rỉ trong bếp'),
(17, 19, '2024-05-11', NULL,                  N'Đang chờ',   N'Chuyển tủ lạnh từ phòng kho ra phòng bếp'),
(18, 13, '2024-05-12', '2024-05-12 10:00:00', N'Đã duyệt',   N'Đăng ký thẻ Gym tháng 5'),
(19,  4, '2024-05-13', NULL,                  N'Đang chờ',   N'Dọn nhà buổi chiều'),
(20, 12, '2024-05-14', '2024-05-14 11:00:00', N'Đã duyệt',   N'Gói truyền hình cáp tháng 5'),
(8,  15, '2024-05-15', '2024-05-15 09:00:00', N'Đã duyệt',   N'Thẻ hồ bơi tháng 5'),
(9,   7, '2024-05-01', '2024-05-01 10:00:00', N'Đã sử dụng', N'Bảo trì 2 máy lạnh phòng ngủ và phòng khách'),
(10, 17, '2024-05-16', '2024-05-16 14:00:00', N'Đã duyệt',   N'Thuê phòng họp 2 tiếng tổ chức sinh nhật mini'),
(11, 20, '2024-05-17', NULL,                  N'Đã hủy',     N'Hủy vì không còn nhu cầu'),
(12,  5, '2024-04-28', '2024-04-29 09:00:00', N'Đã sử dụng', N'Dọn nhà cuối tuần'),
(13,  8, '2024-05-18', NULL,                  N'Đang chờ',   N'Bóng đèn phòng khách bị hỏng'),
(14, 18, '2024-05-19', '2024-05-19 10:00:00', N'Đã duyệt',   N'Gửi trẻ 3 tiếng buổi sáng'),
(15, 11, '2024-05-20', '2024-05-20 09:00:00', N'Đã duyệt',   N'Gói internet 200Mbps tháng 5');
-- Tổng: 20 đặt dịch vụ | IDs: 1-20

-- ============================================================
-- 16. LICH_SU_VOTE (20 bản ghi)
-- UniqueConstraint: mỗi cu_dan chỉ vote 1 lần trong 1 khao_sat
-- cu_dan_id: 8-20 | khao_sat_id: 1-5 | lua_chon_id: 1-20
-- ============================================================
INSERT INTO lich_su_vote (cu_dan_id, khao_sat_id, lua_chon_id, thoi_gian_vote) VALUES
-- Khảo sát 1 (lua_chon: 1=Rất hài lòng, 2=Hài lòng, 3=Bình thường, 4=Không hài lòng, 17=Chưa tiếp xúc)
(8,   1, 1,  '2024-04-10 09:00:00'),
(10,  1, 2,  '2024-04-11 10:00:00'),
(11,  1, 2,  '2024-04-12 11:00:00'),
(12,  1, 3,  '2024-04-13 09:30:00'),
(13,  1, 4,  '2024-04-14 14:00:00'),
-- Khảo sát 2 (lua_chon: 5=Ngày 14/09, 6=Ngày 15/09, 7=Cả hai đều tốt, 18=Không muốn tổ chức)
(8,   2, 6,  '2024-08-03 09:00:00'),
(10,  2, 5,  '2024-08-04 10:00:00'),
(11,  2, 7,  '2024-08-05 11:00:00'),
(14,  2, 6,  '2024-08-06 09:00:00'),
(15,  2, 5,  '2024-08-07 14:00:00'),
-- Khảo sát 3 (lua_chon: 8=Đồng ý gấp, 9=Đồng ý không gấp, 10=Không cần)
(8,   3, 8,  '2024-05-11 14:00:00'),
(11,  3, 8,  '2024-05-12 08:00:00'),
(13,  3, 9,  '2024-05-13 10:00:00'),
(16,  3, 8,  '2024-05-14 09:00:00'),
(17,  3, 10, '2024-05-15 11:00:00'),
-- Khảo sát 4 (lua_chon: 11=Rất hài lòng, 12=Hài lòng, 13=Cần cải thiện, 14=Kém, 19=Chưa dùng)
(9,   4, 12, '2024-06-05 09:00:00'),
(12,  4, 11, '2024-06-06 10:00:00'),
(14,  4, 13, '2024-06-07 11:00:00'),
-- Khảo sát 5 (lua_chon: 15=22h-6h, 16=23h-7h, 17=Giữ nguyên, 20=Không quan tâm)
(9,   5, 15, '2024-07-05 09:00:00'),
(10,  5, 16, '2024-07-06 10:00:00');
-- Tổng: 20 lượt vote | IDs: 1-20

-- ============================================================
-- 17. LICH_SU_BAO_TRI (20 bản ghi)
-- tai_san_id phải tồn tại trong tai_san (1-20)
-- ============================================================
INSERT INTO lich_su_bao_tri (tai_san_id, ngay_bao_tri, noi_dung, chi_phi, nguoi_thuc_hien) VALUES
(1,  '2023-01-15', N'Kiểm tra định kỳ 6 tháng, tra dầu mỡ cơ cấu, thử tải',                  2500000,  N'Công ty Thang máy Otis VN'),
(2,  '2023-01-15', N'Kiểm tra định kỳ 6 tháng, thay cáp dây an toàn',                          3200000,  N'Công ty Thang máy Otis VN'),
(3,  '2022-09-01', N'Bảo trì định kỳ lần đầu sau 6 tháng vận hành',                            2000000,  N'Công ty Thang máy Kone'),
(4,  '2022-12-01', N'Thay toàn bộ bo mạch điều khiển thang máy bị cháy',                       15000000, N'Công ty Thang máy Mitsubishi'),
(5,  '2023-01-01', N'Kiểm tra, thay lọc dầu và ắc quy máy phát điện dự phòng 1',               1800000,  N'Công ty Điện lực TNHH Hùng Phát'),
(6,  '2023-06-01', N'Bảo dưỡng định kỳ máy phát điện 2, thử tải 100%',                         2200000,  N'Công ty Điện lực TNHH Hùng Phát'),
(7,  '2024-02-15', N'Thay mới bơm nước tăng áp tầng cao, kiểm tra đường ống áp lực',           8500000,  N'Công ty CP Nước sạch Hà Nội'),
(8,  '2023-02-01', N'Vệ sinh màng lọc, kiểm tra motor bơm',                                     1200000,  N'Đội kỹ thuật nội bộ'),
(9,  '2023-05-10', N'Nâng cấp firmware hệ thống camera Block A, thay 3 camera hỏng',            5000000,  N'Công ty An ninh Việt Security'),
(10, '2023-05-10', N'Thay toàn bộ 8 camera hỏng Block B, nâng lên độ phân giải 4K',            12000000, N'Công ty An ninh Việt Security'),
(11, '2024-01-01', N'Kiểm tra và vệ sinh camera hầm B1, căn chỉnh góc quan sát',               800000,   N'Đội kỹ thuật nội bộ'),
(12, '2022-04-01', N'Kiểm tra định kỳ quý 1: bình chữa cháy, hệ thống sprinkler Block A',      3500000,  N'Công ty PCCC An Bình'),
(13, '2022-04-01', N'Kiểm tra định kỳ quý 1: bình chữa cháy, hệ thống sprinkler Block B',      3500000,  N'Công ty PCCC An Bình'),
(14, '2022-12-01', N'Vệ sinh lọc khí, bổ sung gas R410A điều hòa sảnh',                        2800000,  N'Công ty Daikin Service'),
(15, '2023-06-01', N'Thay hoá chất xử lý nước hồ bơi, kiểm tra hệ thống lọc tuần hoàn',       3200000,  N'Công ty Vệ sinh Hồ Bơi Sạch'),
(16, '2023-03-01', N'Bảo trì hệ thống motor, kiểm tra cảm biến cổng tự động B1',               2000000,  N'Công ty Tự động hóa Đinh Tiên'),
(17, '2020-07-01', N'Kiểm tra cảm biến cổng B2, kết luận cần thay thế toàn bộ hệ thống',      500000,   N'Công ty Tự động hóa Đinh Tiên'),
(18, '2024-05-01', N'Kiểm tra trang thiết bị sân chơi, thay ghế đu bị mòn',                    1500000,  N'Đội kỹ thuật nội bộ'),
(19, '2024-07-15', N'Kiểm tra định kỳ 6 tháng trạm sạc xe điện, cập nhật phần mềm',           1000000,  N'Công ty EV Charging Solutions'),
(20, '2023-01-01', N'Kiểm tra áp lực và vòi phun bơm PCCC áp lực cao, thay gioăng van',       2500000,  N'Công ty PCCC An Bình');
-- Tổng: 20 lịch sử bảo trì | IDs: 1-20

-- ============================================================
-- GHI CHÚ QUAN TRỌNG - Các bảng KHÔNG thêm dữ liệu mẫu:
-- ============================================================
-- 1. HOA_DON: Được hệ thống TỰ ĐỘNG TẠO hàng tháng dựa trên
--    chi_so_hang_thang + dat_dich_vu. Không thêm dữ liệu mẫu
--    vì cần logic nghiệp vụ tính tổng tiền chính xác.
--
-- 2. THANH_TOAN: Được tạo khi cư dân thanh toán hóa đơn.
--    Phụ thuộc vào hoa_don.id và có UniqueConstraint(hoa_don_id).
--
-- 3. LICH_SU_THANH_TOAN: Tự động ghi lại khi có thanh toán.
--    Phụ thuộc vào hoa_don.id và có UniqueConstraint(hoa_don_id).
--
-- => 3 bảng trên sẽ có dữ liệu khi hệ thống vận hành thực tế.
-- ============================================================
