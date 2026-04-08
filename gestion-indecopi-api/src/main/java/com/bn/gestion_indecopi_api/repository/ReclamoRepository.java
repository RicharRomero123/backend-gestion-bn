package com.bn.gestion_indecopi_api.repository;

import com.bn.gestion_indecopi_api.model.ReclamoIndecopi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReclamoRepository extends JpaRepository<ReclamoIndecopi, Long> {

    // Permite buscar coincidencias parciales ignorando mayúsculas/minúsculas
    List<ReclamoIndecopi> findBySolicitadoPorContainingIgnoreCase(String solicitadoPor);

    List<ReclamoIndecopi> findByDatosClienteContainingIgnoreCase(String datosCliente);
    
    // Para auditoría rápida de casos críticos (vencidos o por vencer)
    List<ReclamoIndecopi> findBySolicitudRealizadaFalse();
    @Query("SELECT COUNT(r) FROM ReclamoIndecopi r WHERE r.fechaRecepcion = CURRENT_DATE")
long countIngresadosHoy();

@Query("SELECT COUNT(r) FROM ReclamoIndecopi r WHERE r.fechaVencimiento < CURRENT_DATE AND (r.estadoInforme != 'ENVIADO' OR r.estadoNotificacion != 'ENVIADO')")
long countVencidos();

@Query("SELECT COUNT(r) FROM ReclamoIndecopi r WHERE r.fechaVencimiento >= CURRENT_DATE AND r.fechaVencimiento <= :mañana AND (r.estadoInforme != 'ENVIADO' OR r.estadoNotificacion != 'ENVIADO')")
long countCriticos(LocalDate mañana);

@Query("SELECT COUNT(r) FROM ReclamoIndecopi r WHERE r.estadoInforme = 'ENVIADO' AND r.estadoNotificacion = 'ENVIADO' AND r.solicitudRealizada = true")
long countCompletados();
}