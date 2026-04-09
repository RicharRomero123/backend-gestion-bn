package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.model.Abono;
import com.bn.gestion_indecopi_api.service.AbonoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abonos")
@Tag(name = "Gestion de Abonos", description = "Endpoints para el control de abonos y constancias con auditoría")
@CrossOrigin(origins = "http://localhost:3000")
public class AbonoController {

    @Autowired
    private AbonoService service;

    @GetMapping
    @Operation(summary = "Listar todos los abonos")
    public List<Abono> listar() {
        return service.listarTodos();
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo abono")
    // Recibimos quién opera desde el Header enviado por Next.js
    public ResponseEntity<Abono> guardar(
            @RequestBody Abono abono, 
            @RequestHeader("X-User-Operador") String usuario) {
        
        return ResponseEntity.ok(service.guardar(abono, usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar abono y recalcular plazos por intereses")
    public ResponseEntity<Abono> actualizar(
            @PathVariable Long id, 
            @RequestBody Abono abono,
            @RequestHeader("X-User-Operador") String usuario) {
        try {
            // Pasamos el ID, los datos y el usuario responsable
            Abono actualizado = service.actualizar(id, abono, usuario);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un abono por ID")
    // Solo el ADMIN debería poder eliminar registros, para mayor seguridad de auditoría
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader("X-User-Operador") String usuario) {
        
        service.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}