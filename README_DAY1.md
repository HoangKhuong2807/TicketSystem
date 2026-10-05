# 🚀 DAY 1: NỀN TẢNG KIẾN TRÚC, PARENT POM & 3 COMMON MODULES
> **Dự án:** World Cup 2026 Ticket Platform 🎫  
> **Thời lượng khuyến nghị:** 1 ngày làm việc (6 - 8 tiếng)  
> **Độ khó:** ⭐⭐☆☆☆ (Nền tảng kiến trúc)

---

## 🎯 1. Mục Tiêu Đầu Ra (Definition of Done - DoD Day 1)

Trước khi kết thúc ngày hôm nay, học viên phải hoàn thành trọn vẹn các tiêu chí sau:
- [ ] **Hiểu bài toán & kiến trúc:** Giải thích được vì sao hệ thống bán vé World Cup lại cần Microservices, vai trò của 8 microservices và tư duy thiết kế **Contract-First**.
- [ ] **Chuẩn bị môi trường:** Cài đặt xong JDK (21 hoặc 25), Maven 3.9+, Docker Desktop, Git và IDE (IntelliJ IDEA).
- [ ] **Thống nhất 4 quy ước vàng:** Nắm vững cấu trúc Headers nội bộ, JWT RSA-256, RFC 7807 Error, Kafka `BaseEvent<T>`.
- [ ] **Hoàn thành Parent `pom.xml`:** Cấu hình Dependency Management tập trung cho Spring Boot 3.4.x, Spring Cloud 2024.x, JJWT, Lombok.
- [ ] **Hoàn thành 3 thư viện dùng chung (`common/`):**
  1. `common-dto`: Chứa Java Record `BaseEvent<T>`.
  2. `common-exception`: Bắt lỗi tập trung `GlobalExceptionHandler`, RFC 7807 `ProblemDetail`, `ResourceNotFoundException`, `BusinessRuleException`.
  3. `common-security`: `SecurityConstants`, `JwtUtil` (xác thực token bằng RSA Public Key), `TokenClaims`.
- [ ] **Nghiệm thu thực tế:** Mở Terminal chạy lệnh `mvn clean compile` tại thư mục gốc thành công 100% không có lỗi.
- [ ] **Vấn đáp:** Trả lời đầy đủ 5 câu hỏi phản biện cuối ngày của Mentor.

---

## 🛠️ 2. Chuẩn Bị Môi Trường Phát Triển (Prerequisites)

| Công cụ | Phiên bản yêu cầu | Lệnh kiểm tra trong Terminal |
|---|---|---|
| **JDK** | JDK 21 LTS hoặc JDK 25 | `java -version` |
| **Maven** | 3.9.x trở lên | `mvn -v` |
| **Docker & Compose** | Docker Desktop mới nhất | `docker --version` & `docker compose version` |
| **Git** | 2.40+ | `git --version` |
| **IDE** | IntelliJ IDEA (Ultimate / Community) | Bật Annotation Processing |

> ⚠️ **Lưu ý cấu hình IntelliJ IDEA:**  
> Vào **Settings (Ctrl+Alt+S)** $\rightarrow$ **Build, Execution, Deployment** $\rightarrow$ **Compiler** $\rightarrow$ **Annotation Processors** $\rightarrow$ Tích chọn **Enable annotation processing** (để tránh lỗi không nhận Lombok `@Getter`, `@Slf4j`).

---

## 🧠 3. Kiến Thức Nền Tảng: Bài Toán Bán Vé & 4 Quy Ước Vàng

