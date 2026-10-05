# 🏆 GIÁO TRÌNH ĐÀO TẠO MICROSERVICES THỰC CHIẾN
## DỰ ÁN: WORLD CUP 2026 TICKET PLATFORM 🎫
> **Mục tiêu đào tạo:** Huấn luyện lập trình viên từ nền tảng Spring Boot cơ bản tiến lên làm chủ kiến trúc Microservices phân tán, tự tay xây dựng 8 microservices, xử lý bài toán chống bán quá vé (**Anti-Oversell với Optimistic Locking**) và giao dịch phân tán (**SAGA Orchestrator**).

---

## 📌 BẢNG MỤC LỤC GIÁO TRÌNH TOÀN KHÓA (14 NGÀY)

Học viên sẽ được mở khóa (unlock) nội dung chi tiết theo từng ngày sau khi Tech Lead/Mentor nghiệm thu (Code Review) đạt yêu cầu của ngày trước đó:

| Ngày | Trạng Thái | Chủ Đề Trọng Tâm | Dịch Vụ & Hạ Tầng | Chuẩn Đầu Ra (DoD) |
| :---: | :---: | :--- | :--- | :--- |
| **DAY 1** | 🟢 **ACTIVE** | **Khung Dự Án Đa Module & 4 Hợp Đồng Dùng Chung** | Parent POM, `common-*` | Build thành công 3 common modules, pass bài test kiến trúc |
| **DAY 2** | 🔒 *Chờ duyệt* | Service Registry (Eureka Server) & Docker Databases | `discovery-service`, 5x Postgres, Redis | Eureka Dashboard UP, kết nối thành công 5 database độc lập |
| **DAY 3** | 🔒 *Chờ duyệt* | API Gateway Đơn Điểm & Trace ID Correlation | `api-gateway`, Spring Cloud Gateway | Định tuyến động qua Eureka, tự sinh `X-Trace-Id` |
| **DAY 4** | 🔒 *Chờ duyệt* | Quản Lý Người Dùng & Asymmetric JWT (RSA-256) | `user-service`, Flyway migration | Đăng ký, đăng nhập cấp JWT ký bằng RSA Private Key |
| **DAY 5** | 🔒 *Chờ duyệt* | Bảo Mật Tập Trung Gateway & Token Blacklist | `api-gateway`, Redis Blacklist | Gateway xác thực RSA Public Key, forward `X-User-Id` |
| **DAY 6** | 🔒 *Chờ duyệt* | Quản Lý Trận Đấu, Lọc Đa Tiêu Chí & Redis Cache | `match-service`, Spring Data JPA Specs | Lọc trận đấu phân trang, áp dụng Cache-Aside Pattern |
| **DAY 7** | 🔒 *Chờ duyệt* | Tồn Kho Vé & Thiết Kế Bất Biến Toàn Vẹn | `ticket-service` (Part 1), Flyway | Đảm bảo `available + held + sold = total_seats` |
| **DAY 8** | 🔒 *Chờ duyệt* | ⭐ **Chống Oversell: Optimistic Lock & Test Concurrency** | `ticket-service` (Part 2), `@Version` | Giữ vé 10 phút, test 50 threads tranh chấp không oversell |
| **DAY 9** | 🔒 *Chờ duyệt* | SAGA Orchestrator: Khởi Tạo Đơn Hàng & Giữ Vé | `order-service`, `payment-service` | WebClient gọi `/hold` vé, chuyển trạng thái `TICKET_HELD` |
| **DAY 10** | 🔒 *Chờ duyệt* | ⭐ **Hoàn Thiện SAGA & Cơ Chế Bù Trừ (Compensate)** | `order-service` SAGA State Machine | Mô phỏng lỗi thanh toán, tự động hoàn vé (`/release`) |
| **DAY 11** | 🔒 *Chờ duyệt* | Event-Driven với Apache Kafka & Stripe Payment | Apache Kafka KRaft, Stripe Webhook | Lắng nghe Webhook Stripe, publish event `payment.confirmed` |
| **DAY 12** | 🔒 *Chờ duyệt* | Xuất Vé PDF, Mã QR & MinIO Object Storage | `notification-service`, MinIO, Mailtrap | Tạo vé PDF A5, mã QR, lưu MinIO và gửi email xác nhận |
| **DAY 13** | 🔒 *Chờ duyệt* | Hàng Đợi Ảo (Virtual Queue) & Giám Sát Phân Tán | Redis Virtual Queue, Zipkin, Prometheus | Chống nghẽn truy cập đột biến, vẽ biểu đồ trên Grafana |
| **DAY 14** | 🔒 *Chờ duyệt* | Đóng Gói Full Stack Docker Compose & Live Demo | Docker Compose 16 Containers, Scripts | Khởi động 1-click `run.bat`, toàn bộ E2E Test PASS 100% |

