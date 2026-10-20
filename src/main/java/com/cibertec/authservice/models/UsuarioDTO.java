package com.cibertec.authservice.models;
import com.cibertec.authservice.entities.enums.Rol;
import jakarta.validation.constraints.*;
public record UsuarioDTO(Long id,
 @NotBlank @Size(max=50) String username,
 String password,
 @NotBlank @Size(max=100) String nombres,
 @NotBlank @Size(max=100) String apellidos,
 @Email @Size(max=150) String email,
 @NotNull Rol rol, Boolean activo) {}