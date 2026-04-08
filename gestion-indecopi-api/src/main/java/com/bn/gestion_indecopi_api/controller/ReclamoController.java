package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.model.ReclamoIndecopi;
import com.bn.gestion_indecopi_api.service.ReclamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // IMPORTANTE: Para activar las validaciones
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reclamos")
@Tag(name = "Reclamos Indecopi", description = "Gestión de seguimiento de reclamos administrativos")
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
    // Añadimos @Valid para validar el nroExpediente y campos obligatorios
    public ResponseEntity<ReclamoIndecopi> guardar(@Valid @RequestBody ReclamoIndecopi reclamo) {
        return ResponseEntity.ok(service.guardar(reclamo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos básicos de un reclamo")
    // También validamos en la actualización
    public ResponseEntity<ReclamoIndecopi> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ReclamoIndecopi reclamo
    ) {
        try {
            ReclamoIndecopi actualizado = service.actualizar(id, reclamo);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reclamo por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}