---

## 📜 QUY TRÌNH HỌC TẬP & NỘP BÀI (WORKFLOW)

1. **Nhận bài hàng ngày:** Đọc kỹ lý thuyết, mục tiêu và hướng dẫn thực hành của ngày hiện tại trong README.
2. **Thực hiện code:** Tự tay gõ mã nguồn trên nhánh làm việc (ví dụ: `feature/day-1-common-modules`), tuyệt đối không copy-paste mà không hiểu.
3. **Tự kiểm thử (Self-Test):** Chạy lệnh build và test theo hướng dẫn trong bài lab.
4. **Tạo Pull Request / Commit & Push:** Đẩy code lên repository với thông điệp chuẩn Conventional Commits (ví dụ: `feat(day-1): setup parent pom and common modules`).
5. **Code Review & Nghiệm thu:** Mentor sẽ so sánh trực tiếp code của bạn với tiêu chuẩn doanh nghiệp của dự án gốc, đưa ra nhận xét:
   * 🟢 **Đạt chuẩn (Passed):** Được Tech Lead duyệt để mở khóa bài học ngày tiếp theo!
   * 🟡 **Cần sửa đổi (Needs Revision):** Cần fix theo góp ý của Mentor trước khi chuyển ngày.

---
---

# 🟢 NỘI DUNG CHI TIẾT: DAY 1
### CHỦ ĐỀ: KHUNG DỰ ÁN ĐA MODULE (PARENT POM) & 4 HỢP ĐỒNG DÙNG CHUNG

> **Thời lượng khuyến nghị:** 1 ngày làm việc (6 - 8 tiếng)  
> **Độ khó:** ⭐⭐☆☆☆ (Nền tảng kiến trúc)

---

### 🎯 1. Mục Tiêu Đầu Ra Của Day 1 (Definition of Done)
Trước khi kết thúc ngày hôm nay, bạn phải hoàn thành các tiêu chí sau:
- [ ] Giải thích được tại sao hệ thống bán vé lại cần kiến trúc Microservices và tư duy **Contract-First**.
- [ ] Cài đặt đầy đủ môi trường phát triển (JDK 21/25, Maven, Docker, Git, IDE).
- [ ] Viết xong Parent `pom.xml` quản lý version tập trung cho Spring Boot 3.4.x và Spring Cloud 2024.x.
- [ ] Hoàn thiện 3 thư viện dùng chung trong thư mục `common/`:
  - `common-dto`: Chứa vỏ bọc sự kiện Kafka `BaseEvent<T>`.
  - `common-exception`: Bắt lỗi tập trung chuẩn quốc tế RFC 7807 `ProblemDetail`.
  - `common-security`: Tiện ích giải mã token JWT RSA-256 (`JwtUtil`, `SecurityConstants`).
- [ ] Chạy lệnh `mvn clean compile` tại thư mục gốc thành công 100% không có cảnh báo nghiêm trọng.
- [ ] Trả lời được 5 câu hỏi phản biện cuối ngày của Mentor.

---

### 🛠️ 2. Chuẩn Bị Môi Trường Phát Triển
Kiểm tra các phần mềm đã cài đặt trên máy bằng Terminal:
```bash
java -version           # Yêu cầu: Java 21 LTS hoặc Java 25
mvn -v                  # Yêu cầu: Maven 3.9+
docker --version        # Yêu cầu: Docker Desktop hoạt động
git --version           # Yêu cầu: Git 2.40+
```
> ⚠️ **Lưu ý IDE (IntelliJ IDEA):** Vào `Settings` $\rightarrow$ `Build, Execution, Deployment` $\rightarrow$ `Compiler` $\rightarrow$ `Annotation Processors` và tích chọn **Enable annotation processing** để IDE nhận diện Lombok.

---

### 🧠 3. Kiến Thức Nền Tảng: Bài Toán Bán Vé & 4 Hợp Đồng Vàng

