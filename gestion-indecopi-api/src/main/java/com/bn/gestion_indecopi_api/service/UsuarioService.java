package com.bn.gestion_indecopi_api.service;

import com.bn.gestion_indecopi_api.model.Usuario;
import com.bn.gestion_indecopi_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditoriaService auditoria;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario guardar(Usuario usuario, String ejecutor) {
        // Encriptamos la clave antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario nuevoUsuario = repository.save(usuario);

        // REGISTRO DE AUDITORÍA
        auditoria.registrar(ejecutor, "CREACION", "USUARIO", nuevoUsuario.getId(), 
                "Se creó el usuario: " + nuevoUsuario.getUsername() + " con roles.");
        
        return nuevoUsuario;
    }

    public void eliminar(Long id, String ejecutor) {
        Usuario u = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        auditoria.registrar(ejecutor, "ELIMINACION", "USUARIO", id, "Eliminó al usuario: " + u.getUsername());
    }
}