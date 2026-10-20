package com.cibertec.authservice.controllers;
import com.cibertec.authservice.entities.Usuario;
import com.cibertec.authservice.models.*;
import com.cibertec.authservice.security.JwtUtil;
import com.cibertec.authservice.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 private final UsuarioService service;private final JwtUtil jwt;
 public AuthController(UsuarioService service,JwtUtil jwt){this.service=service;this.jwt=jwt;}
 @PostMapping("/login") public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req){
   Usuario u=service.validar(req.username(),req.password());
   return ResponseEntity.ok(response(u,jwt.generateToken(u)));
 }
 @GetMapping("/me") public ResponseEntity<LoginResponse> me(Authentication authentication){
   Usuario u=service.buscarPorUsername(authentication.getName());
   return ResponseEntity.ok(response(u,null));
 }
 private LoginResponse response(Usuario u,String token){
   return new LoginResponse(u.getId(),token,u.getUsername(),u.getNombres(),u.getApellidos(),u.getRol());
 }
}