package com.isegoria.server.global.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.isegoria.server.global.constants.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final Constants constants;
  private final Key accessTokenSigningKey;
  private final Key refreshTokenSigningKey;

  public JwtProvider(Constants constants) {
    this.constants = constants;
    this.accessTokenSigningKey = getSigningKey(constants.getAccessTokenSecretKey());
    this.refreshTokenSigningKey = getSigningKey(constants.getRefreshTokenSecretKey());
  }

  public long getAccessTokenExpiredAt() {
    return constants.getAccessTokenExpiredAt();
  }

  public long getRefreshTokenExpiredAt() {
    return constants.getRefreshTokenExpiredAt();
  }

  private Key getSigningKey(String secretKeyBase64) {
    return Keys.hmacShaKeyFor(secretKeyBase64.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(UUID id) {
    Instant now = Instant.now();
    Instant expiryInstant = now.plus(Duration.ofSeconds(getAccessTokenExpiredAt()));
    Date expiredAt = Date.from(expiryInstant);

    String jwt = Jwts.builder()
        .signWith(accessTokenSigningKey, SignatureAlgorithm.HS256)
        .setSubject(id.toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(expiredAt)
        .compact();
    log.info("Access Token 생성 완료 (Subject: {})", id);
    return jwt;
  }

  public String createRefreshToken(UUID id) {
    Instant now = Instant.now();
    Instant expiryInstant = now.plus(Duration.ofSeconds(getRefreshTokenExpiredAt()));
    Date expiredAt = Date.from(expiryInstant);

    String jwt = Jwts.builder()
        .signWith(refreshTokenSigningKey, SignatureAlgorithm.HS256)
        .setSubject(id.toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(expiredAt)
        .compact();
    log.info("Refresh Token 생성 완료 (Subject: {})", id);
    return jwt;
  }

  public Claims validateAccessToken(String accessToken) {
    return parseClaims(accessToken, accessTokenSigningKey, "액세스 토큰");
  }

  public Claims validateRefreshToken(String refreshToken) {
    return parseClaims(refreshToken, refreshTokenSigningKey, "리프레시 토큰");
  }

  private Claims parseClaims(String token, Key signingKey, String tokenType) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      log.warn("만료된 {}입니다.", tokenType, e);
    } catch (UnsupportedJwtException e) {
      log.warn("지원되지 않는 {}입니다.", tokenType, e);
    } catch (MalformedJwtException e) {
      log.warn("형식 오류 또는 잘못된 구성의 {}입니다.", tokenType, e);
    } catch (SignatureException e) {
      log.warn("유효하지 않은 {} 서명입니다.", tokenType, e);
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않거나 비어있는 {}입니다.", tokenType, e);
    } catch (Exception e) {
      log.error("{} 검증 중 알 수 없는 오류 발생.", tokenType, e);
    }
    return null;
  }
}