### 3.1. Bài toán bán vé World Cup 2026 có gì thách thức?
1. **Lưu lượng truy cập cực lớn (Spike Traffic):** Hàng triệu cổ động viên cùng truy cập đặt vé trận Chung kết trong vòng vài phút. Nếu hệ thống Monolith bị nghẽn ở phân hệ thanh toán, toàn bộ website sẽ chết theo. Microservices cho phép bóc tách để scale riêng service chịu tải.
2. **Nguy cơ Oversell (Bán quá số vé thực tế):** Sân vận động có giới hạn ghế ngồi. Nếu 2 người cùng nhấn mua chiếc vé cuối cùng, hệ thống bắt buộc phải có cơ chế khóa đồng thời (**Optimistic Locking**) để không bao giờ bán vượt quá số vé khả dụng.
3. **Giao dịch phân tán (Distributed Transactions):** Quy trình đặt vé trải dài qua 3 service với 3 database riêng biệt: `ticket-service` (giữ vé) $\rightarrow$ `payment-service` (trừ tiền Stripe) $\rightarrow$ `order-service` (xác nhận đơn hàng). Không thể dùng `@Transactional` đơn thuần của 1 database, mà phải áp dụng **SAGA Orchestrator** kèm cơ chế bù trừ (Compensation) khi thanh toán lỗi.

### 3.2. Bốn quy ước vàng của hệ thống (Golden Contracts)

```
[ Client ] ──(Request)──> [ API Gateway ] ──(X-User-Id / X-Trace-Id)──> [ Downstream Services ]
```

1. **Forward Headers (Gateway $\rightarrow$ Downstream Services):**
   Sau khi API Gateway xác thực token thành công, Gateway sẽ tự động gắn các header nội bộ sau vào request trước khi chuyển tiếp:
   ```http
   X-User-Id:    6ba7b810-9dad-11d1-80b4-00c04fd430c8  (UUID người dùng)
   X-User-Roles: ROLE_USER                             (ROLE_USER, ROLE_ADMIN, ROLE_STAFF)
   X-Trace-Id:   a1b2c3d4-e5f6-7890-abcd-ef1234567890  (UUID chuỗi request để trace log)
   ```
2. **Cấu trúc Access Token (RSA-256 Asymmetric):**
   * Service phát hành (`user-service`) giữ **Private Key** để ký JWT khi người dùng login.
   * `api-gateway` và các service khác chỉ giữ **Public Key** để kiểm tra tính hợp lệ của token, không bao giờ lo bị lộ Private Key.
   * Thời gian sống: Access Token = 15 phút, Refresh Token = 7 ngày.
3. **Định dạng lỗi chuẩn RFC 7807 (`ProblemDetail`):**
   Mọi API trong toàn bộ hệ sinh thái khi trả về lỗi đều phải tuân thủ định dạng chuẩn quốc tế RFC 7807:
   ```json
   {
     "type": "https://wc2026-tickets.com/errors/resource-not-found",
     "title": "Resource Not Found",
     "status": 404,
     "detail": "Trận đấu với ID 123 không tồn tại trên hệ thống",
     "instance": "/api/v1/matches/123",
     "timestamp": "2026-07-01T10:00:00Z"
   }
   ```
4. **Vỏ bọc sự kiện Kafka (`BaseEvent<T>`):**
   Mọi message publish lên Apache Kafka đều được bọc trong Record `BaseEvent<T>` gồm: `eventId` (UUID), `eventType`, `source`, `timestamp` (UTC ISO8601), và `payload` chứa dữ liệu chi tiết.

---

## 💻 4. Hướng Dẫn Thực Hành Chi Tiết (Hands-on Lab)

### Bước 4.1: Cấu trúc thư mục dự án
Tạo cấu trúc thư mục như sau tại gốc repository:
```text
wc2026-ticket-platform/
├── pom.xml                                ← Parent POM
└── common/
    ├── common-dto/                        ← DTO & Kafka Event Envelope
    │   ├── pom.xml
    │   └── src/main/java/com/wc2026/common/dto/event/BaseEvent.java
    ├── common-exception/                  ← RFC 7807 GlobalExceptionHandler
    │   ├── pom.xml
    │   └── src/main/java/com/wc2026/common/exception/...
    └── common-security/                   ← JWT RSA-256 Utility
        ├── pom.xml
        └── src/main/java/com/wc2026/common/security/...
```

---

### Bước 4.2: Viết Parent `pom.xml` (Gốc Dự Án)
Tệp `pom.xml` tại thư mục gốc quản lý toàn bộ phiên bản thư viện dùng chung cho cả 8 microservices sau này:

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
            <!-- Spring Cloud Dependencies -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- JJWT (RSA-256 Support) -->
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

