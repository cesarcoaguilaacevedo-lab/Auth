package com.cibertec.authservice.entities;

import com.cibertec.authservice.entities.enums.Rol;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 100)
    private String nombres;
    @Column(nullable = false, length = 100)
    private String apellidos;
    @Column(length = 150)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;
    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long x) {
        id = x;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String x) {
        username = x;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String x) {
        password = x;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String x) {
        nombres = x;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String x) {
        apellidos = x;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String x) {
        email = x;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol x) {
        rol = x;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean x) {
        activo = x;
    }
}