#### 3.1. Bài toán bán vé World Cup có gì thách thức?
1. **Lưu lượng truy cập cực lớn (Spike Traffic):** Hàng triệu người cùng truy cập đặt vé trận Chung kết. Nếu hệ thống monolith bị nghẽn ở module thanh toán, toàn bộ website sẽ sập theo. Microservices cho phép tách độc lập để scale module cần thiết.
2. **Nguy cơ Oversell:** Sân vận động có giới hạn ghế ngồi. Nếu 2 người cùng nhấn mua chiếc vé cuối cùng, làm sao để hệ thống không bán quá số lượng? (Chúng ta sẽ giải quyết ở Day 8 bằng Optimistic Locking).
3. **Giao dịch phân tán:** Quy trình đặt vé trải dài qua 3 service: `ticket-service` (giữ vé) $\rightarrow$ `payment-service` (trừ tiền) $\rightarrow$ `order-service` (hoàn tất). Nếu trừ tiền lỗi thì vé giữ phải được tự động hoàn lại (SAGA Compensation ở Day 10).

#### 3.2. 4 Hợp đồng vàng (Golden Contracts)
Để 8 service sau này có thể giao tiếp trơn tru mà không bị xung đột, bạn phải tuân thủ 4 quy ước:

1. **Header nội bộ chuyển tiếp qua Gateway:**
   ```http
   X-User-Id:    <UUID người dùng>
   X-User-Roles: ROLE_USER | ROLE_ADMIN | ROLE_STAFF
   X-Trace-Id:   <UUID vết chuỗi request để trace log>
   ```
2. **Cấu trúc Access Token (RSA-256 Asymmetric):**
   * Service phát hành (`user-service`) giữ Private Key để ký.
   * Gateway và các service khác chỉ giữ Public Key để verify, tuyệt đối không share Private Key.
   * TTL: 15 phút.
3. **Định dạng lỗi chuẩn RFC 7807 (`ProblemDetail`):**
   * Mọi API khi gặp lỗi đều phải trả về đối tượng có cấu trúc: `type`, `title`, `status`, `detail`, `instance`, `timestamp`.
4. **Vỏ bọc sự kiện Kafka (`BaseEvent<T>`):**
   * Gồm: `eventId` (UUID), `eventType`, `source`, `timestamp` (UTC ISO8601), và `payload` mang dữ liệu thực.

---

### 💻 4. Hướng Dẫn Thực Hành Từng Bước (Hands-on Lab)

#### 📁 Bước 1: Khởi tạo cấu trúc thư mục
Tạo cây thư mục chuẩn như sau:
```text
wc2026-ticket-platform/
├── pom.xml                                ← Parent POM
└── common/
    ├── common-dto/                        ← DTO & Kafka event envelope
    │   ├── pom.xml
    │   └── src/main/java/com/wc2026/common/dto/event/BaseEvent.java
    ├── common-exception/                  ← RFC 7807 GlobalExceptionHandler
    │   ├── pom.xml
    │   └── src/main/java/com/wc2026/common/exception/
    └── common-security/                   ← Tiện ích JWT RSA-256
        ├── pom.xml
        └── src/main/java/com/wc2026/common/security/
```

---

#### 📄 Bước 2: Tạo Parent `pom.xml` tại thư mục gốc
Tạo tệp `pom.xml` tại thư mục gốc của dự án:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.4</version>
        <relativePath/>
    </parent>

    <groupId>com.wc2026</groupId>
    <artifactId>wc2026-ticket-platform</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <name>WC2026 Ticket Platform</name>
    <description>Hệ thống bán vé World Cup 2026 - Edu Edition</description>

    <properties>
        <java.version>21</java.version>
        <lombok.version>1.18.38</lombok.version>
        <spring-cloud.version>2024.0.0</spring-cloud.version>
        <jjwt.version>0.12.6</jjwt.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <modules>
        <module>common/common-dto</module>
        <module>common/common-exception</module>
        <module>common/common-security</module>
    </modules>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>${jjwt.version}</version>
                <scope>runtime</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

---

#### 📦 Bước 3: Hiện thực hóa module `common-dto`
1. Tạo `common/common-dto/pom.xml`:
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
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>common-dto</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
        </dependency>
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
        </dependency>
    </dependencies>
</project>
```

2. Tạo `common/common-dto/src/main/java/com/wc2026/common/dto/event/BaseEvent.java`:
```java
package com.wc2026.common.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseEvent<T>(
        @NotBlank String eventId,
        @NotBlank String eventType,
        @NotBlank String source,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC") Instant timestamp,
        @NotNull T payload
) {}
```

---

#### 📦 Bước 4: Hiện thực hóa module `common-exception`
1. Tạo `common/common-exception/pom.xml`:
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
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>common-exception</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
    </dependencies>
</project>
```

2. Tạo các Custom Exception trong package `com.wc2026.common.exception`:
* `ResourceNotFoundException.java`:
```java
package com.wc2026.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```
* `BusinessRuleException.java`:
```java
package com.wc2026.common.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {
    private final int status;
    private final String errorType;

    public BusinessRuleException(int status, String errorType, String message) {
        super(message);
        this.status = status;
        this.errorType = errorType;
    }
}
```

