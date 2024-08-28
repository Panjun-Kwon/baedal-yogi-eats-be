package com.fstuckint.baedalyogieats.core.api.common.jwt;

import com.fstuckint.baedalyogieats.storage.db.core.token.TokenBlacklist;
import com.fstuckint.baedalyogieats.storage.db.core.token.TokenBlacklistRepository;
import com.fstuckint.baedalyogieats.storage.db.core.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String CLAIMS_USERNAME = "username";

    public static final String CLAIMS_UUID = "uuid";

    public static final String CLAIMS_ROLE = "auth";

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(UUID uuid, String username, UserRole role) {
        return BEARER_PREFIX + Jwts.builder()
            .claim(CLAIMS_UUID, uuid)
            .claim(CLAIMS_USERNAME, username)
            .claim(CLAIMS_ROLE, role.getAuthority())
            .signWith(key, SignatureAlgorithm.HS256)
            .setIssuer(issuer)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .compact();
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            TokenBlacklist blackToken = tokenBlacklistRepository.findByToken(token).orElse(null);
            return blackToken == null;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean checkAdmin(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String role = claims.get(CLAIMS_ROLE).toString();
        return role.equals(UserRole.MANAGER.getAuthority()) || role.equals(UserRole.MASTER.getAuthority());
    }

    public void addBlacklist(String token) {
        TokenBlacklist blackToken = new TokenBlacklist(token);
        tokenBlacklistRepository.save(blackToken);
    }

    public boolean checkOwner(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String role = claims.get(CLAIMS_ROLE).toString();
        return role.equals(UserRole.OWNER.getAuthority());
    }

}