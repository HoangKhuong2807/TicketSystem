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
