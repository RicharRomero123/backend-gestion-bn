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

    public List<ReclamoIndecopi> listarTodos() {
        return repository.findAll();
    }

    public Optional<ReclamoIndecopi> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public ReclamoIndecopi guardar(ReclamoIndecopi reclamo) {
        return repository.save(reclamo);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    // Dentro de la clase ReclamoService, actualiza el método:

public ReclamoIndecopi actualizar(Long id, ReclamoIndecopi datosNuevos) {
    return repository.findById(id).map(registro -> {
        // Actualizamos el número de expediente
        registro.setNroExpediente(datosNuevos.getNroExpediente());
        
        registro.setSolicitadoPor(datosNuevos.getSolicitadoPor());
        registro.setDatosCliente(datosNuevos.getDatosCliente());
        registro.setCanal(datosNuevos.getCanal());
        registro.setSolicitudRealizada(datosNuevos.isSolicitudRealizada());
        registro.setEstadoInforme(datosNuevos.getEstadoInforme());
        registro.setEstadoNotificacion(datosNuevos.getEstadoNotificacion());
        
        return repository.save(registro);
    }).orElseThrow(() -> new RuntimeException("No se encontró el reclamo con ID: " + id));
}
}