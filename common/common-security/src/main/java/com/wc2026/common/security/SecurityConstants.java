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
