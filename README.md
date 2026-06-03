# 🏢 QL_ChungCu - Hệ Thống Quản Lý Chung Cư

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Microsoft%20SQL%20Server-CC2927?style=for-the-badge&logo=microsoft%20sql%20server&logoColor=white" alt="Microsoft SQL Server" />
  <img src="https://img.shields.io/badge/Maven-876F27?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" />
  </p>
  <p align="center">
  <img src="https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white" alt="Bootstrap" />
  <img src="https://img.shields.io/badge/HTML-563D7C.svg?style=for-the-badge&logo=html5&logoColor=white" alt="HTML" />
  <img src="https://img.shields.io/badge/CSS-563D7C.svg?style=for-the-badge&logo=css3&logoColor=white" alt="CSS" />
  <img src="https://img.shields.io/badge/JavaScript-563D7C?style=for-the-badge&logo=javascript&logoColor=white" alt="JavaScript" />
</p>
<p align="center"   >
  <img src="https://img.shields.io/badge/Cloudinary-F37653?style=for-the-badge&logo=cloudinary&logoColor=white" alt="Cloudinary" />
</p>

Dự án **QL_ChungCu** là phần mềm quản lý chung cư toàn diện, được phát triển nhằm mục đích số hóa và tối ưu hóa quy trình quản lý cũng như tăng cường tính tiện lợi trong tương tác giữa Ban quản lý tòa nhà và Cư dân.

## 🌟 Tính năng nổi bật

## 🌟 Tính năng nổi bật

### 👨‍💼 Dành cho Ban Quản Lý (Admin)
* **Quản lý Căn hộ & Cư dân:** Theo dõi trạng thái căn hộ, cập nhật thông tin chủ hộ, quản lý nhân khẩu và thông tin liên hệ.
* **Quản lý Tài chính:** Lập hóa đơn định kỳ, theo dõi trạng thái thanh toán chi tiết của từng hộ, quản lý lịch sử thanh toán.
* **Quản lý Dịch vụ:** Cung cấp danh sách dịch vụ và phê duyệt các yêu cầu sử dụng, đặt trước dịch vụ nội khu từ cư dân.
* **Quản lý Tài sản & Bảo trì:** Ghi nhận tài sản tòa nhà, theo dõi tình trạng vật chất và lập lịch sử bảo trì.
* **Tương tác & Truyền thông:** Đăng tải thông báo, tạo các bảng khảo sát ý kiến, tiếp nhận và xử lý khiếu nại/phản ánh.
* **Quản lý Tiện ích Khác:** Quản lý bưu kiện/kiện hàng, kiểm soát ra vào của khách thăm, đăng ký chỗ đậu xe/phương tiện.

### 🧑‍🤝‍🧑 Dành cho Cư Dân (Resident)
* **Bảng tin & Thông báo:** Cập nhật tin tức, chính sách, hoặc các thông báo sự cố từ ban quản lý nhanh chóng.
* **Thanh toán tiện lợi:** Xem chi tiết hóa đơn (tiền điện, nước, phí quản lý...) hàng tháng và thanh toán nhanh qua mã QR.
* **Tiện ích Nội khu:** Đăng ký sử dụng dịch vụ (gym, hồ bơi, BBQ...), đăng ký thông tin khách đến thăm, đăng ký phương tiện đi lại.
* **Giao tiếp & Phản hồi:** Gửi khiếu nại, phản ánh trực tiếp kèm hình ảnh đến ban quản lý, tham gia các bài đánh giá, khảo sát.
* **Quản lý Thông tin:** Kiểm tra thông tin hợp đồng và nhận thông báo nhắc nhở khi có bưu kiện gửi đến.

---

## 🛠️ Công nghệ sử dụng

* **Backend:** Java (JDK 17+), Spring Boot, Spring Data JPA
* **Frontend:** HTML, CSS, JavaScript, Thymeleaf Bootstrap
* **Cơ sở dữ liệu (Database):** Microsoft SQL Server
* **Lưu trữ đám mây:** Cloudinary (Lưu trữ và quản lý hình ảnh)
* **Build Tool:** Maven

---

## 🚀 Hướng dẫn cài đặt và chạy dự án

### 1. Yêu cầu môi trường
* JDK 17 trở lên
* SQL Server Management Studio (SSMS) hoặc DataGrip
* Maven
* Tài khoản Cloudinary (để cấu hình API lưu ảnh)

### 2. Thiết lập Cơ sở dữ liệu
1. Mở SQL Server Management Studio (SSMS).
2. Mở và chạy script `DATABASE.sql` để khởi tạo database `QL_ChungCu` cùng cấu trúc các bảng.
3. Chạy file script `IMPORT_DATA.sql` để nạp dữ liệu mẫu ban đầu (nếu cần).

### 3. Cấu hình ứng dụng
Mở file `src/main/resources/application.properties` và điều chỉnh các thông số phù hợp với môi trường cục bộ của bạn:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=QL_ChungCu;encrypt=true;trustServerCertificate=true
spring.datasource.username=YOUR_SQL_USERNAME
spring.datasource.password=YOUR_SQL_PASSWORD

# Thông tin API Cloudinary
cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET
```

### 4. Khởi chạy
Bạn có thể mở toàn bộ mã nguồn bằng IDE như IntelliJ IDEA, Eclipse, hoặc VS Code.

Chạy trực tiếp file `SyncApplication.java` từ IDE của bạn.

Hoặc sử dụng Maven thông qua Terminal:
```bash
mvn spring-boot:run
```

Hệ thống sẽ chạy ở port mặc định (thường là `http://localhost:8080`).

---

## 📁 Cấu trúc thư mục (Packages)

Dự án áp dụng mô hình kiến trúc chuẩn MVC trong Spring Boot:
* `controller/`: Xử lý các request HTTP, phân luồng chức năng cho Admin (`AdminController`, `AdminHoaDonController`...) và Cư dân (`CuDanController`, `CuDanHoaDonController`...).
* `service/`: Nơi chứa các Interface và Implementations xử lý logic nghiệp vụ, cũng như cấu hình gọi API bên ngoài (ví dụ: `CloudinaryService`).
* `repository/`: Các Interface kế thừa từ `JpaRepository` để thao tác trực tiếp với cơ sở dữ liệu.
* `entity/`: Định nghĩa các model map với cấu trúc bảng dưới SQL (`CanHo`, `CuDan`, `HoaDon`, `PhuongTien`...).
* `config/`: Chứa các setting cho Web, Locale và kết nối Cloudinary.
* `interceptor/`: Xử lý các logic chặn request, xác thực, phân quyền người dùng (`AuthInterceptor`).
* `resources/templates/`: Chứa toàn bộ giao diện Thymeleaf (HTML), được phân tách rõ ràng thành thư mục `/admin`, `/cudan`, và `/fragments` (các thành phần dùng chung).

---

## 👥 Nhóm phát triển

Đồ án được thực hiện bởi:
* **Nguyễn Ngọc Tiên** (Leader)
* **Ngô Thanh Tiến** (Member)
* **Hà Tiên Tiến** (Member)
---

## 📄 Giấy phép (License)

Mã nguồn này tuân thủ các quy định phân phối được nêu trong file `LICENSE`.
