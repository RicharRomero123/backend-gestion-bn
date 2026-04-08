package com.bn.gestion_indecopi_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDTO {
    private long total;
    private long ingresadosHoy;
    private long criticos; // Faltan 0 o 1 día
    private long vencidos;  // Ya pasaron los 3 días
    private long completados; // Ya están ENVIADOS
}