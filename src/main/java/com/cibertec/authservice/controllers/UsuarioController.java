package com.cibertec.authservice.controllers;
import com.cibertec.authservice.models.UsuarioDTO;
import com.cibertec.authservice.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
 private final UsuarioService service;
 public UsuarioController(UsuarioService service){this.service=service;}
 @GetMapping public List<UsuarioDTO> listar(){return service.listar();}
 @GetMapping("/{id}") public UsuarioDTO buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));}
 @PutMapping("/{id}") public UsuarioDTO editar(@PathVariable Long id,@Valid @RequestBody UsuarioDTO dto){return service.editar(id,dto);}
 @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){service.eliminar(id);return ResponseEntity.noContent().build();}
}