### Bước 4.3: Viết Module `common-dto`

#### 1. File `common/common-dto/pom.xml`:
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

#### 2. Class `BaseEvent.java`:
Tạo tại: `common/common-dto/src/main/java/com/wc2026/common/dto/event/BaseEvent.java`
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

### Bước 4.4: Viết Module `common-exception`

#### 1. File `common/common-exception/pom.xml`:
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

#### 2. Các Custom Exceptions:
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

#### 3. Bắt lỗi tập trung `GlobalExceptionHandler.java`:
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

### Bước 4.5: Viết Module `common-security`

#### 1. File `common/common-security/pom.xml`:
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

#### 2. Class hằng số `SecurityConstants.java`:
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

#### 3. Record `TokenClaims.java`:
```java
package com.wc2026.common.security;

import java.util.List;

public record TokenClaims(
        String userId,
        String email,
        List<String> roles
) {}
```

#### 4. Tiện ích `JwtUtil.java` (RSA Public Key Verification):
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

## 🧪 5. Kiểm Thử Nghiệm Thu (Verification)

Mở Terminal tại thư mục gốc của dự án và chạy lệnh sau:
```bash
mvn clean compile
```

### Kỳ vọng kết quả:
```text
[INFO] Reactor Summary for WC2026 Ticket Platform 1.0.0-SNAPSHOT:
[INFO] 
[INFO] WC2026 Ticket Platform ............................. SUCCESS
[INFO] common-dto ......................................... SUCCESS
[INFO] common-exception ................................... SUCCESS
[INFO] common-security .................................... SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## ❓ 6. Câu Hỏi Phản Biện Cuối Ngày (Day 1 Quiz)

1. **Tại sao ta dùng thuật toán bất đối xứng RSA-256 thay vì HMAC-SHA256 (Secret Key)?**
   * *Đáp án mong đợi:* HMAC dùng chung 1 khóa bí mật để cả ký và kiểm tra. Nếu 1 service bị tấn công để lộ khóa, kẻ gian có thể tự sinh token giả mạo danh tính cho cả hệ thống. Với RSA, chỉ `user-service` giữ Private Key để ký, các service khác chỉ giữ Public Key để đọc, an toàn hơn nhiều.
2. **Khái niệm `ProblemDetail` trong RFC 7807 mang lại lợi ích gì cho hệ thống?**
   * *Đáp án mong đợi:* Chuẩn hóa cấu trúc phản hồi lỗi giữa tất cả các Microservices, giúp Frontend/Mobile xử lý lỗi đồng nhất mà không cần viết nhiều logic parser khác nhau.
3. **Tại sao `BaseEvent<T>` lại dùng Java `record` thay vì `class` thông thường?**
   * *Đáp án mong đợi:* `record` đảm bảo tính bất biến (immutable), tự sinh constructor, getter, `equals`, `hashCode`, tránh việc payload của sự kiện bị sửa đổi ngoài ý muốn trong quá trình lan truyền qua Kafka.
4. **Header `X-Trace-Id` đóng vai trò gì khi hệ thống gặp sự cố?**
   * *Đáp án mong đợi:* Trong hệ thống phân tán gồm nhiều service, `X-Trace-Id` giúp liên kết tất cả các log của cùng 1 luồng request từ Gateway đến Order, Ticket, Payment, phục vụ việc truy vết nguyên nhân lỗi nhanh chóng.
5. **Thẻ `<dependencyManagement>` trong Parent POM khác gì với thẻ `<dependencies>`?**
   * *Đáp án mong đợi:* `<dependencyManagement>` chỉ khai báo quản lý phiên bản (version) tập trung chứ chưa tải thư viện về. Thư viện chỉ thực sự được tải khi các module con khai báo lại dependency đó trong thẻ `<dependencies>` của riêng nó (không cần chỉ định version).

---

## ⏭️ Bước Tiếp Theo:
Khi hoàn tất toàn bộ yêu cầu trên, hãy commit code, tạo PR và báo cho Mentor nghiệm thu để được mở khóa **[DAY 2: Service Registry (Eureka Server) & Hạ Tầng Docker Database](README_DAY2.md)**!
