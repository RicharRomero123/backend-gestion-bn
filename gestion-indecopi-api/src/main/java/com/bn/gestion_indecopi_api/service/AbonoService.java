package com.bn.gestion_indecopi_api.service;

import com.bn.gestion_indecopi_api.model.Abono;
import com.bn.gestion_indecopi_api.repository.AbonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class AbonoService {

    @Autowired
    private AbonoRepository repository;

    public Abono guardar(Abono abono) {
        // Al guardar uno nuevo, verificamos si ya nace con intereses
        if (abono.getInteresesLegales() != null && abono.getInteresesLegales() > 0) {
            abono.setFechaUpdateIntereses(LocalDate.now());
            abono.setFechaVencimiento(calcularDiasHabiles(LocalDate.now(), 3));
            // Si tiene interés, la nota de interés nace como PENDIENTE por defecto
            if (abono.getNotaAbonoInteres() == null) {
                abono.setNotaAbonoInteres("PENDIENTE");
            }
        } else {
            abono.setFechaVencimiento(calcularDiasHabiles(abono.getFechaIngreso(), 3));
        }
        return repository.save(abono);
    }

    public Abono actualizar(Long id, Abono datosNuevos) {
        return repository.findById(id).map(existente -> {
            
            // 1. Gestión de Intereses y Plazos
            double intPrevios = (existente.getInteresesLegales() != null) ? existente.getInteresesLegales() : 0.0;
            double intNuevos = (datosNuevos.getInteresesLegales() != null) ? datosNuevos.getInteresesLegales() : 0.0;

            if (intPrevios <= 0 && intNuevos > 0) {
                // Si se añaden intereses ahora, reiniciamos el plazo de 3 días desde HOY
                existente.setFechaUpdateIntereses(LocalDate.now());
                existente.setFechaVencimiento(calcularDiasHabiles(LocalDate.now(), 3));
                // Activamos el flujo de nota de interés como PENDIENTE
                existente.setNotaAbonoInteres("PENDIENTE");
            } 
            else if (intNuevos <= 0) {
                // Si se quitan los intereses, el plazo vuelve al original
                existente.setFechaVencimiento(calcularDiasHabiles(existente.getFechaIngreso(), 3));
                existente.setNotaAbonoInteres(null); // Opcional: limpiar si no hay intereses
            }

            // 2. Gestión del Estado "ATENDIDO" (Constancia)
            if (datosNuevos.isConstanciaEntregada() && !existente.isConstanciaEntregada()) {
                existente.setFechaEntregaConstancia(LocalDate.now());
            } 
            else if (!datosNuevos.isConstanciaEntregada()) {
                existente.setFechaEntregaConstancia(null);
            }

            // ============================================================
            // 3. ACTUALIZACIÓN DE LOS NUEVOS CAMPOS (LO QUE FALTABA)
            // ============================================================
            existente.setNotaAbonoInteres(datosNuevos.getNotaAbonoInteres());
            existente.setEnviadoLegalInteres(datosNuevos.isEnviadoLegalInteres());
            // ============================================================

            // 4. Actualización de campos generales
            existente.setSolicitante(datosNuevos.getSolicitante());
            existente.setCliente(datosNuevos.getCliente());
            existente.setImporteReclamado(datosNuevos.getImporteReclamado());
            existente.setInteresesLegales(intNuevos);
            existente.setCostas(datosNuevos.getCostas());
            existente.setConstanciaEntregada(datosNuevos.isConstanciaEntregada());

            return repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Abono no encontrado con ID: " + id));
    }

    private LocalDate calcularDiasHabiles(LocalDate inicio, int diasASumar) {
        LocalDate fecha = inicio;
        int diasContados = 0;
        while (diasContados < diasASumar) {
            fecha = fecha.plusDays(1);
            if (!(fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                diasContados++;
            }
        }
        return fecha;
    }

    public List<Abono> listarTodos() { return repository.findAll(); }
    public void eliminar(Long id) { repository.deleteById(id); }
}