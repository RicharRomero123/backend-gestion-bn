package com.bn.gestion_indecopi_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que realizó la operación (ej: "rromero")
    @Column(nullable = false)
    private String usuario;

    // Acción realizada: "CREACION", "EDICION", "ELIMINACION"
    @Column(nullable = false)
    private String accion;

    // Tabla o módulo afectado: "ABONOS", "INDECOPI", "USUARIOS"
    @Column(nullable = false)
    private String entidad;

    // El ID del registro que se tocó
    private Long entidadId;

    // Descripción legible de lo que pasó
    // Ej: "Se cambió el interés legal de S/ 10.00 a S/ 50.00"
    @Column(length = 1000)
    private String detalle;

    // Fecha y hora exacta del movimiento
    private LocalDateTime fecha;

    // Constructor vacío requerido por JPA
    public Auditoria() {}

    // Constructor útil para registrar rápido
    public Auditoria(String usuario, String accion, String entidad, Long entidadId, String detalle) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.detalle = detalle;
        this.fecha = LocalDateTime.now();
    }
}