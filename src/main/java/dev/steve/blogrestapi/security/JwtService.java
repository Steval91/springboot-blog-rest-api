package dev.steve.blogrestapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private static final Logger log = LoggerFactory.getLogger(JwtService.class);

  @Value("${application.jwt.secret-key}")
  private String jwtSecretKey;

  @Value("${application.jwt.access-token-expiration-millis}")
  private int tokenExpirationMilis;

  @Value("${application.jwt.refresh-token-expiration-millis}")
  private int refreshTokenExpirationMilis;

  public String generateToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, tokenExpirationMilis);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, tokenExpirationMilis);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(
        new HashMap<>(),
        userDetails,
        refreshTokenExpirationMilis);
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      int expirationMilis) {
    MacAlgorithm algorithm = Jwts.SIG.HS256;
    return Jwts
        .builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationMilis))
        .signWith(getSigningKey(), algorithm)
        .compact();
  }

  private SecretKey getSigningKey() {
    byte[] key = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(key);
  }

  private Claims extractAllClaims(String token) {
    log.info("extractAllClaims: {}", token);
    return Jwts
        .parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    log.info("extractClaim: {}", token);
    final Claims claims = extractAllClaims(token);
    log.info("claims: {}", claims);
    return claimsResolver.apply(claims);
  }

  public String extractUsername(String token) {
    log.info("extractUsername: {}", token);
    return extractClaim(token, Claims::getSubject);
  }

  private Date extractExpirationDate(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Boolean isTokenExpired(String token) {
    return extractExpirationDate(token).before(new Date());
  }

  public Boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
