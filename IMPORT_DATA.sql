
USE QL_ChungCu;
SET NOCOUNT ON;

-- ===================================================================

INSERT INTO nguoi_dung (ten_dang_nhap, mat_khau_ma_hoa, ho_ten, email, so_dien_thoai, vai_tro) VALUES
('levan_thang','1234', N'Lê Văn Thắng','levan.thang@gmail.com','0987654321', 3),
('nguyen_mai','1234', N'Nguyễn Thị Mai','nguyen.mai@gmail.com','0987654322', 3),
('pham_hoang','1234', N'Phạm Minh Hoàng','pham.hoang@gmail.com','0977112233', 3),
('tran_thuy','1234', N'Trần Bích Thủy','tran.thuy@gmail.com', '0966445566', 3),
('dang_hung','1234', N'Đặng Văn Hùng', 'dang.hung@gmail.com', '0955889900', 3),
('hoang_anh','1234', N'Hoàng Lan Anh', 'hoang.anh@gmail.com', '0944773322', 3),
('vu_quocanh','1234', N'Vũ Quốc Anh','vu.quocanh@gmail.com','0933221100', 3),
('ngo_thanvan', '1234', N'Ngô Thanh Vân','ngo.thanvan@gmail.com','0922334455', 3),
('do_minhtuan', '1234', N'Đỗ Minh Tuấn','do.minhtuan@gmail.com','0911223344', 3),
('bui_thuha','1234', N'Bùi Thu Hà','bui.thuha@gmail.com', '0900112233', 3),
('cao_phuong','1234', N'Cao Ngọc Phương','cao.phuong@gmail.com','0889012345', 3),
('ly_kimphi','1234', N'Lý Kim Phi','ly.kimphi@gmail.com', '0878123456', 3),
('trinh_son','1234', N'Trịnh Văn Sơn','trinh.son@gmail.com', '0867234567', 3),
('user123','1234', N'Lê Văn Lương','user123@gmail.com','0856789012', 3),
('user456','1234', N'Trần Thị Mỹ Linh','user456@gmail.com','0845678901', 3);

