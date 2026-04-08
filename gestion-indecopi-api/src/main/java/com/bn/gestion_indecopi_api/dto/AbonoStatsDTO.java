package com.bn.gestion_indecopi_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbonoStatsDTO {
    private long total;
    private long ingresadosHoy;
    private long pendientesConstancia; // Constancia = NO
    private long vencidos; // Pasaron 3 días y sigue sin constancia
    private Double montoTotalAcumulado; // Suma de Importe + Intereses + Costas
}