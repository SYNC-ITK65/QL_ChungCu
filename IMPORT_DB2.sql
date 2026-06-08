
USE QL_ChungCu;

-- ===================================================================

INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau_ma_hoa, ho_ten, email, so_dien_thoai, vai_tro) VALUES
('levan_thang','1234', 'Lê Văn Thắng','levan.thang@gmail.com','0987654321', 3),
('nguyen_mai','1234', 'Nguyễn Thị Mai','nguyen.mai@gmail.com','0987654322', 3),
('pham_hoang','1234', 'Phạm Minh Hoàng','pham.hoang@gmail.com','0977112233', 3),
('tran_thuy','1234', 'Trần Bích Thủy','tran.thuy@gmail.com', '0966445566', 3),
('dang_hung','1234', 'Đặng Văn Hùng', 'dang.hung@gmail.com', '0955889900', 3),
('hoang_anh','1234', 'Hoàng Lan Anh', 'hoang.anh@gmail.com', '0944773322', 3),
('vu_quocanh','1234', 'Vũ Quốc Anh','vu.quocanh@gmail.com','0933221100', 3),
('ngo_thanvan', '1234', 'Ngô Thanh Vân','ngo.thanvan@gmail.com','0922334455', 3),
('do_minhtuan', '1234', 'Đỗ Minh Tuấn','do.minhtuan@gmail.com','0911223344', 3),
('bui_thuha','1234', 'Bùi Thu Hà','bui.thuha@gmail.com', '0900112233', 3),
('cao_phuong','1234', 'Cao Ngọc Phương','cao.phuong@gmail.com','0889012345', 3),
('ly_kimphi','1234', 'Lý Kim Phi','ly.kimphi@gmail.com', '0878123456', 3),
('trinh_son','1234', 'Trịnh Văn Sơn','trinh.son@gmail.com', '0867234567', 3),
('user123','1234', 'Lê Văn Lương','user123@gmail.com','0856789012', 3),
('user456','1234', 'Trần Thị Mỹ Linh','user456@gmail.com','0845678901', 3);

