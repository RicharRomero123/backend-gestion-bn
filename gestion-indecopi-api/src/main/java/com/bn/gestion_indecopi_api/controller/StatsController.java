package com.bn.gestion_indecopi_api.controller;

import com.bn.gestion_indecopi_api.dto.AbonoStatsDTO;
import com.bn.gestion_indecopi_api.dto.DashboardStatsDTO;
import com.bn.gestion_indecopi_api.service.StatsService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:3000")
public class StatsController {

    @Autowired
    private StatsService service;

    @GetMapping("/reclamos")
    public DashboardStatsDTO getReclamoStats() {
        return service.getReclamoStats();
    }
    // ...

@GetMapping("/abonos")
@Operation(summary = "Obtener estadísticas financieras de abonos")
public AbonoStatsDTO getAbonoStats() {
    return service.getAbonoStats();
}
}