package com.cibertec.authservice.services;
import com.cibertec.authservice.entities.Usuario;
import com.cibertec.authservice.models.UsuarioDTO;
import com.cibertec.authservice.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class UsuarioService {
 private final UsuarioRepository repo;
 private final PasswordEncoder encoder;
 public UsuarioService(UsuarioRepository repo,PasswordEncoder encoder){this.repo=repo;this.encoder=encoder;}
 @Transactional(readOnly=true) public List<UsuarioDTO> listar(){return repo.findAll().stream().map(this::dto).toList();}
 @Transactional(readOnly=true) public UsuarioDTO buscar(Long id){return dto(obtener(id));}
 @Transactional public UsuarioDTO crear(UsuarioDTO d){
   if(repo.existsByUsername(d.username())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Username ya existe");
   if(d.password()==null || d.password().length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La contraseña debe tener al menos 8 caracteres");
   Usuario u=new Usuario(); u.setUsername(d.username()); actualizar(u,d);
   u.setPassword(encoder.encode(d.password()));
   return dto(repo.save(u));
 }
 @Transactional public UsuarioDTO editar(Long id,UsuarioDTO d){
   Usuario u=obtener(id);
   if(!u.getUsername().equals(d.username()) && repo.existsByUsername(d.username()))
     throw new ResponseStatusException(HttpStatus.CONFLICT,"Username ya existe");
   u.setUsername(d.username());
   actualizar(u,d);
   if(d.password()!=null && !d.password().isBlank()) {
     if(d.password().length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La contraseña debe tener al menos 8 caracteres");
     u.setPassword(encoder.encode(d.password()));
   }
   return dto(repo.save(u));
 }
 @Transactional public void eliminar(Long id){repo.delete(obtener(id));}
 @Transactional(readOnly=true) public Usuario validar(String username,String password){
   Usuario u=repo.findByUsername(username).orElseThrow(this::loginFallido);
   if(!Boolean.TRUE.equals(u.getActivo()) || !encoder.matches(password,u.getPassword())) throw loginFallido();
   return u;
 }
 @Transactional(readOnly=true) public Usuario obtener(Long id){
   return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
 }
 @Transactional(readOnly=true) public Usuario buscarPorUsername(String username){
   return repo.findByUsername(username).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario no encontrado"));
 }
 private void actualizar(Usuario u,UsuarioDTO d){
   u.setNombres(d.nombres());u.setApellidos(d.apellidos());u.setEmail(d.email());
   u.setRol(d.rol());if(d.activo()!=null)u.setActivo(d.activo());
 }
 private UsuarioDTO dto(Usuario u){
   return new UsuarioDTO(u.getId(),u.getUsername(),null,u.getNombres(),u.getApellidos(),u.getEmail(),u.getRol(),u.getActivo());
 }
 private ResponseStatusException loginFallido(){return new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario o contrasena invalidos");}
}