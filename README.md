# 🏆 GIÁO TRÌNH ĐÀO TẠO MICROSERVICES THỰC CHIẾN
## DỰ ÁN: WORLD CUP 2026 TICKET PLATFORM 🎫
> **Mục tiêu đào tạo:** Huấn luyện lập trình viên từ nền tảng Spring Boot cơ bản tiến lên làm chủ kiến trúc Microservices phân tán chuẩn doanh nghiệp, tự tay xây dựng 8 microservices, xử lý bài toán chống bán quá vé (**Anti-Oversell với Optimistic Locking**) và điều phối giao dịch phân tán (**SAGA Orchestrator**).

---

## 📌 BẢNG MỤC LỤC GIÁO TRÌNH TOÀN KHÓA (14 NGÀY)

Chương trình đào tạo được chia nhỏ theo từng ngày độc lập. Mỗi ngày có tài liệu hướng dẫn chi tiết, bài thực hành Lab (Step-by-step), tiêu chí nghiệm thu (DoD) và bộ câu hỏi phản biện:

| Ngày | Trạng Thái | Chủ Đề Trọng Tâm | Dịch Vụ & Hạ Tầng | Tài Liệu Chi Tiết |
| :---: | :---: | :--- | :--- | :---: |
| **DAY 1** | 🟢 **ACTIVE** | **Khung Dự Án Đa Module & 4 Hợp Đồng Dùng Chung** | Parent POM, `common-dto`, `common-exception`, `common-security` | [📖 **Xem Chi Tiết DAY 1**](README_DAY1.md) |
| **DAY 2** | 🟢 **ACTIVE** | **Service Registry (Eureka) & Cụm Database Docker** | `discovery-service`, 5x Postgres, Redis Cache | [📖 **Xem Chi Tiết DAY 2**](README_DAY2.md) |
| **DAY 3** | 🔒 *Chờ duyệt* | API Gateway Đơn Điểm & Trace ID Correlation | `api-gateway`, Spring Cloud Gateway | *(Mở sau khi pass Day 2)* |
| **DAY 4** | 🔒 *Chờ duyệt* | Quản Lý Người Dùng & Asymmetric JWT (RSA-256) | `user-service`, Flyway migration | *(Mở sau khi pass Day 3)* |
| **DAY 5** | 🔒 *Chờ duyệt* | Bảo Mật Tập Trung Gateway & Token Blacklist | `api-gateway`, Redis Blacklist | *(Mở sau khi pass Day 4)* |
| **DAY 6** | 🔒 *Chờ duyệt* | Quản Lý Trận Đấu, Lọc Đa Tiêu Chí & Redis Cache | `match-service`, Spring Data JPA Specs | *(Mở sau khi pass Day 5)* |
| **DAY 7** | 🔒 *Chờ duyệt* | Tồn Kho Vé & Thiết Kế Bất Biến Toàn Vẹn | `ticket-service` (Part 1), Flyway | *(Mở sau khi pass Day 6)* |
| **DAY 8** | 🔒 *Chờ duyệt* | ⭐ **Chống Oversell: Optimistic Lock & Concurrency Test** | `ticket-service` (Part 2), `@Version` | *(Mở sau khi pass Day 7)* |
| **DAY 9** | 🔒 *Chờ duyệt* | SAGA Orchestrator: Khởi Tạo Đơn Hàng & Giữ Vé | `order-service`, `payment-service` | *(Mở sau khi pass Day 8)* |
| **DAY 10** | 🔒 *Chờ duyệt* | ⭐ **Hoàn Thiện SAGA & Cơ Chế Bù Trừ (Compensate)** | `order-service` SAGA State Machine | *(Mở sau khi pass Day 9)* |
| **DAY 11** | 🔒 *Chờ duyệt* | Event-Driven với Apache Kafka & Stripe Payment | Apache Kafka KRaft, Stripe Webhook | *(Mở sau khi pass Day 10)* |
| **DAY 12** | 🔒 *Chờ duyệt* | Xuất Vé PDF, Mã QR & MinIO Object Storage | `notification-service`, MinIO, Mailtrap | *(Mở sau khi pass Day 11)* |
| **DAY 13** | 🔒 *Chờ duyệt* | Hàng Đợi Ảo (Virtual Queue) & Giám Sát Phân Tán | Redis Virtual Queue, Zipkin, Prometheus | *(Mở sau khi pass Day 12)* |
| **DAY 14** | 🔒 *Chờ duyệt* | Đóng Gói Full Stack Docker Compose & Live Demo | Docker Compose 16 Containers, Scripts | *(Mở sau khi pass Day 13)* |

---

## 📜 QUY TRÌNH HỌC TẬP & NỘP BÀI (WORKFLOW)

1. **Nhận bài hàng ngày:** Đọc kỹ lý thuyết, mục tiêu và hướng dẫn thực hành của ngày hiện tại trong file `README_DAY*.md`.
2. **Thực hiện code:** Tự tay gõ mã nguồn trên nhánh làm việc (ví dụ: `feature/day-1-common-modules` hoặc `feature/day-2-eureka-docker`), tuyệt đối không copy-paste mà không hiểu.
3. **Tự kiểm thử (Self-Test):** Chạy lệnh build và test theo hướng dẫn trong bài lab.
4. **Tạo Commit & Push:** Đẩy code lên repository với thông điệp chuẩn Conventional Commits (ví dụ: `feat(day-1): setup parent pom and common modules`).
5. **Code Review & Nghiệm thu:** Mentor sẽ so sánh trực tiếp code của bạn với tiêu chuẩn doanh nghiệp của dự án mẫu, đưa ra nhận xét:
   * 🟢 **Đạt chuẩn (Passed):** Được Tech Lead duyệt để chuyển sang bài học ngày tiếp theo!
   * 🟡 **Cần sửa đổi (Needs Revision):** Cần fix theo góp ý của Mentor trước khi chuyển ngày.

---

## 🧭 HƯỚNG DẪN BẮT ĐẦU NGAY BÂY GIỜ

* 👉 **Bắt đầu Ngày 1:** [**Nhấn vào đây để xem chi tiết bài học DAY 1**](README_DAY1.md)
* 👉 **Bắt đầu Ngày 2:** [**Nhấn vào đây để xem chi tiết bài học DAY 2**](README_DAY2.md)
