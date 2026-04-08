package com.bn.gestion_indecopi_api.repository;

import com.bn.gestion_indecopi_api.model.Abono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {

    // Buscadores para el panel de filtros
    List<Abono> findBySolicitanteContainingIgnoreCase(String solicitante);

    List<Abono> findByClienteContainingIgnoreCase(String cliente);

    // Listar solo los abonos que aún tienen la constancia pendiente (útil para KPIs)
    List<Abono> findByConstanciaEntregadaFalse();
    
    // Buscar abonos por importes mayores a un monto específico
    List<Abono> findByImporteReclamadoGreaterThanEqual(Double monto);
    // ... (manten lo anterior y agrega estas consultas)

@Query("SELECT COUNT(a) FROM Abono a WHERE a.fechaIngreso = CURRENT_DATE")
long countIngresadosHoy();

@Query("SELECT COUNT(a) FROM Abono a WHERE a.constanciaEntregada = false")
long countPendientesConstancia();

@Query("SELECT COUNT(a) FROM Abono a WHERE a.fechaVencimiento < CURRENT_DATE AND a.constanciaEntregada = false")
long countVencidos();

@Query("SELECT SUM(a.importeReclamado + a.interesesLegales + a.costas) FROM Abono a")
Double sumTodoElDinero();
}
