# 🚀 DAY 2: SERVICE REGISTRY (NETFLIX EUREKA SERVER) & HẠ TẦNG DOCKER DATABASES (POSTGRESQL + REDIS)
> **Dự án:** World Cup 2026 Ticket Platform 🎫  
> **Thời lượng khuyến nghị:** 1 ngày làm việc (6 - 8 tiếng)  
> **Độ khó:** ⭐⭐☆☆☆ (Hạ tầng phân tán)

---

## 🎯 1. Mục Tiêu Đầu Ra (Definition of Done - DoD Day 2)

Trước khi kết thúc ngày hôm nay, học viên phải hoàn thành trọn vẹn các tiêu chí sau:
- [ ] **Hiểu nguyên lý Service Discovery:** Nắm rõ cơ chế Heartbeat, Lease Renewal, Eviction Timer và Self-Preservation của Eureka.
- [ ] **Nắm vững triết lý Database-per-Service:** Hiểu tại sao các microservices tuyệt đối không được dùng chung database hoặc JOIN bảng chéo.
- [ ] **Dựng hạ tầng Docker:** Viết xong tệp `docker-compose-infra.yml` chạy 5 container PostgreSQL độc lập (ports 5432 - 5436) và 1 container Redis Cache (port 6379).
- [ ] **Tạo module `discovery-service`:** Khởi tạo Spring Boot Eureka Server trên cổng `8761`.
- [ ] **Cập nhật Parent POM:** Thêm `discovery-service` vào danh sách `<modules>` trong `pom.xml` gốc.
- [ ] **Nghiệm thu thực tế:**
  1. Tất cả 6 containers (5 Postgres + 1 Redis) đều ở trạng thái `healthy` hoặc `running`.
  2. Truy cập Eureka Dashboard tại `http://localhost:8761` hiển thị trạng thái `UP`.
- [ ] **Vấn đáp:** Trả lời đầy đủ 5 câu hỏi phản biện cuối ngày của Mentor.

---

## 🧠 2. Kiến Thức Nền Tảng: Service Registry & Database-per-Service

### 2.1. Tại sao cần Service Registry (Eureka Server)?
Trong kiến trúc nguyên khối (Monolith), các hàm gọi nhau trực tiếp qua bộ nhớ RAM. Nhưng trong Microservices:
- Các service nằm ở các container/máy chủ khác nhau, địa chỉ IP thay đổi liên tục khi scale hoặc khởi động lại.
- **Nếu không có Service Registry:** Bạn sẽ phải hardcode IP và Port của từng service trong file config $\rightarrow$ Cực kỳ dễ gãy khi deploy lên Docker/Kubernetes.
- **Với Service Registry (Eureka Server):**
  1. Khi một service khởi động, nó tự gửi thông tin lên Eureka: *"Tôi là `TICKET-SERVICE`, đang chạy ở IP `192.168.1.5`, Port `8083`"*.
  2. Các service khác (như `ORDER-SERVICE` hoặc `API-GATEWAY`) muốn gọi tới `TICKET-SERVICE` chỉ cần hỏi Eureka Server: *"Cho tôi xin địa chỉ của `TICKET-SERVICE`"*. Eureka sẽ trả về địa chỉ kèm cân bằng tải (Client-side Load Balancing).

```
                            ┌────────────────────────┐
                            │ EUREKA SERVER (:8761)  │
                            │   (Service Registry)   │
                            └─────▲────────────▲─────┘
                                  │            │
                         Đăng ký  │            │  Đăng ký & Heartbeat
                        & Tra cứu │            │
                                  │            │
                 ┌────────────────┴─┐        ┌─┴────────────────┐
                 │   API GATEWAY    │        │  TICKET SERVICE  │
                 │   (Port 8080)    │───────>│   (Port 8083)    │
                 └──────────────────┘  Gọi   └──────────────────┘
```

---

### 2.2. Triết lý Database-per-Service (Nguyên Tắc Bất Di Bất Dịch)
Hệ thống World Cup 2026 sử dụng **5 cơ sở dữ liệu PostgreSQL độc lập**:
- `db_users` (Port 5432) $\rightarrow$ Thuộc về `user-service`
- `db_matches` (Port 5433) $\rightarrow$ Thuộc về `match-service`
- `db_tickets` (Port 5434) $\rightarrow$ Thuộc về `ticket-service`
- `db_orders` (Port 5435) $\rightarrow$ Thuộc về `order-service`
- `db_payments` (Port 5436) $\rightarrow$ Thuộc về `payment-service`

