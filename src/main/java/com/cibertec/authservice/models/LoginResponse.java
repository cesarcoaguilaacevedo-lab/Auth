package com.cibertec.authservice.models;
import com.cibertec.authservice.entities.enums.Rol;
public record LoginResponse(Long id,String token,String username,String nombres,String apellidos,Rol rol) {}