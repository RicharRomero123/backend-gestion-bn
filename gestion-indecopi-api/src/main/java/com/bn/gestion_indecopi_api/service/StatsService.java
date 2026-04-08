package com.bn.gestion_indecopi_api.service;

import com.bn.gestion_indecopi_api.dto.AbonoStatsDTO;
import com.bn.gestion_indecopi_api.dto.DashboardStatsDTO;
import com.bn.gestion_indecopi_api.repository.ReclamoRepository;
import com.bn.gestion_indecopi_api.repository.AbonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class StatsService {

    @Autowired private ReclamoRepository reclamoRepo;
    @Autowired private AbonoRepository abonoRepo;

    public DashboardStatsDTO getReclamoStats() {
        LocalDate mañana = LocalDate.now().plusDays(1);
        
        return new DashboardStatsDTO(
            reclamoRepo.count(),
            reclamoRepo.countIngresadosHoy(),
            reclamoRepo.countCriticos(mañana),
            reclamoRepo.countVencidos(),
            reclamoRepo.countCompletados()
        );
    }
    // ... (manten lo de reclamos y agrega esto)

public AbonoStatsDTO getAbonoStats() {
    Double totalDinero = abonoRepo.sumTodoElDinero();
    
    return new AbonoStatsDTO(
        abonoRepo.count(),
        abonoRepo.countIngresadosHoy(),
        abonoRepo.countPendientesConstancia(),
        abonoRepo.countVencidos(),
        totalDinero != null ? totalDinero : 0.0
    );
}
    // Aquí puedes añadir un método similar para getAbonoStats()
}