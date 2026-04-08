package com.bn.gestion_indecopi_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "abonos")
@Data
public class Abono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String solicitante;
    private String cliente;
    private Double importeReclamado;
    private Double interesesLegales; 
    private Double costas;
    
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;
    private LocalDate fechaUpdateIntereses;
    
    // --- FLUJO ESPECÍFICO DE INTERESES ---
    private String notaAbonoInteres = "PENDIENTE"; // PENDIENTE, ATENDIDO
    private boolean enviadoLegalInteres = false; 
    
    private boolean constanciaEntregada;
    private LocalDate fechaEntregaConstancia;

    // Método de ayuda para saber si tiene intereses
    public boolean tieneIntereses() {
        return interesesLegales != null && interesesLegales > 0;
    }
}