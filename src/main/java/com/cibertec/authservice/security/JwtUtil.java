package com.cibertec.authservice.security;
import com.cibertec.authservice.entities.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Component
public class JwtUtil {
  @Value("${jwt.secret}") private String secret;
  @Value("${jwt.expiration:3600000}") private long expiration;
  private SecretKey key;
  @PostConstruct public void init() {
    byte[] bytes=secret.getBytes(StandardCharsets.UTF_8);
    if(bytes.length<32) throw new IllegalStateException("jwt.secret requiere al menos 32 bytes");
    key=Keys.hmacShaKeyFor(bytes);
  }
  public String generateToken(Usuario u) {
    Date now=new Date();
    return Jwts.builder().subject(u.getUsername()).id(String.valueOf(u.getId()))
      .claim("rol",u.getRol().name()).issuedAt(now)
      .expiration(new Date(now.getTime()+expiration))
      .signWith(key,Jwts.SIG.HS256).compact();
  }
  public Claims claims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}