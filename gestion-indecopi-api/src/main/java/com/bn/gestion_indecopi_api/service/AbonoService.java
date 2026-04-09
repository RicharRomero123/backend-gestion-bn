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

    @Autowired
    private AuditoriaService auditoriaService; // 1. INYECTAMOS EL SERVICIO DE AUDITORÍA

    // 2. ACTUALIZAMOS EL MÉTODO PARA RECIBIR EL USUARIO
    public Abono guardar(Abono abono, String usuario) {
        if (abono.getInteresesLegales() != null && abono.getInteresesLegales() > 0) {
            abono.setFechaUpdateIntereses(LocalDate.now());
            abono.setFechaVencimiento(calcularDiasHabiles(LocalDate.now(), 3));
            if (abono.getNotaAbonoInteres() == null) {
                abono.setNotaAbonoInteres("PENDIENTE");
            }
        } else {
            abono.setFechaVencimiento(calcularDiasHabiles(abono.getFechaIngreso(), 3));
        }

        Abono guardado = repository.save(abono);

        // 3. REGISTRAMOS LA CREACIÓN
        auditoriaService.registrar(
            usuario, 
            "CREACION", 
            "ABONOS", 
            guardado.getId(), 
            "Ingreso de nuevo abono para el cliente: " + guardado.getCliente()
        );

        return guardado;
    }

    // 4. ACTUALIZAMOS ACTUALIZAR PARA RECIBIR EL USUARIO
    public Abono actualizar(Long id, Abono datosNuevos, String usuario) {
        return repository.findById(id).map(existente -> {
            
            String detalle = "Actualización de datos. ";
            
            // Lógica de intereses...
            double intPrevios = (existente.getInteresesLegales() != null) ? existente.getInteresesLegales() : 0.0;
            double intNuevos = (datosNuevos.getInteresesLegales() != null) ? datosNuevos.getInteresesLegales() : 0.0;

            if (intPrevios <= 0 && intNuevos > 0) {
                existente.setFechaUpdateIntereses(LocalDate.now());
                existente.setFechaVencimiento(calcularDiasHabiles(LocalDate.now(), 3));
                existente.setNotaAbonoInteres("PENDIENTE");
                detalle += "Se añadieron intereses. ";
            } 
            else if (intNuevos <= 0 && intPrevios > 0) {
                existente.setFechaVencimiento(calcularDiasHabiles(existente.getFechaIngreso(), 3));
                existente.setNotaAbonoInteres(null);
                detalle += "Se eliminaron intereses. ";
            }

            if (datosNuevos.isConstanciaEntregada() && !existente.isConstanciaEntregada()) {
                existente.setFechaEntregaConstancia(LocalDate.now());
                detalle += "Constancia entregada. ";
            } 

            // Actualización de campos
            existente.setNotaAbonoInteres(datosNuevos.getNotaAbonoInteres());
            existente.setEnviadoLegalInteres(datosNuevos.isEnviadoLegalInteres());
            existente.setSolicitante(datosNuevos.getSolicitante());
            existente.setCliente(datosNuevos.getCliente());
            existente.setImporteReclamado(datosNuevos.getImporteReclamado());
            existente.setInteresesLegales(intNuevos);
            existente.setCostas(datosNuevos.getCostas());
            existente.setConstanciaEntregada(datosNuevos.isConstanciaEntregada());

            Abono actualizado = repository.save(existente);

            // 5. REGISTRAMOS LA EDICIÓN
            auditoriaService.registrar(
                usuario, 
                "EDICION", 
                "ABONOS", 
                actualizado.getId(), 
                detalle
            );

            return actualizado;
        }).orElseThrow(() -> new RuntimeException("Abono no encontrado con ID: " + id));
    }

    // 6. ACTUALIZAMOS ELIMINAR PARA RECIBIR EL USUARIO
    public void eliminar(Long id, String usuario) {
        Abono existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el abono"));
        
        repository.deleteById(id);

        // 7. REGISTRAMOS LA ELIMINACIÓN
        auditoriaService.registrar(
            usuario, 
            "ELIMINACION", 
            "ABONOS", 
            id, 
            "Se eliminó el registro del cliente: " + existente.getCliente()
        );
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
}