package com.cibertec.authservice.config;

import com.cibertec.authservice.entities.Usuario;
import com.cibertec.authservice.entities.enums.Rol;
import com.cibertec.authservice.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {
    @Bean
    CommandLineRunner registrarAdministradorInicial(UsuarioRepository repository,
          PasswordEncoder passwordEncoder,
          @Value("${auth.bootstrap.enabled:false}") boolean enabled,
          @Value("${auth.bootstrap.username:admin}") String username,
          @Value("${auth.bootstrap.password:}") String password) {
        return args -> {
            if(!enabled || repository.count() != 0) return;
            if(password == null || password.length() < 8)
                throw new IllegalStateException("auth.bootstrap.password debe tener al menos 8 caracteres");
            Usuario admin=new Usuario();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setNombres("Administrador");
            admin.setApellidos("Inicial");
            admin.setRol(Rol.ADMINISTRADOR);
            admin.setActivo(true);
            repository.save(admin);
        };
    }
}
