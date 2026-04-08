package com.bn.gestion_indecopi_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reclamos_indecopi")
@Data
public class ReclamoIndecopi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NUEVO CAMPO OBLIGATORIO Y UNICO
    @NotBlank(message = "El número de expediente es obligatorio")
    @Column(nullable = false, unique = true)
    private String nroExpediente;

    @NotBlank(message = "El solicitante no puede estar vacío")
    private String solicitadoPor;

    @NotBlank(message = "Los datos del cliente son obligatorios")
    private String datosCliente;

    @Column(nullable = false)
    private String canal;

    @Column(nullable = false)
    private LocalDate fechaRecepcion;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    private boolean solicitudRealizada;
    
    private String estadoInforme = "NO ENVIADO";
    private LocalDateTime fechaUpdateInforme;
    
    private String estadoNotificacion = "NO ENVIADO";
    private LocalDateTime fechaUpdateNotificacion;
}