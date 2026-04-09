package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.model.Auditoria;
import com.bn.gestion_indecopi_api.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')") // Solo el admin ve los logs
public class AuditoriaController {

    @Autowired
    private AuditoriaService service;

    @GetMapping
    public List<Auditoria> verHistorial() {
        return service.listarTodo();
    }
}