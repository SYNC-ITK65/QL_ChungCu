# 🏢 Dự Án Quản Lý Chung Cư - Sync ITK65

[![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot%203.x-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/Database-MySQL%208.0-blue)](https://www.mysql.com/)
[![Java](https://img.shields.io/badge/Language-Java%2017-orange)](https://www.oracle.com/java/)

Chào mừng các thành viên nhóm **Sync ITK65**! Đây là tài liệu hướng dẫn cấu trúc dự án và phân công nhiệm vụ cho giai đoạn xây dựng hệ thống quản lý chung cư bằng **Spring Boot**.

---

## 📂 1. Cấu Trúc Thư Mục (Project Structure)

Dự án được tổ chức theo mô hình **Layered Architecture** (Kiến trúc phân lớp) để đảm bảo tính dễ bảo trì và mở rộng. Mỗi lớp đóng một vai trò riêng biệt:

```text
src/main/java/com/sync/itk65
 ├── 🛠️ config          # Cấu hình bảo mật, phân quyền (Security, CORS).
 ├── 🎮 controller      # Tiếp nhận Request từ web (URL) và trả về dữ liệu/giao diện.
 ├── 🧬 entity          # (Model) Chứa các class tương đương với các bảng trong MySQL.
 ├── 🗄️ repository      # Chứa các Interface để giao tiếp với Database (CRUD).
 ├── 🧠 service         # Chứa các logic nghiệp vụ (Tính tiền, kiểm tra mật khẩu...).
 └── 🚀 SyncApplication.java  # File chạy chính của ứng dụng.