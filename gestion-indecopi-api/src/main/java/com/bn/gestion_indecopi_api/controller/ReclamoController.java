package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.model.ReclamoIndecopi;
import com.bn.gestion_indecopi_api.service.ReclamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importante para roles
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reclamos")
@Tag(name = "Reclamos Indecopi", description = "Gestión de seguimiento de reclamos con auditoría activa")
@CrossOrigin(origins = "http://localhost:3000")
public class ReclamoController {

    @Autowired
    private ReclamoService service;

    @GetMapping
    @Operation(summary = "Listar todos los reclamos")
    public List<ReclamoIndecopi> listar() {
        return service.listarTodos();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo reclamo")
    public ResponseEntity<ReclamoIndecopi> guardar(
            @Valid @RequestBody ReclamoIndecopi reclamo,
            @RequestHeader("X-User-Operador") String usuario // <--- Capturamos el operador
    ) {
        // Pasamos el objeto y el usuario al service
        return ResponseEntity.ok(service.guardar(reclamo, usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos básicos de un reclamo")
    public ResponseEntity<ReclamoIndecopi> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ReclamoIndecopi reclamo,
            @RequestHeader("X-User-Operador") String usuario // <--- Capturamos el operador
    ) {
        try {
            // Pasamos ID, datos nuevos y el usuario responsable
            ReclamoIndecopi actualizado = service.actualizar(id, reclamo, usuario);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reclamo por ID")
    // Solo el administrador puede borrar expedientes para evitar pérdida de información
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader("X-User-Operador") String usuario // <--- Capturamos el operador
    ) {
        service.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}