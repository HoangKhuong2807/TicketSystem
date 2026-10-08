package com.wc2026.common.security;

import java.util.List;

public record TokenClaims(
        String userId,
        String email,
        List<String> roles
) {}