-- ============================================================
INSERT INTO can_ho (ma_can_ho, dien_tich, tang, loai, trang_thai, hinh_anh) VALUES
('A1-01', 75.5,  1,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A1-02', 45.0,  1,  'Studio',             'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A1-03', 75.5,  1,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A2-01', 80.0,  2,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A2-02', 55.0,  2,  'Studio',             'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A3-01', 90.0,  3,  'Duplex',             'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A3-02', 75.5,  3,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B1-01', 65.0,  1,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B2-01', 60.0,  2,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B3-01', 85.0,  3,  'Duplex',             'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B4-01', 70.0,  4,  'Căn hộ tiêu chuẩn', 'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C1-01', 100.0, 1,  'Duplex',    'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C2-01', 95.0,  2,  'Căn hộ tiêu chuẩn',    'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C3-01', 110.0, 3,  'Penthouse',          'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C4-01', 120.0, 4,  'Penthouse',          'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('D1-01', 75.5,  1,  'Căn hộ tiêu chuẩn', 'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('D2-01', 80.0,  2,  'Căn hộ tiêu chuẩn', 'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('D3-01', 65.0,  3,  'Studio',             'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('E1-01', 90.0,  1,  'Duplex',             'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('E2-01', 75.5,  2,  'Căn hộ tiêu chuẩn', 'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg');

-- ============================================================
INSERT INTO cu_dan (id, ma_can_ho, moi_quan_he, trang_thai, ngay_sinh, cccd) VALUES
( 1,  1, 'Chủ hộ',      'Đang Ở', '1985-05-15', '001085001001'),
( 2,  1, 'Vợ/Chồng',    'Đang Ở', '1988-08-20', '001088001002'),
( 3,  2, 'Chủ hộ',      'Đang Ở', '1990-10-20', '001090002001'),
( 4,  3, 'Chủ hộ',      'Đang Ở', '1988-03-12', '001088003001'),
( 5,  4, 'Chủ hộ',      'Đang Ở', '1992-07-08', '001092004001'),
( 6,  5, 'Chủ hộ',      'Đang Ở', '1998-12-25', '001098005001'),
( 7,  6, 'Chủ hộ',      'Đang Ở', '1993-02-14', '001093006001'),
( 8,  7, 'Chủ hộ',      'Đang Ở', '1994-06-30', '001094007001'),
( 9,  7, 'Vợ/Chồng',    'Đang Ở', '1991-11-11', '001091008001'),
(10,  7, 'Chủ hộ',      'Đang Ở', '1987-04-05', '001087009001'),
(11, 10, 'Chủ hộ',      'Đang Ở', '1995-09-22', '001095010001'),
(12, 11, 'Chủ hộ',      'Đang Ở', '1983-01-30', '001083011001'),
(13, 11, 'Vợ/Chồng',    'Đang Ở', '1996-07-17', '001096012001');


-- ============================================================
INSERT INTO dich_vu (ten, don_gia, don_vi_tinh, loai, mo_ta) VALUES
('Gửi xe máy',           150000,   'Chiếc/Tháng',   'Gửi xe',      'Phí trông giữ xe máy hàng tháng'),
('Gửi ô tô',            1200000,  'Chiếc/Tháng',   'Gửi xe',      'Phí trông giữ ô tô hàng tháng'),
('Gửi xe đạp',           50000,   'Chiếc/Tháng',   'Gửi xe',      'Phí trông giữ xe đạp hàng tháng'),
('Vệ sinh căn hộ nhỏ',   400000,  'Lần',           'Dọn dẹp',     'Dọn dẹp căn hộ dưới 60m2'),
('Vệ sinh căn hộ vừa',   500000,  'Lần',           'Dọn dẹp',     'Dọn dẹp căn hộ 60-90m2'),
('Vệ sinh căn hộ lớn',   650000,  'Lần',           'Dọn dẹp',     'Dọn dẹp căn hộ trên 90m2'),
('Bảo trì máy lạnh',     200000,  'Máy/Lần',       'Sửa chữa',    'Vệ sinh và nạp gas máy lạnh'),
('Sửa chữa điện nước',   300000,  'Lần',           'Sửa chữa',    'Dịch vụ sửa chữa điện nước nhỏ'),
('Phí quản lý',           12000,  'm2/Tháng',       'Cố định',     'Phí quản lý vận hành chung cư'),
('Internet 100Mbps',     250000,  'Gói/Tháng',     'Viễn thông',  'Gói mạng tốc độ 100Mbps'),
('Internet 200Mbps',     350000,  'Gói/Tháng',     'Viễn thông',  'Gói mạng tốc độ 200Mbps'),
('Truyền hình cáp',      150000,  'Gói/Tháng',     'Viễn thông',  'Gói truyền hình cáp cơ bản'),
('Thẻ Gym tháng',        300000,  'Người/Tháng',   'Tiện ích',    'Thẻ thành viên phòng Gym'),
('Thẻ Hồ bơi tháng',    250000,  'Người/Tháng',   'Tiện ích',    'Thẻ thành viên hồ bơi'),
('Thẻ Gym + Hồ bơi',    450000,  'Người/Tháng',   'Tiện ích',    'Thẻ thành viên combo Gym & Hồ bơi'),
('Đặt phòng BBQ',        200000,  'Buổi',          'Tiện ích',    'Thuê khu BBQ sân thượng (3 tiếng)'),
('Đặt phòng họp',        100000,  'Giờ',           'Tiện ích',    'Thuê phòng họp cộng đồng'),
('Giữ trẻ theo giờ',     80000,   'Giờ',           'Chăm sóc',    'Dịch vụ giữ trẻ tại nhà'),
('Vận chuyển nội khu',   100000,  'Lần',           'Vận chuyển',  'Hỗ trợ vận chuyển đồ trong tòa nhà'),
('Lắp đặt thiết bị',     150000,  'Lần',           'Lắp đặt',     'Hỗ trợ lắp đặt thiết bị điện tử');

-- ==============================================================

INSERT INTO tai_san (ma_tai_san, ten_tai_san, vi_tri, tinh_trang, ngay_mua, chu_ky_bao_tri, ngay_bao_tri_tiep_theo) VALUES
('TS-TM-A01', 'Thang máy Block A - Số 1',        'Hành lang Block A, B1-15F',   'Hoạt động tốt','2026-01-15',  6,'2026-07-15'),
('TS-TM-A02', 'Thang máy Block A - Số 2',        'Hành lang Block A, B1-15F',   'Hoạt động tốt','2026-01-15',  6,'2026-07-15'),
('TS-TM-B01', 'Thang máy Block B - Số 1',        'Hành lang Block B, B1-12F',   'Hoạt động tốt','2026-03-01',  6,'2026-09-01'),
('TS-TM-C01', 'Thang máy Block C - Số 1',        'Hành lang Block C, B1-10F',   'Đang hỏng','2021-06-01',  6,'2026-12-01'),
('TS-MF-01',  'Máy phát điện dự phòng 1',        'Tầng hầm B1, phòng kỹ thuật', 'Hoạt động tốt','2026-01-01',  12, '2027-01-01'),
('TS-MF-02',  'Máy phát điện dự phòng 2',        'Tầng hầm B2, phòng kỹ thuật', 'Hoạt động tốt','2026-06-01',  12, '2027-06-01'),
('TS-BP-01',  'Bơm nước tăng áp tầng cao',       'Phòng kỹ thuật tầng 10',     'Hoạt động tốt','2026-02-15',  12, '2028-02-15'),
('TS-BP-02',  'Bơm nước sinh hoạt B1',           'Tầng hầm B1',                'Hoạt động tốt','2026-08-01',  6,'2029-02-01'),
('TS-CC-01',  'Hệ thống camera an ninh Block A', 'Toàn bộ Block A',            'Hoạt động tốt','2026-05-10',  12, '2029-05-10'),
('TS-CC-02',  'Hệ thống camera an ninh Block B', 'Toàn bộ Block B',            'Đang hỏng','2026-05-10',  12, '2029-05-10'),
('TS-CC-03',  'Hệ thống camera hầm xe B1',       'Tầng hầm B1',                'Hoạt động tốt','2026-01-01',  12, '2029-01-01');

-- =======================================================

INSERT INTO khao_sat (tieu_de, mo_ta, thoi_gian_bat_dau, thoi_gian_ket_thuc) VALUES
('Khảo sát chất lượng dịch vụ bảo vệ',        'Chúng tôi muốn biết ý kiến của bạn về đội ngũ bảo vệ tòa nhà trong 6 tháng qua.', '2026-04-01 08:00:00', '2026-05-01 17:00:00'),
('Lấy ý kiến tổ chức Trung Thu cho bé',        'Góp ý về thời gian và địa điểm tổ chức sự kiện Trung Thu 2026.', '2026-08-01 08:00:00', '2026-08-15 17:00:00'),
('Khảo sát nhu cầu lắp thêm trạm sạc xe điện', 'Thu thập ý kiến về việc lắp đặt thêm trạm sạc xe điện tại hầm B2.', '2026-05-10 08:00:00', '2026-06-10 17:00:00'),
('Đánh giá chất lượng hồ bơi chung cư',        'Khảo sát mức độ hài lòng của cư dân về hồ bơi và khu tiện ích.', '2026-06-01 08:00:00', '2026-06-30 17:00:00'),
('Lấy ý kiến về giờ giới nghiêm âm thanh',     'Thăm dò ý kiến cư dân về quy định giờ yên lặng trong khu chung cư.', '2026-07-01 08:00:00', '2026-07-20 17:00:00');

-- ============================================================
INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
-- Khảo sát 1: Chất lượng bảo vệ (4 lựa chọn)
(1, 'Rất hài lòng',       15),
(1, 'Hài lòng',           32),
(1, 'Bình thường',        10),
(1, 'Không hài lòng',      5),
-- Khảo sát 2: Tổ chức Trung Thu (3 lựa chọn)
(2, 'Ngày 14/09/2026',    22),
(2, 'Ngày 15/09/2026',    28),
(2, 'Cả hai ngày đều tốt', 8),
-- Khảo sát 3: Trạm sạc xe điện (3 lựa chọn)
(3, 'Đồng ý, cần lắp gấp', 45),
(3, 'Đồng ý nhưng không gấp', 18),
(3, 'Không cần thiết',      5),
-- Khảo sát 4: Hồ bơi (4 lựa chọn)
(4, 'Rất hài lòng',       12),
(4, 'Hài lòng',           25),
(4, 'Cần cải thiện',      14),
(4, 'Kém chất lượng',      4),
-- Khảo sát 5: Giờ giới nghiêm (3 lựa chọn)
(5, '22h - 6h sáng',      30),
(5, '23h - 7h sáng',      18),
(5, 'Giữ nguyên quy định hiện tại', 12);

-- ===========================================================

INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
(1, 'Chưa tiếp xúc bảo vệ lần nào',  3),
(2, 'Không muốn tổ chức',             2),
(4, 'Chưa sử dụng hồ bơi',           9),
(5, 'Không quan tâm',                 7);

-- ===========================================================

INSERT INTO thong_bao (tieu_de, noi_dung, loai, ngay_dang, doi_tuong_gui, gia_tri_doi_tuong) VALUES 
('Nhắc nhở', 'Vui lòng thanh toán phí quản lý trước ngày 05/05/2026.', 1, '2026-06-04 16:19:27', 'HO_GIA_DINH', 'A1-01'),
('Thông báo cắt nước', 'Tòa nhà sẽ cắt nước để bảo trì bồn chứa vào ngày 10/05/2026 từ 8h00 đến 11h00.', 1, '2026-06-04 16:19:27', 'HO_GIA_DINH', 'A1-01'),
('Thông báo phun thuốc muỗi', 'Ban quản lý sẽ phun thuốc muỗi khu vực hành lang và khuôn viên vào tối thứ 7, ngày 18/05/2026 lúc 20h00.', 1, '2026-06-04 16:19:27', 'HO_GIA_DINH', 'A1-01'),
('Thông báo kiểm tra PCCC', 'Đoàn kiểm tra PCCC sẽ đến kiểm tra tất cả các căn hộ vào ngày 20/05/2026. Đề nghị cư dân tạo điều kiện và có mặt tại nhà.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Sự kiện Ngày hội cư dân', 'Ban quản lý tổ chức Ngày hội cư dân vào Chủ nhật 26/05/2026. Nhiều hoạt động thú vị và quà tặng hấp dẫn!', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Bảo trì thang máy', 'Thang máy Block A sẽ được bảo trì vào ngày 30/05/2026. Trong thời gian này, thang máy sẽ ngừng hoạt động từ 8h00 đến 17h00.', 1, '2026-06-04 16:19:27', 'TANG', '1'),
('Thông báo thu tiền phí', 'Hóa đơn phí quản lý tháng 5 đã sẵn sàng. Vui lòng liên hệ ban quản lý để nhận và thanh toán.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Bảo trì hệ thống điện', 'Sẽ có bảo trì định kỳ hệ thống điện chính từ 22h đến 6h sáng trong tuần tới.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Khảo sát sự hài lòng', 'Vui lòng tham gia khảo sát chất lượng dịch vụ bảo vệ. Thời hạn: 01/05/2026.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Sự kiện Trung Thu', 'Công ty quản lý sẽ tổ chức sự kiện Trung Thu cho các em nhỏ vào tháng 9 tới.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Cập nhật giá dịch vụ', 'Từ ngày 01/07/2026, một số dịch vụ sẽ điều chỉnh giá theo hợp đồng mới.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Thông báo tạm khóa thang máy', 'Thang máy Block B sẽ tạm khóa để sửa chữa từ 15/05 đến 20/05/2026.', 1, '2026-06-04 16:19:27', 'TANG', '2'),
('Nhắc nhở thanh toán hợp đồng', 'Cư dân nên kiểm tra lại hợp đồng thuê căn hộ và nộp các giấy tờ cần thiết.', 1, '2026-06-04 16:19:27', 'HO_GIA_DINH', 'A2-01'),
('Sự kiện giảm giá dịch vụ', 'Trong tháng 6, tất cả dịch vụ gym sẽ được giảm giá 20% cho thành viên mới.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Thông báo tăng cường bảo vệ', 'Để nâng cao an ninh, ban quản lý sẽ tăng cường nhân lực bảo vệ ban đêm.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL'),
('Khóa học kỹ năng sống', 'Mới cư dân tham gia khóa đào tạo kỹ năng sống vào cuối tháng 5.', 1, '2026-06-04 16:19:27', 'ALL', 'ALL');

-- =================================================================

INSERT INTO hop_dong (can_ho_id, cu_dan_id, loai_hop_dong, gia_tri_hop_dong, tien_coc, tien_thue, ngay_bat_dau, ngay_ket_thuc, ben_cho_thue, ben_thue, trang_thai) VALUES
(1, 1, 'Thue', 120000000, 20000000, 10000000, '2026-01-01', '2027-01-01', 'Ban quản lý chung cư', 'Lê Văn Thắng', 'ACTIVE'),
(2, 3, 'Thue', 72000000, 12000000, 6000000, '2026-03-01', '2027-03-01', 'Ban quản lý chung cư', 'Phạm Minh Hoàng', 'ACTIVE'),
(3, 4, 'Thue', 120000000, 20000000, 10000000, '2026-02-15', '2027-02-15', 'Ban quản lý chung cư', 'Trần Bích Thủy', 'ACTIVE'),
(4, 5, 'Mua', 2500000000, 500000000, 0, '2025-06-01', NULL, 'Công Ty Đầu Tư Địa Ốc', 'Đặng Văn Hùng', 'ACTIVE'),
(5, 6, 'Thue', 84000000, 14000000, 7000000, '2026-04-01', '2027-04-01', 'Ban quản lý chung cư', 'Hoàng Lan Anh', 'ACTIVE'),
(6, 7, 'Mua', 3000000000, 600000000, 0, '2025-08-10', NULL, 'Công Ty Đầu Tư Địa Ốc', 'Vũ Quốc Anh', 'ACTIVE'),
(7, 8, 'Thue', 108000000, 18000000, 9000000, '2026-05-01', '2027-05-01', 'Ban quản lý chung cư', 'Ngô Thanh Vân', 'ACTIVE'),
(10, 11, 'Thue', 144000000, 24000000, 12000000, '2025-01-15', '2026-01-15', 'Ban quản lý chung cư', 'Cao Ngọc Phương', 'EXPIRED'),
(11, 12, 'Mua', 2200000000, 440000000, 0, '2025-12-01', NULL, 'Công Ty Đầu Tư Địa Ốc', 'Lý Kim Phi', 'ACTIVE');

-- ================================================================

INSERT INTO phuong_tien (can_ho_id, bien_so, loai, mau_sac, trang_thai, hinh_anh) VALUES
(1,'29A-123.45', 'Xe máy',  'Đỏ',       'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(1,'30H-999.88', 'Ô tô',    'Trắng',    'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(1,'29C-111.22', 'Xe máy',  'Đen',      'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(2,'29C-567.89', 'Xe máy',  'Xanh',     'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(3,'51G-444.22', 'Ô tô',    'Đen',      'Từ chối', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(3,'29P-222.11', 'Xe máy',  'Bạc',      'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(4,'30K-555.66', 'Xe máy',  'Trắng',    'Chờ duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(4,'29T-777.33', 'Ô tô',    'Xám',      'Đã duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(5,'29D-888.99', 'Xe máy',  'Xám',      'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(6,'51B-100.10', 'Xe đạp',  'Xanh lá',  'Từ chối', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(6,'29A-200.20', 'Xe máy',  'Vàng',     'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(7,'30G-300.30', 'Xe máy',  'Đỏ',       'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(8,'29E-400.40', 'Ô tô',    'Xanh navy','Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(8,'29K-500.50', 'Xe máy',  'Trắng',    'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(9,'51H-600.60', 'Xe máy',  'Đen',      'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(10,'51M-777.77', 'Xe máy', 'Đỏ',       'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(11,'29X-888.88', 'Ô tô',   'Trắng',    'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(12,'51N-999.99', 'Xe đạp', 'Xanh',     'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(13,'30M-111.11', 'Xe máy', 'Vàng',     'Từ chối', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(1,'29R-222.22', 'Ô tô',    'Đen',      'Đã duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(2,'51P-333.33', 'Xe máy',  'Bạc',      'Chờ duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(3,'29Q-444.44', 'Xe máy',  'Xám',      'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(4,'51Q-555.55', 'Xe đạp',  'Xanh lá',  'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(5,'29S-666.66', 'Ô tô',    'Bạc',      'Từ chối', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
(6,'30N-777.77', 'Xe máy',  'Trắng',    'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
(7,'29T-888.88', 'Ô tô',    'Đỏ',       'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png');

-- ============================================================

INSERT INTO kien_hang (can_ho_id, nguoi_gui, nguoi_nhan, ngay_nhan, trang_thai) VALUES
(1,  'Shopee',          'Lê Văn Thắng','2026-04-28', 'Đã nhận'),
(1,  'Tiki',            'Nguyễn Thị Mai', '2026-05-02', 'Đã nhận'),
(2,  'Lazada',          'Phạm Minh Hoàng','2026-05-05', 'Đã nhận'),
(3,  'Grab Express',    'Trần Bích Thủy', '2026-05-08', 'Đã nhận'),
(4,  'GHTK',            'Đặng Văn Hùng','2026-05-10', 'Đã nhận'),
(5,  'J&T Express',     'Hoàng Lan Anh','2026-05-12', 'Chờ nhận'),
(6,  'Viettel Post',    'Vũ Quốc Anh', '2026-05-13', 'Chờ nhận'),
(7,  'Shopee',          'Ngô Thanh Vân','2026-05-14', 'Chờ nhận'),
(8,  'Tiki',            'Đỗ Minh Tuấn','2026-05-14', 'Chờ nhận'),
(9,  'DHL',             'Bùi Thu Hà','2026-05-15', 'Chờ nhận');

-- ============================================================

insert into phan_anh (can_ho_id, tieu_de, noi_dung, ngay_gui, phan_hoi, ngay_phan_hoi, trang_thai, hinh_anh)
values  
(1, 'Hành lang tầng 1 bẩn', 'Hành lang tầng 1 có rác chưa được dọn từ sáng sớm, gây mùi khó chịu.', '2026-04-28 09:00:00', 'Đã điều nhân viên vệ sinh kiểm tra và dọn dẹp lúc 10h30.', '2026-04-28 10:30:00', 'Chờ xử lý', null),
(3, 'Tiếng ồn ban đêm từ tầng trên', 'Căn hộ tầng trên thường xuyên gây tiếng ồn sau 23h, ảnh hưởng giấc ngủ.', '2026-04-20 23:30:00', 'Đã nhắc nhở chủ hộ tầng trên tuân thủ nội quy giờ yên lặng.', '2026-04-21 09:00:00', 'Chờ xử lý', null),
(7, 'Đèn hành lang bị nhấp nháy', 'Đèn LED trước cửa căn hộ A1-03 nhấp nháy liên tục từ tuần trước.', '2026-05-01 19:00:00', '', null, 'Đang xử lý', null),
(2, 'Thang máy hay bị dừng giữa chừng', 'Thang máy Block A số 1 thường xuyên dừng và rung nhẹ giữa các tầng.', '2026-05-03 14:00:00', 'Đã báo đơn vị bảo trì thang máy, dự kiến kiểm tra ngày 06/05.', '2026-05-03 15:30:00', 'Đang xử lý', null),
(5, 'Bồn hoa lối vào xuống cấp', 'Bồn hoa trang trí lối vào Block A bị vỡ và cây chết, trông mất thẩm mỹ.', '2026-05-05 10:00:00', 'Ghi nhận, đã lên kế hoạch cải tạo cảnh quan vào tháng 6.', '2026-05-06 08:00:00', 'Đã xong', null),
(4, 'Bãi xe đầy, không có chỗ gửi', 'Tầng hầm B1 thường xuyên hết chỗ từ 18h-20h, xe phải gửi bên ngoài tốn thêm chi phí.', '2026-05-07 20:00:00', 'Đang nghiên cứu thêm khu vực gửi xe phụ tại tầng hầm B2.', '2026-05-08 09:00:00', 'Đã xong', null),
(8, 'Nước yếu vào giờ cao điểm', 'Áp lực nước rất yếu vào buổi sáng từ 6h-8h, không đủ dùng sinh hoạt.', '2026-05-09 07:30:00', null, null, 'Chờ xử lý', null),
(9, 'Cửa kính sảnh bị hỏng khóa', 'Cửa kính sảnh tầng trệt Block B không đóng được chặt, gây mất an ninh.', '2026-05-10 11:00:00', 'Đã sửa chữa khóa cửa ngay trong ngày.', '2026-05-10 15:00:00', 'Chờ xử lý', null),
(6, 'Hồ bơi nhiều rong rêu', 'Hồ bơi có váng màu xanh và rong rêu ở góc bể, e ngại vệ sinh.', '2026-05-11 16:00:00', 'Đã xong nước và vệ sinh hồ bơi, hoạt động bình thường từ 13/5.', '2026-05-12 08:00:00', 'Chờ xử lý', null),
(10, 'Rò rỉ nước từ tầng trên', 'Tường phòng khách bị rò rỉ nước khiến sơn bị ướt và nứt.', '2026-05-12 10:00:00', 'Đã yêu cầu cư dân tầng trên kiểm tra và khắc phục.', '2026-05-13 09:00:00', 'Đang xử lý', null),
(11, 'Chất lượng nước sinh hoạt', 'Nước có mùi lạ và màu hơi vàng, không rõ là do ống hay bể chứa.', '2026-05-13 08:00:00', 'Đã kiểm tra hệ thống, sẽ vệ sinh bể chứa tuần tới.', '2026-05-14 15:00:00', 'Chờ xử lý', null),
(12, 'Mất nước áp lực', 'Áp lực nước bị mất hoàn toàn trong 2 giờ tối hôm 14/5.', '2026-05-14 18:00:00', null, null, 'Chờ xử lý', null),
(1, 'Thông thoáng gió kém', 'Hệ thống thông khí chung không hoạt động, gây ẩm mốc.', '2026-05-15 12:00:00', 'Đã kiểm tra và làm sạch ống thông khí.', '2026-05-16 10:00:00', 'Đang xử lý', null),
(3, 'Tủ điện chung bị khóa sai', 'Cánh tủ điện chung tầng 3 khóa không khít, gây nguy hiểm.', '2026-05-16 09:00:00', 'Đã sửa lại khóa và kiểm tra an toàn.', '2026-05-16 14:00:00', 'Đang xử lý', null),
(5, 'Sảnh lối ra vào bị bẩn', 'Sảnh chính tòa nhà chưa được lau dọn từ sáng sớm.', '2026-05-17 07:00:00', 'Đã nhắc nhở nhân viên vệ sinh kiểm tra thêm lần.', '2026-05-17 08:00:00', 'Chờ xử lý', null),
(8, 'Bậc cầu thang bị nứt', 'Một vài bậc cầu thang tầng 8 bị nứt, có nguy hiểm trơn trượt.', '2026-05-18 14:00:00', 'Đã báo đơn vị sửa chữa, dự kiến sửa tuần sau.', '2026-05-18 14:00:00', 'Đang xử lý', null),
(1, 'Hư máy lạnh', 'Máy lạnh hư không thể sử dụng Vui lòng sửa gấp', '2026-06-04 19:17:20', null, null, 'Chờ xử lý', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780575441/canho/l9nvkzffb9fzxqvoebv7.png');

-- ============================================================
INSERT INTO dang_ky_khach_tham
(cu_dan_id, ten_khach, cmnd, bien_so_xe, thoi_gian_du_kien, thoi_gian_duyet, trang_thai, ngay_di)
VALUES
(1,  'Nguyễn Văn Bình', '001090112233', '29F1-000.01', '2026-05-02 19:00:00', '2026-05-01 15:00:00', 'Đã duyệt', '2026-05-02 00:00:00'),
(1, 'Trần Thị Cúc',    '001095223344', NULL,           '2026-05-05 10:00:00', '2026-05-04 14:00:00', 'Đã duyệt', '2026-05-05 00:00:00'),
(2, 'Lê Văn Dũng',     '001088334455', '51A-123.00',  '2026-05-06 14:00:00', '2026-05-06 09:00:00', 'Đã duyệt', '2026-05-06 00:00:00'),
(3, 'Phạm Thị Én',     '001092445566', NULL,           '2026-05-08 09:00:00', NULL,                  'Chờ duyệt', '2026-05-08 00:00:00'),
(4, 'Đặng Văn Giang',  '001091556677', '29H-456.00',  '2026-05-10 15:00:00', '2026-05-10 08:00:00', 'Đã duyệt', '2026-05-10 00:00:00'),
(5, 'Hoàng Thị Hoa',   '001097667788', NULL,           '2026-05-12 10:00:00', '2026-05-11 16:00:00', 'Đã duyệt', '2026-05-12 00:00:00'),
(6, 'Vũ Văn Kiên',     '001086778899', '30D-789.00',  '2026-05-15 18:00:00', NULL,                  'Chờ duyệt', '2026-05-15 00:00:00'),
(6, 'Ngô Thị Linh',    '001093889900', NULL,           '2026-05-16 11:00:00', '2026-05-16 09:00:00', 'Đã duyệt', '2026-05-16 00:00:00'),
(7, 'Đỗ Văn Minh',     '001085990011', '29A-012.00',  '2026-05-18 09:00:00', '2026-05-17 15:00:00', 'Đã duyệt', '2026-05-18 00:00:00');

-- ============================================================
INSERT INTO dat_dich_vu (cu_dan_id, dich_vu_id, ngay_dat, ngay_ket_thuc, thoi_gian_duyet, trang_thai, ghi_chu) VALUES
(1, 14, '2026-05-05', '2026-06-05', '2026-05-05 10:00:00', 'Đã sử dụng', 'Đăng ký thẻ Hồ bơi tháng 5'),
(3,  4, '2026-05-06', '2026-05-06', '2026-05-07 08:00:00', 'Đã duyệt', 'Vệ sinh nhà buổi sáng'),
(5, 16, '2026-05-08', '2026-05-08', '2026-05-08 14:00:00', 'Đã duyệt', 'Đặt khu BBQ tối thứ 7'),
(1, 10, '2026-05-09', '2026-06-09', '2026-05-09 09:00:00', 'Đã duyệt', 'Gói internet tháng 5'),
(4,  8, '2026-05-10', '2026-05-10', NULL,                  'Chờ duyệt',   'Sửa ống nước bị rỉ trong bếp'),
(5, 19, '2026-05-11', '2026-05-11', NULL,                  'Chờ duyệt',   'Chuyển tủ lạnh từ phòng kho ra phòng bếp'),
(3, 13, '2026-05-12', '2026-06-12', '2026-05-12 10:00:00', 'Đã duyệt',   'Đăng ký thẻ Gym tháng 5'),
(2,  4, '2026-05-13', '2026-05-13', NULL,                  'Chờ duyệt',   'Dọn nhà buổi chiều'),
(2, 12, '2026-05-14', '2026-06-14', '2026-05-14 11:00:00', 'Đã duyệt',   'Gói truyền hình cáp tháng 5'),
(8,  15, '2026-05-15', '2026-06-15', '2026-05-15 09:00:00', 'Đã duyệt',   'Thẻ hồ bơi tháng 5'),
(9,   7, '2026-05-01', '2026-05-01', '2026-05-01 10:00:00', 'Đã sử dụng', 'Bảo trì 2 máy lạnh phòng ngủ và phòng khách'),
(7, 17, '2026-05-16', '2026-05-16', '2026-05-16 14:00:00', 'Đã duyệt',   'Thuê phòng họp 2 tiếng tổ chức sinh nhật mini'),
(10, 1, '2026-05-17', '2026-06-17', '2026-05-17 10:00:00', 'Đã duyệt',   'Gửi xe máy tháng 5'),
(11, 2, '2026-05-18', '2026-06-18', '2026-05-18 08:00:00', 'Đã duyệt',   'Gửi ô tô tháng 5'),
(12, 3, '2026-05-19', '2026-06-19', NULL,                  'Chờ duyệt',   'Gửi xe đạp tháng 5'),
(13, 5, '2026-05-20', '2026-05-20', '2026-05-20 09:00:00', 'Đã duyệt',   'Vệ sinh căn hộ vừa');

-- ============================================================

INSERT INTO lich_su_vote (cu_dan_id, khao_sat_id, lua_chon_id, thoi_gian_vote) VALUES
-- Khảo sát 1 (lua_chon: 1=Rất hài lòng, 2=Hài lòng, 3=Bình thường, 4=Không hài lòng, 17=Chưa tiếp xúc)
(8,   1, 1,'2026-04-10 09:00:00'),
(10,  1, 2,'2026-04-11 10:00:00'),
(11,  1, 2,'2026-04-12 11:00:00'),
(12,  1, 3,'2026-04-13 09:30:00'),
(13,  1, 4,'2026-04-14 14:00:00'),
-- Khảo sát 2 (lua_chon: 5=Ngày 14/09, 6=Ngày 15/09, 7=Cả hai đều tốt, 18=Không muốn tổ chức)
(8,   2, 6,'2026-08-03 09:00:00'),
(10,  2, 5,'2026-08-04 10:00:00'),
(11,  2, 7,'2026-08-05 11:00:00'),
(4,  2, 6,'2026-08-06 09:00:00'),
(5,  2, 5,'2026-08-07 14:00:00'),
-- Khảo sát 3 (lua_chon: 8=Đồng ý gấp, 9=Đồng ý không gấp, 10=Không cần)
(8,   3, 8,'2026-05-11 14:00:00'),
(1,  3, 8,'2026-05-12 08:00:00'),
(3,  3, 9,'2026-05-13 10:00:00'),
(2,   3, 10, '2026-05-15 11:00:00'),
-- Khảo sát 4 (lua_chon: 11=Rất hài lòng, 12=Hài lòng, 13=Cần cải thiện, 14=Kém, 19=Chưa dùng)
(9,   4, 12, '2026-06-05 09:00:00'),
(2,  4, 11, '2026-06-06 10:00:00'),
(3,   4, 13, '2026-06-07 11:00:00'),
-- Khảo sát 5 (lua_chon: 15=22h-6h, 16=23h-7h, 17=Giữ nguyên, 20=Không quan tâm)
(9,   5, 15, '2026-07-05 09:00:00'),
(10,  5, 16, '2026-07-06 10:00:00');


-- ============================================================
INSERT INTO lich_su_bao_tri (tai_san_id, ngay_bao_tri, noi_dung, chi_phi, nguoi_thuc_hien) VALUES
(1,'2023-01-15', 'Kiểm tra định kỳ 6 tháng, tra dầu mỡ cơ cấu, thử tải',                  2500000,  'Công ty Thang máy Otis VN'),
(2,'2023-01-15', 'Kiểm tra định kỳ 6 tháng, thay cáp dây an toàn',                          3200000,  'Công ty Thang máy Otis VN'),
(3,'2026-09-01', 'Bảo trì định kỳ lần đầu sau 6 tháng vận hành',                            2000000,  'Công ty Thang máy Kone'),
(4,'2026-12-01', 'Thay toàn bộ bo mạch điều khiển thang máy bị cháy',                       15000000, 'Công ty Thang máy Mitsubishi'),
(5,'2023-01-01', 'Kiểm tra, thay lọc dầu và ắc quy máy phát điện dự phòng 1',               1800000,  'Công ty Điện lực TNHH Hùng Phát'),
(6,'2023-06-01', 'Bảo dưỡng định kỳ máy phát điện 2, thử tải 100%',                         2200000,  'Công ty Điện lực TNHH Hùng Phát'),
(7,'2026-02-15', 'Thay mới bơm nước tăng áp tầng cao, kiểm tra đường ống áp lực',           8500000,  'Công ty CP Nước sạch Hà Nội'),
(8,'2023-02-01', 'Vệ sinh màng lọc, kiểm tra motor bơm',                                     1200000,  'Đội kỹ thuật nội bộ'),
(9,'2023-05-10', 'Nâng cấp firmware hệ thống camera Block A, thay 3 camera hỏng',            5000000,  'Công ty An ninh Việt Security'),
(10, '2023-05-10', 'Thay toàn bộ 8 camera hỏng Block B, nâng lên độ phân giải 4K',            12000000, 'Công ty An ninh Việt Security'),
(11, '2026-01-01', 'Kiểm tra và vệ sinh camera hầm B1, căn chỉnh góc quan sát',               800000,   'Đội kỹ thuật nội bộ');