> ⚠️ **Quy tắc vàng:** Service A **TUYỆT ĐỐI KHÔNG ĐƯỢC PHÉP** kết nối trực tiếp vào database của Service B. Muốn lấy dữ liệu của Service B, Service A bắt buộc phải gọi qua REST API hoặc lắng nghe Kafka Event của Service B!  
> *Lợi ích:* Tránh việc một sự thay đổi cấu trúc bảng (schema change) ở service này làm sập hàng loạt service khác (Loose Coupling).

---

## 💻 3. Hướng Dẫn Thực Hành Từng Bước (Hands-on Lab)

### Bước 3.1: Viết tệp Docker Compose hạ tầng cơ sở dữ liệu
Tạo tệp `docker-compose-infra.yml` tại thư mục gốc của dự án:

```yaml
services:
  postgres-users:
    image: postgres:16-alpine
    container_name: postgres-users
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: db_users
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - pgdata-users:/var/lib/postgresql/data
    networks:
      - wc2026-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d db_users"]
      interval: 5s
      timeout: 5s
      retries: 5

  postgres-matches:
    image: postgres:16-alpine
    container_name: postgres-matches
    ports:
      - "5433:5432"
    environment:
      POSTGRES_DB: db_matches
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - pgdata-matches:/var/lib/postgresql/data
    networks:
      - wc2026-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d db_matches"]
      interval: 5s
      timeout: 5s
      retries: 5

  postgres-tickets:
    image: postgres:16-alpine
    container_name: postgres-tickets
    ports:
      - "5434:5432"
    environment:
      POSTGRES_DB: db_tickets
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - pgdata-tickets:/var/lib/postgresql/data
    networks:
      - wc2026-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d db_tickets"]
      interval: 5s
      timeout: 5s
      retries: 5

  postgres-orders:
    image: postgres:16-alpine
    container_name: postgres-orders
    ports:
      - "5435:5432"
    environment:
      POSTGRES_DB: db_orders
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - pgdata-orders:/var/lib/postgresql/data
    networks:
      - wc2026-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d db_orders"]
      interval: 5s
      timeout: 5s
      retries: 5

  postgres-payments:
    image: postgres:16-alpine
    container_name: postgres-payments
    ports:
      - "5436:5432"
    environment:
      POSTGRES_DB: db_payments
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    volumes:
      - pgdata-payments:/var/lib/postgresql/data
    networks:
      - wc2026-net
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d db_payments"]
      interval: 5s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    container_name: redis-cache
    ports:
      - "6379:6379"
    networks:
      - wc2026-net
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 5s
      timeout: 5s
      retries: 5

networks:
  wc2026-net:
    driver: bridge

volumes:
  pgdata-users:
  pgdata-matches:
  pgdata-tickets:
  pgdata-orders:
  pgdata-payments:
```

#### Khởi chạy hạ tầng Docker:
Chạy lệnh sau trong Terminal:
```bash
docker compose -f docker-compose-infra.yml up -d
```

Kiểm tra trạng thái các container:
```bash
docker compose -f docker-compose-infra.yml ps
```
> **Kỳ vọng:** Cả 6 container đều hiển thị `Up` hoặc `Up (healthy)`.

---

### Bước 3.2: Cập nhật Parent `pom.xml`
Thêm module `discovery-service` vào danh sách `<modules>` trong tệp `pom.xml` ở gốc:

```xml
    <modules>
        <module>common/common-dto</module>
        <module>common/common-exception</module>
        <module>common/common-security</module>
        <module>discovery-service</module>  <!-- Thêm dòng này -->
    </modules>
```

---

### Bước 3.3: Tạo module `discovery-service`

Tạo cây thư mục cho module:
```text
discovery-service/
├── pom.xml
└── src/
    └── main/
        ├── java/com/wc2026/discovery/
        │   └── DiscoveryApplication.java
        └── resources/
            └── application.yml
```

#### 1. Viết `discovery-service/pom.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.wc2026</groupId>
        <artifactId>wc2026-ticket-platform</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>discovery-service</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
</project>
```

#### 2. Viết `DiscoveryApplication.java`:
Tạo tại: `discovery-service/src/main/java/com/wc2026/discovery/DiscoveryApplication.java`
```java
package com.wc2026.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryApplication.class, args);
    }
}
```

#### 3. Viết cấu hình `application.yml`:
Tạo tại: `discovery-service/src/main/resources/application.yml`
```yaml
server:
  port: 8761

