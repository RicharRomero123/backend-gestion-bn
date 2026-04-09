package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.model.Usuario;
import com.bn.gestion_indecopi_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listarTodos();
    }

    @PostMapping
    // Solo el ADMIN puede crear otros usuarios
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario crear(@RequestBody Usuario usuario, @RequestHeader("X-User-Operador") String ejecutor) {
        return service.guardar(usuario, ejecutor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id, @RequestHeader("X-User-Operador") String ejecutor) {
        service.eliminar(id, ejecutor);
    }
}