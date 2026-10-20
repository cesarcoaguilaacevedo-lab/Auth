package com.cibertec.authservice.security;
import com.cibertec.authservice.entities.enums.Rol;
import com.cibertec.authservice.repositories.UsuarioRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwt; private final UsuarioRepository usuarios;
  public JwtFilter(JwtUtil jwt,UsuarioRepository usuarios){this.jwt=jwt;this.usuarios=usuarios;}
  @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)
      throws ServletException,IOException {
    String header=req.getHeader("Authorization");
    if(header==null || !header.startsWith("Bearer ")){chain.doFilter(req,res);return;}
    UsernamePasswordAuthenticationToken authentication;
    try {
      Claims claims=jwt.claims(header.substring(7));
      String username=claims.getSubject();
      String role=claims.get("rol",String.class);
      Rol.valueOf(role);
      var user=usuarios.findByUsername(username)
        .filter(u->Boolean.TRUE.equals(u.getActivo()) && u.getRol().name().equals(role))
        .orElseThrow();
      authentication=new UsernamePasswordAuthenticationToken(
          user.getUsername(),null,List.of(new SimpleGrantedAuthority("ROLE_"+role)));
    } catch(RuntimeException ex) {
      SecurityContextHolder.clearContext();
      res.sendError(HttpStatus.UNAUTHORIZED.value(),"JWT invalido o usuario inactivo");
      return;
    }
    try {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(req,res);
    } finally {
      SecurityContextHolder.clearContext();
    }
  }
}