eureka:
  client:
    # Vì đây là Server trung tâm, không cần tự đăng ký chính mình vào registry
    register-with-eureka: false
    # Không cần lấy danh sách registry từ nơi khác
    fetch-registry: false
  server:
    # Tắt chế độ tự bảo vệ trong môi trường phát triển để nhanh chóng phát hiện service bị tắt
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000

spring:
  application:
    name: discovery-service
```

---

## 🧪 4. Kiểm Thử Nghiệm Thu (Verification)

### 4.1. Biên dịch toàn bộ dự án
Chạy lệnh sau tại thư mục gốc:
```bash
mvn clean compile
```
> Kết quả mong đợi: `BUILD SUCCESS` trên cả `common-*` và `discovery-service`.

### 4.2. Khởi chạy Eureka Server
Chạy lệnh:
```bash
mvn spring-boot:run -pl discovery-service
```
*(Hoặc click nút Run class `DiscoveryApplication` trong IntelliJ IDEA).*

### 4.3. Kiểm tra giao diện Dashboard
Mở trình duyệt Web và truy cập:
👉 **[http://localhost:8761](http://localhost:8761)**

**Kết quả thành công:**
- Giao diện **Spring Eureka** xuất hiện với banner đỏ/xanh.
- Mục **System Status** hiển thị Environment: `test`, Data center: `default`.
- Bảng **Instances currently registered with Eureka** hiện tại trống (hoặc `No instances available`), sẵn sàng đón nhận các service tiếp theo đăng ký vào.

---

## ❓ 5. Câu Hỏi Phản Biện Cuối Ngày (Day 2 Quiz)

1. **Tại sao Eureka Server lại phải cấu hình `register-with-eureka: false` và `fetch-registry: false`?**
   * *Đáp án:* Vì Eureka Server là trung tâm đầu não lưu trữ registry. Nếu không đặt `false`, nó sẽ cố gắng tìm kiếm một Eureka Server khác để đăng ký chính nó và ném ra lỗi ngoại lệ kết nối liên tục trên log.
2. **Cơ chế Heartbeat giữa Eureka Client và Eureka Server hoạt động như thế nào?**
   * *Đáp án:* Mặc định cứ mỗi 30 giây, Client sẽ gửi 1 tín hiệu heartbeat (ping) về Eureka Server để báo rằng mình vẫn còn sống. Nếu sau 90 giây Server không nhận được heartbeat, Server sẽ đánh dấu service đó đã chết và chuẩn bị xóa khỏi danh sách.
3. **Hiện tượng "Self-Preservation Mode" của Eureka là gì? Tại sao trong dev lại tắt (`enable-self-preservation: false`)?**
   * *Đáp án:* Khi có sự cố rớt mạng diện rộng (network partition), nhiều client không gửi được heartbeat nhưng bản thân chúng vẫn chạy tốt. Eureka sẽ kích hoạt Self-Preservation để không vội vàng xóa các service này đi. Tuy nhiên trong môi trường dev local, khi ta tắt 1 service, ta muốn nó biến mất khỏi Dashboard ngay lập tức để test, nên cần tắt chế độ này đi.
4. **Tại sao Microservices cấm tuyệt đối việc Service A truy vấn trực tiếp vào Database của Service B?**
   * *Đáp án:* Để đảm bảo tính độc lập và toàn vẹn dữ liệu (Loose Coupling & High Cohesion). Nếu Service A truy cập DB của Service B, Service B sẽ không thể tự do sửa đổi cấu trúc bảng, không thể kiểm soát được logic nghiệp vụ, và nếu DB của Service B bị khóa (lock) thì Service A cũng sẽ bị treo theo.
5. **Lệnh `pg_isready` trong cấu hình Docker healthcheck có tác dụng gì?**
   * *Đáp án:* Đảm bảo container không chỉ ở trạng thái "bật" (running) mà database bên trong đã hoàn tất quá trình khởi động và thực sự sẵn sàng chấp nhận các kết nối mạng đến từ ứng dụng Spring Boot.

---

## ⏭️ Bước Tiếp Theo:
Sau khi Mentor nghiệm thu Day 2, bạn sẽ được mở khóa **[DAY 3: Xây Dựng API Gateway (Spring Cloud Gateway) & Cơ Chế Định Tuyến Động X-Trace-Id](README_DAY3.md)**!
