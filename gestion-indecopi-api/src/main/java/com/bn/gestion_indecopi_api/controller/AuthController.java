package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.model.Usuario;
import com.bn.gestion_indecopi_api.model.LoginRequest; // El nuevo DTO
import com.bn.gestion_indecopi_api.model.AuthResponse; // El nuevo DTO
import com.bn.gestion_indecopi_api.repository.UsuarioRepository;
import com.bn.gestion_indecopi_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        // Buscamos al usuario
        Usuario user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validamos la contraseña con BCrypt
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Generamos el Token
            String token = jwtUtil.generarToken(user.getUsername());
            
            // Obtenemos el rol principal (asumiendo que tiene al menos uno)
            String rol = user.getRoles().iterator().next().getNombre();
            
            // Retornamos el objeto de respuesta
            return new AuthResponse(token, rol);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}