USE QL_ChungCu;

-- 1. NGUOI_DUNG (Admin, Nhan vien, Cu dan)
-- Vai tro: 1 - Admin, 2 - Quản lý, 3 - Cư dân, 4 - Lễ tân, 5 - Bảo vệ
INSERT INTO nguoi_dung (email, ho_ten, mat_khau_ma_hoa, so_dien_thoai, ten_dang_nhap, vai_tro) VALUES
('admin@chungcu.com', N'Nguyễn Quản Trị', 'hashed_pass_1', '0901234567', 'admins', 1),
('nhanvien1@chungcu.com', N'Trần Thị Nhân Viên', 'hashed_pass_2', '0912345678', 'nv01', 2),
('nhanvien2@chungcu.com', N'Lê Văn Lễ Tân', 'hashed_pass_2b', '0912345679', 'nv02', 4),
('cu_dan_1@gmail.com', N'Lê Văn Thắng', 'hashed_pass_3', '0987654321', 'thang_lv', 3),
('thang_vo@gmail.com', N'Nguyễn Thị Mai', 'hashed_pass_3b', '0987654322', 'mai_nt', 3),
('cu_dan_2@gmail.com', N'Phạm Minh Hoàng', 'hashed_pass_4', '0977112233', 'hoang_pm', 3),
('cu_dan_3@gmail.com', N'Trần Bích Thủy', 'hashed_pass_5', '0966445566', 'thuy_tb', 3),
('cu_dan_4@gmail.com', N'Đặng Văn Hùng', 'hashed_pass_6', '0955889900', 'hung_dv', 3),
('cu_dan_5@gmail.com', N'Hoàng Lan Anh', 'hashed_pass_7', '0944773322', 'anh_hl', 3),
('cu_dan_6@gmail.com', N'Vũ Quốc Anh', 'hashed_pass_8', '0933221100', 'anh_vq', 3),
('cu_dan_7@gmail.com', N'Ngô Thanh Vân', 'hashed_pass_9', '0922334455', 'van_nt', 3),
('baove@chungcu.com', N'Lê Văn Bảo Vệ', 'hashed_pass_2c', '0912345680', 'nv03', 5);