-- ============================================================
INSERT INTO can_ho (ma_can_ho, dien_tich, tang, loai, trang_thai, hinh_anh) VALUES
('A1-01', 75.5,  1,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A1-02', 45.0,  1,  N'Studio',             N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A1-03', 75.5,  1,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A2-01', 80.0,  2,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A2-02', 55.0,  2,  N'Studio',             N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A3-01', 90.0,  3,  N'Duplex',             N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('A3-02', 75.5,  3,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B1-01', 65.0,  1,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B2-01', 60.0,  2,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B3-01', 85.0,  3,  N'Duplex',             N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('B4-01', 70.0,  4,  N'Căn hộ tiêu chuẩn', N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C1-01', 100.0, 1,  N'Duplex',    N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C2-01', 95.0,  2,  N'Căn hộ tiêu chuẩn',    N'Đã có chủ','https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C3-01', 110.0, 3,  N'Penthouse',          N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('C4-01', 120.0, 4,  N'Penthouse',          N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('D1-01', 75.5,  1,  N'Căn hộ tiêu chuẩn', N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('D2-01', 80.0,  2,  N'Căn hộ tiêu chuẩn', N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('D3-01', 65.0,  3,  N'Studio',             N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('E1-01', 90.0,  1,  N'Duplex',             N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg'),
('E2-01', 75.5,  2,  N'Căn hộ tiêu chuẩn', N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780563759/canho/g4iltpgjlfuavc5twyt0.jpg');

-- ============================================================
INSERT INTO cu_dan (id, ma_can_ho, moi_quan_he, trang_thai, ngay_sinh, cccd) VALUES
( 1,  1, N'Chủ hộ',      N'Đang Ở', '1985-05-15', '001085001001'),
( 2,  1, N'Vợ/Chồng',    N'Đang Ở', '1988-08-20', '001088001002'),
( 3,  2, N'Chủ hộ',      N'Đang Ở', '1990-10-20', '001090002001'),
( 4,  3, N'Chủ hộ',      N'Đang Ở', '1988-03-12', '001088003001'),
( 5,  4, N'Chủ hộ',      N'Đang Ở', '1992-07-08', '001092004001'),
( 6,  5, N'Chủ hộ',      N'Đang Ở', '1998-12-25', '001098005001'),
( 7,  6, N'Chủ hộ',      N'Đang Ở', '1993-02-14', '001093006001'),
( 8,  7, N'Chủ hộ',      N'Đang Ở', '1994-06-30', '001094007001'),
( 9,  7, N'Vợ/Chồng',    N'Đang Ở', '1991-11-11', '001091008001'),
(10,  7, N'Chủ hộ',      N'Đang Ở', '1987-04-05', '001087009001'),
(11, 10, N'Chủ hộ',      N'Đang Ở', '1995-09-22', '001095010001'),
(12, 11, N'Chủ hộ',      N'Đang Ở', '1983-01-30', '001083011001'),
(13, 11, N'Vợ/Chồng',    N'Đang Ở', '1996-07-17', '001096012001');


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

-- ==============================================================

INSERT INTO tai_san (ma_tai_san, ten_tai_san, vi_tri, tinh_trang, ngay_mua, chu_ky_bao_tri, ngay_bao_tri_tiep_theo) VALUES
('TS-TM-A01', N'Thang máy Block A - Số 1',        N'Hành lang Block A, B1-15F',   N'Hoạt động tốt','2026-01-15',  6,'2026-07-15'),
('TS-TM-A02', N'Thang máy Block A - Số 2',        N'Hành lang Block A, B1-15F',   N'Hoạt động tốt','2026-01-15',  6,'2026-07-15'),
('TS-TM-B01', N'Thang máy Block B - Số 1',        N'Hành lang Block B, B1-12F',   N'Hoạt động tốt','2026-03-01',  6,'2026-09-01'),
('TS-TM-C01', N'Thang máy Block C - Số 1',        N'Hành lang Block C, B1-10F',   N'Đang hỏng','2021-06-01',  6,'2026-12-01'),
('TS-MF-01',  N'Máy phát điện dự phòng 1',        N'Tầng hầm B1, phòng kỹ thuật',N'Hoạt động tốt','2026-01-01',  12, '2027-01-01'),
('TS-MF-02',  N'Máy phát điện dự phòng 2',        N'Tầng hầm B2, phòng kỹ thuật',N'Hoạt động tốt','2026-06-01',  12, '2027-06-01'),
('TS-BP-01',  N'Bơm nước tăng áp tầng cao',       N'Phòng kỹ thuật tầng 10',     N'Hoạt động tốt','2026-02-15',  12, '2028-02-15'),
('TS-BP-02',  N'Bơm nước sinh hoạt B1',           N'Tầng hầm B1',                N'Hoạt động tốt','2026-08-01',  6,'2029-02-01'),
('TS-CC-01',  N'Hệ thống camera an ninh Block A', N'Toàn bộ Block A',            N'Hoạt động tốt','2026-05-10',  12, '2029-05-10'),
('TS-CC-02',  N'Hệ thống camera an ninh Block B', N'Toàn bộ Block B',            N'Đang hỏng','2026-05-10',  12, '2029-05-10'),
('TS-CC-03',  N'Hệ thống camera hầm xe B1',       N'Tầng hầm B1',                N'Hoạt động tốt','2026-01-01',  12, '2029-01-01');

-- =======================================================

INSERT INTO khao_sat (tieu_de, mo_ta, thoi_gian_bat_dau, thoi_gian_ket_thuc) VALUES
(N'Khảo sát chất lượng dịch vụ bảo vệ',        N'Chúng tôi muốn biết ý kiến của bạn về đội ngũ bảo vệ tòa nhà trong 6 tháng qua.', '2026-04-01 08:00:00', '2026-05-01 17:00:00'),
(N'Lấy ý kiến tổ chức Trung Thu cho bé',        N'Góp ý về thời gian và địa điểm tổ chức sự kiện Trung Thu 2026.', '2026-08-01 08:00:00', '2026-08-15 17:00:00'),
(N'Khảo sát nhu cầu lắp thêm trạm sạc xe điện', N'Thu thập ý kiến về việc lắp đặt thêm trạm sạc xe điện tại hầm B2.', '2026-05-10 08:00:00', '2026-06-10 17:00:00'),
(N'Đánh giá chất lượng hồ bơi chung cư',        N'Khảo sát mức độ hài lòng của cư dân về hồ bơi và khu tiện ích.', '2026-06-01 08:00:00', '2026-06-30 17:00:00'),
(N'Lấy ý kiến về giờ giới nghiêm âm thanh',     N'Thăm dò ý kiến cư dân về quy định giờ yên lặng trong khu chung cư.', '2026-07-01 08:00:00', '2026-07-20 17:00:00');
-- Tổng: 5 khảo sát | IDs: 1-5

-- ============================================================
INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
-- Khảo sát 1: Chất lượng bảo vệ (4 lựa chọn)
(1, N'Rất hài lòng',       15),
(1, N'Hài lòng',           32),
(1, N'Bình thường',        10),
(1, N'Không hài lòng',      5),
-- Khảo sát 2: Tổ chức Trung Thu (3 lựa chọn)
(2, N'Ngày 14/09/2026',    22),
(2, N'Ngày 15/09/2026',    28),
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

-- ===========================================================

INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
(1, N'Chưa tiếp xúc bảo vệ lần nào',  3),
(2, N'Không muốn tổ chức',             2),
(4, N'Chưa sử dụng hồ bơi',           9),
(5, N'Không quan tâm',                 7);

-- ===========================================================

INSERT INTO thong_bao (tieu_de, noi_dung, loai, ngay_dang, doi_tuong_gui, gia_tri_doi_tuong) VALUES 
(N'Nhắc nhở', N'Vui lòng thanh toán phí quản lý trước ngày 05/05/2026.', 1, N'2026-06-04T16:19:27.3', N'HO_GIA_DINH', N'A1-01'),
(N'Thông báo cắt nước', N'Tòa nhà sẽ cắt nước để bảo trì bồn chứa vào ngày 10/05/2026 từ 8h00 đến 11h00.', 1, N'2026-06-04T16:19:27.3', N'HO_GIA_DINH', N'A1-01'),
(N'Thông báo phun thuốc muỗi', N'Ban quản lý sẽ phun thuốc muỗi khu vực hành lang và khuôn viên vào tối thứ 7, ngày 18/05/2026 lúc 20h00.', 1, N'2026-06-04T16:19:27.3', N'HO_GIA_DINH', N'A1-01'),
(N'Thông báo kiểm tra PCCC', N'Đoàn kiểm tra PCCC sẽ đến kiểm tra tất cả các căn hộ vào ngày 20/05/2026. Đề nghị cư dân tạo điều kiện và có mặt tại nhà.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Sự kiện Ngày hội cư dân', N'Ban quản lý tổ chức Ngày hội cư dân vào Chủ nhật 26/05/2026. Nhiều hoạt động thú vị và quà tặng hấp dẫn!', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Bảo trì thang máy', N'Thang máy Block A sẽ được bảo trì vào ngày 30/05/2026. Trong thời gian này, thang máy sẽ ngừng hoạt động từ 8h00 đến 17h00.', 1, N'2026-06-04T16:19:27.3', N'TANG', N'1'),
(N'Thông báo thu tiền phí', N'Hóa đơn phí quản lý tháng 5 đã sẵn sàng. Vui lòng liên hệ ban quản lý để nhận và thanh toán.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Bảo trì hệ thống điện', N'Sẽ có bảo trì định kỳ hệ thống điện chính từ 22h đến 6h sáng trong tuần tới.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Khảo sát sự hài lòng', N'Vui lòng tham gia khảo sát chất lượng dịch vụ bảo vệ. Thời hạn: 01/05/2026.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Sự kiện Trung Thu', N'Công ty quản lý sẽ tổ chức sự kiện Trung Thu cho các em nhỏ vào tháng 9 tới.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Cập nhật giá dịch vụ', N'Từ ngày 01/07/2026, một số dịch vụ sẽ điều chỉnh giá theo hợp đồng mới.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Thông báo tạm khóa thang máy', N'Thang máy Block B sẽ tạm khóa để sửa chữa từ 15/05 đến 20/05/2026.', 1, N'2026-06-04T16:19:27.3', N'TANG', N'2'),
(N'Nhắc nhở thanh toán hợp đồng', N'Cư dân nên kiểm tra lại hợp đồng thuê căn hộ và nộp các giấy tờ cần thiết.', 1, N'2026-06-04T16:19:27.3', N'HO_GIA_DINH', N'A2-01'),
(N'Sự kiện giảm giá dịch vụ', N'Trong tháng 6, tất cả dịch vụ gym sẽ được giảm giá 20% cho thành viên mới.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Thông báo tăng cường bảo vệ', N'Để nâng cao an ninh, ban quản lý sẽ tăng cường nhân lực bảo vệ ban đêm.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL'),
(N'Khóa học kỹ năng sống', N'Mời cư dân tham gia khóa đào tạo kỹ năng sống vào cuối tháng 5.', 1, N'2026-06-04T16:19:27.3', N'ALL', N'ALL');
-- Tổng: 20 thông báo | IDs: 1-20

-- =================================================================

INSERT INTO hop_dong (can_ho_id, cu_dan_id, loai_hop_dong, gia_tri_hop_dong, tien_coc, tien_thue, ngay_bat_dau, ngay_ket_thuc, ben_cho_thue, ben_thue, trang_thai) VALUES
(1, 1, N'Thue', 120000000, 20000000, 10000000, N'2026-01-01', N'2027-01-01', N'Ban quản lý chung cư', N'Lê Văn Thắng', N'ACTIVE'),
(2, 3, N'Thue', 72000000, 12000000, 6000000, N'2026-03-01', N'2027-03-01', N'Ban quản lý chung cư', N'Phạm Minh Hoàng', N'ACTIVE'),
(3, 4, N'Thue', 120000000, 20000000, 10000000, N'2026-02-15', N'2027-02-15', N'Ban quản lý chung cư', N'Trần Bích Thủy', N'ACTIVE'),
(4, 5, N'Mua', 2500000000, 500000000, 0, N'2025-06-01', NULL, N'Công Ty Đầu Tư Địa Ốc', N'Đặng Văn Hùng', N'ACTIVE'),
(5, 6, N'Thue', 84000000, 14000000, 7000000, N'2026-04-01', N'2027-04-01', N'Ban quản lý chung cư', N'Hoàng Lan Anh', N'ACTIVE'),
(6, 7, N'Mua', 3000000000, 600000000, 0, N'2025-08-10', NULL, N'Công Ty Đầu Tư Địa Ốc', N'Vũ Quốc Anh', N'ACTIVE'),
(7, 8, N'Thue', 108000000, 18000000, 9000000, N'2026-05-01', N'2027-05-01', N'Ban quản lý chung cư', N'Ngô Thanh Vân', N'ACTIVE'),
(10, 11, N'Thue', 144000000, 24000000, 12000000, N'2025-01-15', N'2026-01-15', N'Ban quản lý chung cư', N'Cao Ngọc Phương', N'EXPIRED'),
(11, 12, N'Mua', 2200000000, 440000000, 0, N'2025-12-01', NULL, N'Công Ty Đầu Tư Địa Ốc', N'Lý Kim Phi', N'ACTIVE');

-- ================================================================

INSERT INTO phuong_tien (can_ho_id, bien_so, loai, mau_sac, trang_thai, hinh_anh) VALUES
    (1,'29A-123.45', N'Xe máy',  N'Đỏ',       N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (1,'30H-999.88', N'Ô tô',    N'Trắng',    N'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (1,'29C-111.22', N'Xe máy',  N'Đen',      N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (2,'29C-567.89', N'Xe máy',  N'Xanh',     N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (3,'51G-444.22', N'Ô tô',    N'Đen',      N'Từ chối', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (3,'29P-222.11', N'Xe máy',  N'Bạc',      N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (4,'30K-555.66', N'Xe máy',  N'Trắng',    N'Chờ duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (4,'29T-777.33', N'Ô tô',    N'Xám',      N'Đã duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (5,'29D-888.99', N'Xe máy',  N'Xám',      N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (6,'51B-100.10', N'Xe đạp',  N'Xanh lá',  N'Từ chối', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (6,'29A-200.20', N'Xe máy',  N'Vàng',     N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (7,'30G-300.30', N'Xe máy',  N'Đỏ',       N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (8,'29E-400.40', N'Ô tô',    N'Xanh navy',N'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (8,'29K-500.50', N'Xe máy',  N'Trắng',    N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (9,'51H-600.60', N'Xe máy',  N'Đen',      N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (10,'51M-777.77', N'Xe máy', N'Đỏ',       N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (11,'29X-888.88', N'Ô tô',   N'Trắng',    N'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (12,'51N-999.99', N'Xe đạp', N'Xanh',     N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (13,'30M-111.11', N'Xe máy', N'Vàng',     N'Từ chối', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (1,'29R-222.22', N'Ô tô',    N'Đen',      N'Đã duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (2,'51P-333.33', N'Xe máy',  N'Bạc',      N'Chờ duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (3,'29Q-444.44', N'Xe máy',  N'Xám',      N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (4,'51Q-555.55', N'Xe đạp',  N'Xanh lá',  N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (5,'29S-666.66', N'Ô tô',    N'Bạc',      N'Từ chối', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png'),
    (6,'30N-777.77', N'Xe máy',  N'Trắng',    N'Đã duyệt', 'https://cdn2.tuoitre.vn/thumb_w/1200/2021/8/19/hinh-1-16293617253051743233681.jpg'),
    (7,'29T-888.88', N'Ô tô',    N'Đỏ',       N'Chờ duyệt', 'https://hondaotobienhoa.com.vn/wp-content/uploads/2025/09/4.png');

-- ============================================================

INSERT INTO kien_hang (can_ho_id, nguoi_gui, nguoi_nhan, ngay_nhan, trang_thai) VALUES
(1,  N'Shopee',          N'Lê Văn Thắng','2026-04-28', N'Đã nhận'),
(1,  N'Tiki',            N'Nguyễn Thị Mai', '2026-05-02', N'Đã nhận'),
(2,  N'Lazada',          N'Phạm Minh Hoàng','2026-05-05', N'Đã nhận'),
(3,  N'Grab Express',    N'Trần Bích Thủy', '2026-05-08', N'Đã nhận'),
(4,  N'GHTK',            N'Đặng Văn Hùng','2026-05-10', N'Đã nhận'),
(5,  N'J&T Express',     N'Hoàng Lan Anh','2026-05-12', N'Chờ nhận'),
(6,  N'Viettel Post',    N'Vũ Quốc Anh', '2026-05-13', N'Chờ nhận'),
(7,  N'Shopee',          N'Ngô Thanh Vân','2026-05-14', N'Chờ nhận'),
(8,  N'Tiki',            N'Đỗ Minh Tuấn','2026-05-14', N'Chờ nhận'),
(9,  N'DHL',             N'Bùi Thu Hà','2026-05-15', N'Chờ nhận');

-- ============================================================

insert into phan_anh (can_ho_id, tieu_de, noi_dung, ngay_gui, phan_hoi, ngay_phan_hoi, trang_thai, hinh_anh)
values  
(1, N'Hành lang tầng 1 bẩn', N'Hành lang tầng 1 có rác chưa được dọn từ sáng sớm, gây mùi khó chịu.', '2026-04-28 09:00:00', N'Đã điều nhân viên vệ sinh kiểm tra và dọn dẹp lúc 10h30.', '2026-04-28 10:30:00', N'Chờ xử lý', null),
(3, N'Tiếng ồn ban đêm từ tầng trên', N'Căn hộ tầng trên thường xuyên gây tiếng ồn sau 23h, ảnh hưởng giấc ngủ.', '2026-04-20 23:30:00', N'Đã nhắc nhở chủ hộ tầng trên tuân thủ nội quy giờ yên lặng.', '2026-04-21 09:00:00', N'Chờ xử lý', null),
(7, N'Đèn hành lang bị nhấp nháy', N'Đèn LED trước cửa căn hộ A1-03 nhấp nháy liên tục từ tuần trước.', '2026-05-01 19:00:00', '', null, N'Đang xử lý', null),
(2, N'Thang máy hay bị dừng giữa chừng', N'Thang máy Block A số 1 thường xuyên dừng và rung nhẹ giữa các tầng.', '2026-05-03 14:00:00', N'Đã báo đơn vị bảo trì thang máy, dự kiến kiểm tra ngày 06/05.', '2026-05-03 15:30:00', N'Đang xử lý', null),
(5, N'Bồn hoa lối vào xuống cấp', N'Bồn hoa trang trí lối vào Block A bị vỡ và cây chết, trông mất thẩm mỹ.', '2026-05-05 10:00:00', N'Ghi nhận, đã lên kế hoạch cải tạo cảnh quan vào tháng 6.', '2026-05-06 08:00:00', N'Đã xong', null),
(4, N'Bãi xe đầy, không có chỗ gửi', N'Tầng hầm B1 thường xuyên hết chỗ từ 18h-20h, xe phải gửi bên ngoài tốn thêm chi phí.', '2026-05-07 20:00:00', N'Đang nghiên cứu thêm khu vực gửi xe phụ tại tầng hầm B2.', '2026-05-08 09:00:00', N'Đã xong', null),
(8, N'Nước yếu vào giờ cao điểm', N'Áp lực nước rất yếu vào buổi sáng từ 6h-8h, không đủ dùng sinh hoạt.', '2026-05-09 07:30:00', null, null, N'Chờ xử lý', null),
(9, N'Cửa kính sảnh bị hỏng khóa', N'Cửa kính sảnh tầng trệt Block B không đóng được chặt, gây mất an ninh.', '2026-05-10 11:00:00', N'Đã sửa chữa khóa cửa ngay trong ngày.', '2026-05-10 15:00:00', N'Chờ xử lý', null),
(6, N'Hồ bơi nhiều rong rêu', N'Hồ bơi có váng màu xanh và rong rêu ở góc bể, e ngại vệ sinh.', '2026-05-11 16:00:00', N'Đã xong nước và vệ sinh hồ bơi, hoạt động bình thường từ 13/5.', '2026-05-12 08:00:00', N'Chờ xử lý', null),
(10, N'Rò rỉ nước từ tầng trên', N'Tường phòng khách bị rò rỉ nước khiến sơn bị ướt và nứt.', '2026-05-12 10:00:00', N'Đã yêu cầu cư dân tầng trên kiểm tra và khắc phục.', '2026-05-13 09:00:00', N'Đang xử lý', null),
(11, N'Chất lượng nước sinh hoạt', N'Nước có mùi lạ và màu hơi vàng, không rõ là do ống hay bể chứa.', '2026-05-13 08:00:00', N'Đã kiểm tra hệ thống, sẽ vệ sinh bể chứa tuần tới.', '2026-05-14 15:00:00', N'Chờ xử lý', null),
(12, N'Mất nước áp lực', N'Áp lực nước bị mất hoàn toàn trong 2 giờ tối hôm 14/5.', '2026-05-14 18:00:00', null, null, N'Chờ xử lý', null),
(1, N'Thông thoáng gió kém', N'Hệ thống thông khí chung không hoạt động, gây ẩm mốc.', '2026-05-15 12:00:00', N'Đã kiểm tra và làm sạch ống thông khí.', '2026-05-16 10:00:00', N'Đang xử lý', null),
(3, N'Tủ điện chung bị khóa sai', N'Cánh tủ điện chung tầng 3 khóa không khít, gây nguy hiểm.', '2026-05-16 09:00:00', N'Đã sửa lại khóa và kiểm tra an toàn.', '2026-05-16 14:00:00', N'Đang xử lý', null),
(5, N'Sảnh lối ra vào bị bẩn', N'Sảnh chính tòa nhà chưa được lau dọn từ sáng sớm.', '2026-05-17 07:00:00', N'Đã nhắc nhở nhân viên vệ sinh kiểm tra thêm lần.', '2026-05-17 08:00:00', N'Chờ xử lý', null),
(8, N'Bậc cầu thang bị nứt', N'Một vài bậc cầu thang tầng 8 bị nứt, có nguy hiểm trơn trượt.', '2026-05-18 14:00:00', N'Đã báo đơn vị sửa chữa, dự kiến sửa tuần sau.', '2026-05-18 14:00:00', N'Đang xử lý', null),
(1, N'Hư máy lạnh', N'Máy lạnh hư không thể sử dụng Vui lòng sửa gấp', '2026-06-04 19:17:20', null, null, N'Chờ xử lý', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780575441/canho/l9nvkzffb9fzxqvoebv7.png');

-- ============================================================
INSERT INTO dang_ky_khach_tham
(cu_dan_id, ten_khach, cmnd, bien_so_xe, thoi_gian_du_kien, thoi_gian_duyet, trang_thai, ngay_di)
VALUES
(1,  N'Nguyễn Văn Bình', N'001090112233', N'29F1-000.01', '2026-05-02T19:00:00', '2026-05-01T15:00:00', N'Đã duyệt', '2026-05-02'),
(1, N'Trần Thị Cúc',    N'001095223344', NULL,           '2026-05-05T10:00:00', '2026-05-04T14:00:00', N'Đã duyệt', '2026-05-05'),
(2, N'Lê Văn Dũng',     N'001088334455', N'51A-123.00',  '2026-05-06T14:00:00', '2026-05-06T09:00:00', N'Đã duyệt', '2026-05-06'),
(3, N'Phạm Thị Én',     N'001092445566', NULL,           '2026-05-08T09:00:00', NULL,                   N'Chờ duyệt', '2026-05-08'),
(4, N'Đặng Văn Giang',  N'001091556677', N'29H-456.00',  '2026-05-10T15:00:00', '2026-05-10T08:00:00', N'Đã duyệt', '2026-05-10'),
(5, N'Hoàng Thị Hoa',   N'001097667788', NULL,           '2026-05-12T10:00:00', '2026-05-11T16:00:00', N'Đã duyệt', '2026-05-12'),
(6, N'Vũ Văn Kiên',     N'001086778899', N'30D-789.00',  '2026-05-15T18:00:00', NULL,                   N'Chờ duyệt', '2026-05-15'),
(6, N'Ngô Thị Linh',    N'001093889900', NULL,           '2026-05-16T11:00:00', '2026-05-16T09:00:00', N'Đã duyệt', '2026-05-16'),
(7, N'Đỗ Văn Minh',     N'001085990011', N'29A-012.00',  '2026-05-18T09:00:00', '2026-05-17T15:00:00', N'Đã duyệt', '2026-05-18');



-- ============================================================
INSERT INTO dat_dich_vu (cu_dan_id, dich_vu_id, ngay_dat, ngay_ket_thuc, thoi_gian_duyet, trang_thai, ghi_chu) VALUES
(1, 14, '2026-05-05', '2026-06-05', '2026-05-05 10:00:00', N'Đã sử dụng', N'Đăng ký thẻ Hồ bơi tháng 5'),
(3,  4, '2026-05-06', '2026-05-06', '2026-05-07 08:00:00', N'Đã duyệt', N'Vệ sinh nhà buổi sáng'),
(5, 16, '2026-05-08', '2026-05-08', '2026-05-08 14:00:00', N'Đã duyệt', N'Đặt khu BBQ tối thứ 7'),
(1, 10, '2026-05-09', '2026-06-09', '2026-05-09 09:00:00', N'Đã duyệt', N'Gói internet tháng 5'),
(4,  8, '2026-05-10', '2026-05-10', NULL,                  N'Chờ duyệt',   N'Sửa ống nước bị rỉ trong bếp'),
(5, 19, '2026-05-11', '2026-05-11', NULL,                  N'Chờ duyệt',   N'Chuyển tủ lạnh từ phòng kho ra phòng bếp'),
(3, 13, '2026-05-12', '2026-06-12', '2026-05-12 10:00:00', N'Đã duyệt',   N'Đăng ký thẻ Gym tháng 5'),
(2,  4, '2026-05-13', '2026-05-13', NULL,                  N'Chờ duyệt',   N'Dọn nhà buổi chiều'),
(2, 12, '2026-05-14', '2026-06-14', '2026-05-14 11:00:00', N'Đã duyệt',   N'Gói truyền hình cáp tháng 5'),
(8,  15, '2026-05-15', '2026-06-15', '2026-05-15 09:00:00', N'Đã duyệt',   N'Thẻ hồ bơi tháng 5'),
(9,   7, '2026-05-01', '2026-05-01', '2026-05-01 10:00:00', N'Đã sử dụng', N'Bảo trì 2 máy lạnh phòng ngủ và phòng khách'),
(7, 17, '2026-05-16', '2026-05-16', '2026-05-16 14:00:00', N'Đã duyệt',   N'Thuê phòng họp 2 tiếng tổ chức sinh nhật mini'),
(10, 1, '2026-05-17', '2026-06-17', '2026-05-17 10:00:00', N'Đã duyệt',   N'Gửi xe máy tháng 5'),
(11, 2, '2026-05-18', '2026-06-18', '2026-05-18 08:00:00', N'Đã duyệt',   N'Gửi ô tô tháng 5'),
(12, 3, '2026-05-19', '2026-06-19', NULL,                  N'Chờ duyệt',   N'Gửi xe đạp tháng 5'),
(13, 5, '2026-05-20', '2026-05-20', '2026-05-20 09:00:00', N'Đã duyệt',   N'Vệ sinh căn hộ vừa');
-- Tổng: 20 lần đặt dịch vụ | IDs: 1-20

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
(1,'2023-01-15', N'Kiểm tra định kỳ 6 tháng, tra dầu mỡ cơ cấu, thử tải',                  2500000,  N'Công ty Thang máy Otis VN'),
(2,'2023-01-15', N'Kiểm tra định kỳ 6 tháng, thay cáp dây an toàn',                          3200000,  N'Công ty Thang máy Otis VN'),
(3,'2026-09-01', N'Bảo trì định kỳ lần đầu sau 6 tháng vận hành',                            2000000,  N'Công ty Thang máy Kone'),
(4,'2026-12-01', N'Thay toàn bộ bo mạch điều khiển thang máy bị cháy',                       15000000, N'Công ty Thang máy Mitsubishi'),
(5,'2023-01-01', N'Kiểm tra, thay lọc dầu và ắc quy máy phát điện dự phòng 1',               1800000,  N'Công ty Điện lực TNHH Hùng Phát'),
(6,'2023-06-01', N'Bảo dưỡng định kỳ máy phát điện 2, thử tải 100%',                         2200000,  N'Công ty Điện lực TNHH Hùng Phát'),
(7,'2026-02-15', N'Thay mới bơm nước tăng áp tầng cao, kiểm tra đường ống áp lực',           8500000,  N'Công ty CP Nước sạch Hà Nội'),
(8,'2023-02-01', N'Vệ sinh màng lọc, kiểm tra motor bơm',                                     1200000,  N'Đội kỹ thuật nội bộ'),
(9,'2023-05-10', N'Nâng cấp firmware hệ thống camera Block A, thay 3 camera hỏng',            5000000,  N'Công ty An ninh Việt Security'),
(10, '2023-05-10', N'Thay toàn bộ 8 camera hỏng Block B, nâng lên độ phân giải 4K',            12000000, N'Công ty An ninh Việt Security'),
(11, '2026-01-01', N'Kiểm tra và vệ sinh camera hầm B1, căn chỉnh góc quan sát',               800000,   N'Đội kỹ thuật nội bộ');
