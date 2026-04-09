package com.bn.gestion_indecopi_api.service;

import com.bn.gestion_indecopi_api.model.ReclamoIndecopi;
import com.bn.gestion_indecopi_api.repository.ReclamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamoService {

    @Autowired
    private ReclamoRepository repository;

    @Autowired
    private AuditoriaService auditoriaService; // 1. Inyectamos la auditoría

    public List<ReclamoIndecopi> listarTodos() {
        return repository.findAll();
    }

    public Optional<ReclamoIndecopi> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    // 2. Método guardar actualizado con usuario
    public ReclamoIndecopi guardar(ReclamoIndecopi reclamo, String usuario) {
        ReclamoIndecopi guardado = repository.save(reclamo);
        
        // Registramos en auditoría
        auditoriaService.registrar(
            usuario, 
            "CREACION", 
            "INDECOPI", 
            guardado.getId(), 
            "Se ingresó reclamo Exp: " + guardado.getNroExpediente()
        );
        
        return guardado;
    }

    // 3. Método actualizar actualizado con usuario
    public ReclamoIndecopi actualizar(Long id, ReclamoIndecopi datosNuevos, String usuario) {
        return repository.findById(id).map(registro -> {
            
            String detalle = "Actualización de expediente: " + registro.getNroExpediente() + ". ";
            
            // Verificamos cambios de estado para el detalle de auditoría
            if (!registro.getEstadoInforme().equals(datosNuevos.getEstadoInforme())) {
                detalle += "Cambio de informe a: " + datosNuevos.getEstadoInforme() + ". ";
            }

            // Actualizamos los campos
            registro.setNroExpediente(datosNuevos.getNroExpediente());
            registro.setSolicitadoPor(datosNuevos.getSolicitadoPor());
            registro.setDatosCliente(datosNuevos.getDatosCliente());
            registro.setCanal(datosNuevos.getCanal());
            registro.setSolicitudRealizada(datosNuevos.isSolicitudRealizada());
            registro.setEstadoInforme(datosNuevos.getEstadoInforme());
            registro.setEstadoNotificacion(datosNuevos.getEstadoNotificacion());
            
            ReclamoIndecopi actualizado = repository.save(registro);

            // Registramos en auditoría
            auditoriaService.registrar(usuario, "EDICION", "INDECOPI", actualizado.getId(), detalle);

            return actualizado;
        }).orElseThrow(() -> new RuntimeException("No se encontró el reclamo con ID: " + id));
    }

    // 4. Método eliminar actualizado con usuario
    public void eliminar(Long id, String usuario) {
        ReclamoIndecopi existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));

        repository.deleteById(id);

        // Registramos en auditoría
        auditoriaService.registrar(
            usuario, 
            "ELIMINACION", 
            "INDECOPI", 
            id, 
            "Eliminó expediente: " + existente.getNroExpediente() + " del cliente: " + existente.getDatosCliente()
        );
    }
}