-- 2. CAN_HO
INSERT INTO can_ho (ma_can_ho, dien_tich, tang, loai, trang_thai, hinh_anh) VALUES
('A1-01', 75.5, 1, N'Căn hộ tiêu chuẩn', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('A1-02', 45.0, 1, N'Studio', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('A2-05', 90.0, 2, N'Duplex', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('B3-10', 60.0, 3, N'Căn hộ tiêu chuẩn', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('C5-02', 120.0, 5, N'Penthouse', N'Trống', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('D1-01', 75.5, 1, N'Căn hộ tiêu chuẩn', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('A1-03', 75.5, 1, N'Căn hộ tiêu chuẩn', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('B2-04', 65.0, 2, N'Căn hộ tiêu chuẩn', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg'),
('C3-07', 80.0, 3, N'Duplex', N'Đã có chủ', 'https://res.cloudinary.com/dtr8hrxlt/image/upload/v1780401430/canho/pkg2fhdqqqcy6oqn7ebm.jpg');


INSERT INTO cu_dan (id, cccd, moi_quan_he, ngay_sinh, trang_thai, ma_can_ho) VALUES
(4, '001095001234', N'Chủ hộ', '1985-05-15', N'Đang Ở', 1),
(5, '001095001235', N'Vợ/Chồng', '1988-08-20', N'Đang Ở', 1),
(6, '001090005678', N'Chủ hộ', '1990-10-20', N'Chưa Đến', 2),
(7, '001088009988', N'Chủ hộ', '1988-03-12', N'Đang Ở', 3),
(8, '001092001122', N'Chủ hộ', '1992-07-08', N'Đang Ở', 4),
(9, '001098003344', N'Con cái', '1998-12-25', N'Chưa Đến', 6),
(10, '001093004455', N'Chủ hộ', '1993-02-14', N'Đang Ở', 7),
(11, '001094006677', N'Chủ hộ', '1994-06-30', N'Đang Ở', 8);

-- 4. DICH_VU
INSERT INTO dich_vu (ten, don_gia, don_vi_tinh, loai, mo_ta) VALUES
(N'Gửi xe máy', 150000, N'Chiếc/Tháng', N'Gửi xe', N'Phí trông giữ xe máy hàng tháng'),
(N'Gửi ô tô', 1200000, N'Chiếc/Tháng', N'Gửi xe', N'Phí trông giữ ô tô hàng tháng'),
(N'Vệ sinh căn hộ', 500000, N'Lần', N'Dọn dẹp', N'Dịch vụ dọn dẹp vệ sinh theo yêu cầu'),
(N'Bảo trì máy lạnh', 200000, N'Máy', N'Sửa chữa', N'Vệ sinh và nạp gas máy lạnh'),
(N'Phí quản lý', 12000, N'm2/Tháng', N'Cố định', N'Phí quản lý vận hành chung cư'),
(N'Internet tốc độ cao', 250000, N'Gói/Tháng', N'Viễn thông', N'Gói mạng 100Mbps'),
(N'Sử dụng Gym/Pool', 300000, N'Người/Tháng', N'Tiện ích', N'Thẻ thành viên khu tiện ích');

-- 5. TAI_SAN
INSERT INTO tai_san (ma_tai_san, ten_tai_san, vi_tri, tinh_trang, ngay_mua, chu_ky_bao_tri, ngay_bao_tri_tiep_theo) VALUES
('TS-TL01', N'Thang máy Block A', N'Hành lang Block A', N'Hoạt động tốt', '2023-01-01', 6, '2023-07-01'),
('TS-TL02', N'Thang máy Block B', N'Hành lang Block B', N'Hoạt động tốt', '2023-01-01', 6, '2023-07-01'),
('TS-MF01', N'Máy phát điện dự phòng', N'Tầng hầm B1', N'Hoạt động tốt', '2023-01-01', 12, '2024-01-01'),
('TS-CC01', N'Hệ thống Camera PCCC', N'Toàn bộ tòa nhà', N'Đang hỏng', '2023-05-10', 3, '2023-08-10'),
('TS-BP01', N'Bơm nước sinh hoạt', N'Phòng kỹ thuật', N'Hoạt động tốt', '2024-02-15', 12, '2025-02-15');

-- 6. KHAO_SAT
INSERT INTO khao_sat (tieu_de, mo_ta, thoi_gian_bat_dau, thoi_gian_ket_thuc) VALUES
(N'Khảo sát chất lượng dịch vụ bảo vệ', N'Chúng tôi muốn biết ý kiến của bạn về đội ngũ bảo vệ tòa nhà.', '2024-04-01 08:00:00', '2024-05-01 17:00:00'),
(N'Lấy ý kiến tổ chức Trung Thu cho bé', N'Góp ý về thời gian và địa điểm tổ chức.', '2024-08-01 08:00:00', '2024-08-15 17:00:00'),
(N'Khảo sát nhu cầu lắp thêm trạm sạc xe điện', N'Thu thập ý kiến về việc lắp đặt trạm sạc tại hầm B2.', '2024-05-10 08:00:00', '2024-06-10 17:00:00');

-- 7. THONG_BAO
insert into dbo.thong_bao 
(id, tieu_de, noi_dung, loai, ngay_dang, doi_tuong_gui, gia_tri_doi_tuong) values

(7, N'Thông báo bảo trì hệ thống nước',
 N'<p>Ban quản lý thông báo sẽ tiến hành bảo trì hệ thống cấp nước toàn tòa nhà từ 08:00 đến 12:00 ngày 05/06/2026. Kính đề nghị cư dân chủ động dự trữ nước sinh hoạt trong thời gian trên.</p>',
 1, N'2026-06-02T22:01:08.17', N'ALL', N''),
(8, N'Nhắc nhở hoàn thành phí quản lý tháng 6',
 N'<p>Quý cư dân căn hộ A1-01 vui lòng hoàn thành phí quản lý tháng 06/2026 trước ngày 10/06/2026 để tránh phát sinh nhắc nhở từ hệ thống.</p>',
 1, N'2026-06-02T22:01:30.543', N'HO_GIA_DINH', N'A1-01'),
(9, N'Kiểm tra đồng hồ điện định kỳ',
 N'<p>Ban quản lý sẽ thực hiện kiểm tra đồng hồ điện tại các căn hộ A1-01 và A1-02 vào ngày 06/06/2026. Vui lòng bố trí thời gian để hỗ trợ nhân viên kỹ thuật.</p>',
 1, N'2026-06-02T22:01:54.553', N'NHIEU_HO', N'A1-01, A1-02'),
(10, N'Vệ sinh hành lang tầng 1',
 N'<p>Công tác vệ sinh và bảo dưỡng hành lang tầng 1 sẽ được thực hiện từ 09:00 đến 11:00 ngày 04/06/2026. Mong cư dân hạn chế để vật dụng cá nhân tại khu vực hành lang trong thời gian này.</p>',
 1, N'2026-06-02T22:02:15.473', N'TANG', N'1'),
(11, N'Hướng dẫn sử dụng thẻ cư dân',
 N'<p>Cư dân vui lòng mang theo thẻ cư dân khi sử dụng các tiện ích chung như bãi đỗ xe, phòng gym và hồ bơi. Thẻ cần được bảo quản cẩn thận để tránh mất mát.</p>',
 2, N'2026-06-02T22:02:34.19', N'ALL', N''),
(12, N'Hướng dẫn đăng ký tạm trú',
 N'<p>Cư dân căn hộ A1-01 có thể thực hiện đăng ký tạm trú trực tuyến hoặc tại văn phòng Ban quản lý. Vui lòng chuẩn bị đầy đủ giấy tờ tùy thân theo quy định.</p>',
 2, N'2026-06-02T22:02:57.377', N'HO_GIA_DINH', N'A1-01'),
(13, N'Hướng dẫn phân loại rác sinh hoạt',
 N'<p>Các căn hộ A1-01 và A2-05 vui lòng thực hiện phân loại rác hữu cơ, rác tái chế và rác thải khác theo hướng dẫn của Ban quản lý nhằm góp phần xây dựng môi trường sống xanh - sạch - đẹp.</p>',
 2, N'2026-06-02T22:03:25.593', N'NHIEU_HO', N'A1-01, A2-05');

-- 8. CHI_SO_HANG_THANG (Lịch sử 2 tháng cho căn A1-01 và các căn khác)
INSERT INTO chi_so_hang_thang (can_ho_id, dien_tieu_thu, nuoc_tieu_thu, ngay_ghi_nhan) VALUES
(1, 150.5, 12.0, '2024-03-31'),
(1, 295.0, 23.5, '2024-04-30'), -- Căn 1 tiêu thụ thêm (295-150.5) = 144.5 số điện
(2, 80.2, 5.0, '2024-03-31'),
(2, 165.5, 11.2, '2024-04-30'),
(3, 210.0, 18.2, '2024-04-30'),
(4, 120.5, 9.0, '2024-04-30'),
(6, 300.0, 25.0, '2024-04-30'),
(7, 180.0, 15.0, '2024-04-30');

-- 9. HOP_DONG
INSERT INTO hop_dong (can_ho_id, cu_dan_id, loai_hop_dong, gia_tri_hop_dong, tien_coc, tien_thue, ngay_bat_dau, ngay_ket_thuc, ben_cho_thue, ben_thue, trang_thai) VALUES
(1, 4, N'Mua bán', 2500000000, 500000000, 0, '2023-01-15', '2073-01-15', N'Chủ đầu tư ABC', N'Lê Văn Thắng', N'Hiệu lực'),
(2, 6, N'Thuê', 8000000, 16000000, 8000000, '2024-01-01', '2025-01-01', N'Công ty Quản lý X', N'Phạm Minh Hoàng', N'Hiệu lực'),
(3, 7, N'Mua bán', 3200000000, 1000000000, 0, '2023-05-20', '2073-05-20', N'Chủ đầu tư ABC', N'Trần Bích Thủy', N'Hiệu lực');

-- 10. PHUONG_TIEN
INSERT INTO phuong_tien (can_ho_id, bien_so, loai, mau_sac, trang_thai) VALUES
(1, '29A-123.45', N'Xe máy', N'Đỏ', N'Đã đăng ký'),
(1, '30H-999.88', N'Ô tô', N'Trắng', N'Đã đăng ký'),
(1, '29C-111.22', N'Xe máy', N'Đen', N'Đã đăng ký'),
(2, '29C-567.89', N'Xe máy', N'Xanh', N'Đã đăng ký'),
(3, '51G-444.22', N'Ô tô', N'Đen', N'Đã đăng ký'),
(4, '30K-555.66', N'Xe máy', N'Trắng', N'Đã đăng ký'),
(6, '29D-888.99', N'Xe máy', N'Xám', N'Đã đăng ký');

-- 11. HOA_DON (Liên kết với các dịch vụ và chỉ số)
INSERT INTO hoa_don (can_ho_id, tong_tien, ngay_phat_hanh, ngay_den_han, trang_thai_thanh_toan) VALUES
(1, 1500000, '2024-04-01', '2024-04-10', N'Đã thanh toán'),
(1, 1650000, '2024-05-01', '2024-05-10', N'Chưa thanh toán'),
(2, 900000, '2024-05-01', '2024-05-10', N'Chưa thanh toán'),
(3, 2100000, '2024-05-01', '2024-05-10', N'Chưa thanh toán'),
(4, 1200000, '2024-05-01', '2024-05-10', N'Đã thanh toán');

-- 12. THANH_TOAN / LICH_SU_THANH_TOAN
-- Thanh toán hóa đơn 1 (Tháng 4)
INSERT INTO thanh_toan (hoa_don_id, so_tien, ngay_thanh_toan, phuong_thuc) VALUES
(1, 1500000, '2024-04-05', N'Chuyển khoản');

INSERT INTO lich_su_thanh_toan (hoa_don_id, can_ho_id, ma_hoa_don, so_tien_thanh_toan, ngay_thanh_toan, phuong_thuc_thanh_toan, nguoi_thanh_toan, trang_thai_sua, ghi_chu) VALUES
(1, 1, 'HD-202404-A101', 1500000, '2024-04-05 10:30:00', N'Chuyển khoản', N'Lê Văn Thắng', N'Gốc', N'Thanh toán phí tháng 3');

-- Thanh toán hóa đơn 5 (Tháng 5 cho căn 4)
INSERT INTO thanh_toan (hoa_don_id, so_tien, ngay_thanh_toan, phuong_thuc) VALUES
(5, 1200000, '2024-05-02', N'Tiền mặt');

INSERT INTO lich_su_thanh_toan (hoa_don_id, can_ho_id, ma_hoa_don, so_tien_thanh_toan, ngay_thanh_toan, phuong_thuc_thanh_toan, nguoi_thanh_toan, trang_thai_sua, ghi_chu) VALUES
(5, 4, 'HD-202405-B310', 1200000, '2024-05-02 09:15:00', N'Tiền mặt', N'Đặng Văn Hùng', N'Gốc', N'Đã thu tiền tại quầy');

-- 13. PHAN_ANH
INSERT INTO phan_anh (can_ho_id, tieu_de, noi_dung, ngay_gui, phan_hoi, ngay_phan_hoi, trang_thai, hinh_anh) VALUES
(1, N'Hành lang bẩn', N'Hành lang tầng 1 có rác chưa dọn từ sáng.', '2024-05-01 10:00:00', N'Đã cử nhân viên vệ sinh kiểm tra và dọn dẹp lúc 11h.', '2024-05-01 11:30:00', N'Đã xử lý', 'feedback1.jpg'),
(3, N'Tiếng ồn ban đêm', N'Căn hộ tầng trên thường xuyên gây tiếng ồn sau 11h đêm.', '2024-04-20 23:30:00', N'Đã nhắc nhở chủ hộ tầng trên tuân thủ nội quy.', '2024-04-21 09:00:00', N'Đã xử lý', NULL),
(7, N'Hỏng đèn hành lang', N'Đèn trước cửa căn hộ A1-03 bị nhấp nháy.', '2024-05-14 19:00:00', NULL, NULL, N'Đang chờ', 'hvd123.jpg');

-- 14. KIEN_HANG (Tình trạng giao nhận hàng hóa)
INSERT INTO kien_hang (can_ho_id, nguoi_gui, nguoi_nhan, ngay_nhan, trang_thai) VALUES
(1, N'Shopee', N'Lê Văn Thắng', '2024-05-01', N'Đã nhận'),
(1, N'Tiki', N'Nguyễn Thị Mai', '2024-05-14', N'Chờ nhận'),
(2, N'Lazada', N'Phạm Minh Hoàng', '2024-05-10', N'Đã nhận'),
(4, N'Giao hàng nhanh', N'Đặng Văn Hùng', '2024-05-15', N'Chờ nhận');

-- 15. DANG_KY_KHACH_THAM
INSERT INTO dang_ky_khach_tham (cu_dan_id, ten_khach, cmnd, bien_so_xe, thoi_gian_du_kien, thoi_gian_duyet, trang_thai) VALUES
(4, N'Nguyễn Văn B', '123456789', '29F1-000.01', '2024-05-02 19:00:00', '2024-05-01 15:00:00', N'Đã duyệt'),
(6, N'Trần Thị C', '987654321', NULL, '2024-05-20 10:00:00', NULL, N'Đang chờ');

-- 16. DAT_DICH_VU
INSERT INTO dat_dich_vu (cu_dan_id, dich_vu_id, ngay_dat, thoi_gian_duyet, trang_thai, ghi_chu) VALUES
(4, 3, '2024-05-05', '2024-05-01 14:00:00', N'Đã duyệt', N'Dọn vào sáng chủ nhật'),
(7, 4, '2024-05-12', '2024-05-13 08:30:00', N'Đã duyệt', N'Máy lạnh phòng khách kêu to'),
(9, 3, '2024-05-16', NULL, N'Đang chờ', N'Cần dọn gấp buổi chiều');

-- 17. LUA_CHON_KHAO_SAT
INSERT INTO lua_chon_khao_sat (khao_sat_id, noi_dung_lua_chon, so_luot_binh_chon) VALUES
(1, N'Rất hài lòng', 12),
(1, N'Hài lòng', 28),
(1, N'Không hài lòng', 3),
(2, N'Ngày 14/09', 18),
(2, N'Ngày 15/09', 22),
(3, N'Đồng ý lắp đặt', 45),
(3, N'Không đồng ý', 5);

-- 18. LICH_SU_VOTE
INSERT INTO lich_su_vote (cu_dan_id, khao_sat_id, lua_chon_id, thoi_gian_vote) VALUES
(4, 1, 1, '2024-04-10 09:00:00'),
(6, 1, 2, '2024-04-11 10:00:00'),
(4, 3, 6, '2024-05-11 14:00:00'),
(7, 3, 6, '2024-05-12 08:00:00');

-- 19. LICH_SU_BAO_TRI
insert into dbo.tai_san (id, ma_tai_san, ten_tai_san, vi_tri, tinh_trang, ngay_mua, chu_ky_bao_tri, ngay_bao_tri_tiep_theo) values
(1, N'TS-TL01', N'Thang máy Block A', N'Hành lang Block A', N'Hoạt động tốt', N'2026-06-28', 6, N'2026-12-28'),
(2, N'TS-TL02', N'Thang máy Block B', N'Hành lang Block B', N'Hoạt động tốt', N'2026-06-13', 6, N'2026-12-13'),
(3, N'TS-MF01', N'Máy phát điện dự phòng', N'Tầng hầm B1', N'Hoạt động tốt', N'2026-07-01', 12, N'2027-07-01'),
(4, N'TS-CC01', N'Hệ thống Camera PCCC', N'Toàn bộ tòa nhà', N'Đang hỏng', N'2026-06-20', 3, N'2026-09-20'),
(5, N'TS-BP01', N'Bơm nước sinh hoạt', N'Phòng kỹ thuật', N'Chờ thanh lý', N'2026-06-27', 12, N'2027-06-27');