3. Tạo bộ đón lỗi tập trung `GlobalExceptionHandler.java`:
```java
package com.wc2026.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://wc2026-tickets.com/errors/resource-not-found"));
        pd.setTitle("Resource Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.resolve(ex.getStatus()), ex.getMessage());
        pd.setType(URI.create("https://wc2026-tickets.com/errors/" + ex.getErrorType()));
        pd.setTitle("Business Rule Violation");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        record FieldErrorItem(String field, String message, Object rejectedValue) {}

        List<FieldErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorItem(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
                .collect(Collectors.toList());

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create("https://wc2026-tickets.com/errors/validation-error"));
        pd.setTitle("Validation Failed");
        pd.setDetail("Dữ liệu gửi lên không hợp lệ (" + errors.size() + " lỗi)");
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        log.error("Unhandled exception: ", ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setType(URI.create("https://wc2026-tickets.com/errors/internal-server-error"));
        pd.setTitle("Internal Server Error");
        pd.setDetail("Lỗi hệ thống không xác định. Vui lòng liên hệ Admin.");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
```

---

#### 📦 Bước 5: Hiện thực hóa module `common-security`
1. Tạo `common/common-security/pom.xml`:
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
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>common-security</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```

2. Tạo các class trong `com.wc2026.common.security`:
* `SecurityConstants.java`:
```java
package com.wc2026.common.security;

public final class SecurityConstants {
    private SecurityConstants() {}

    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USER_ROLES = "X-User-Roles";
    public static final String HEADER_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_EMAIL = "email";

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_STAFF = "ROLE_STAFF";

    public static final long ACCESS_TOKEN_TTL = 900;       // 15 phút
    public static final long REFRESH_TOKEN_TTL = 604800;   // 7 ngày
}
```

* `TokenClaims.java`:
```java
package com.wc2026.common.security;

import java.util.List;

public record TokenClaims(
        String userId,
        String email,
        List<String> roles
) {}
```

* `JwtUtil.java`:
```java
package com.wc2026.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.security.PublicKey;
import java.util.List;

public class JwtUtil {
    private final PublicKey publicKey;

    public JwtUtil(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public TokenClaims validateAndParse(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            Claims claims = jws.getPayload();
            String userId = claims.getSubject();
            String email = claims.get(SecurityConstants.CLAIM_EMAIL, String.class);
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get(SecurityConstants.CLAIM_ROLES, List.class);
            if (roles == null) {
                roles = List.of();
            }

            if (userId == null || userId.isBlank()) {
                return null;
            }

            return new TokenClaims(userId, email, roles);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
```

---

### 🧪 5. Kiểm Thử Biên Dịch (Verification)
Mở Terminal tại thư mục gốc của dự án và chạy lệnh:
```bash
mvn clean compile
```

Nếu kết quả hiển thị **`BUILD SUCCESS`** trên cả 3 module `common-dto`, `common-exception`, `common-security`, bạn đã hoàn tất xuất sắc phần code của Day 1!

---

### ❓ 6. Câu Hỏi Phản Biện Cuối Ngày (Day 1 Quiz)
*Hãy tự trả lời 5 câu hỏi này trước khi nộp bài cho Mentor:*
1. **Tại sao ta dùng thuật toán bất đối xứng RSA-256 thay vì HMAC-SHA256 (Secret Key)?**
   *(Gợi ý: Trong Microservices, không nên chia sẻ Secret Key ký token cho các downstream service; chỉ user-service giữ Private Key, các service khác chỉ cần Public Key để đọc).*
2. **Khái niệm `ProblemDetail` trong RFC 7807 mang lại lợi ích gì cho Frontend và Mobile App?**
3. **Tại sao `BaseEvent<T>` lại dùng Java `record` thay vì `class` thông thường?**
4. **Header `X-Trace-Id` đóng vai trò gì khi hệ thống gặp sự cố ở môi trường phân tán?**
5. **Nếu ta thêm một DTO mới dùng chung cho nhiều service, ta nên đặt ở đâu?**

---

### ⏭️ Xem Trước Day 2 (Preview)
*Sau khi được Tech Lead duyệt Day 1, bạn sẽ được mở khóa **DAY 2**: Khởi tạo Service Registry trung tâm với **Netflix Eureka Server** (`discovery-service`) và thiết lập tệp **Docker Compose** chạy cụm 5 cơ sở dữ liệu PostgreSQL độc lập cùng Redis Cache!*
