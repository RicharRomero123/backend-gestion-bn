
package com.bn.gestion_indecopi_api.service;

import com.bn.gestion_indecopi_api.model.Auditoria;
import com.bn.gestion_indecopi_api.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository repository;

    public void registrar(String usuario, String accion, String entidad, Long id, String detalle) {
        Auditoria log = new Auditoria();
        log.setUsuario(usuario);
        log.setAccion(accion);
        log.setEntidad(entidad);
        log.setEntidadId(id);
        log.setDetalle(detalle);
        log.setFecha(LocalDateTime.now());
        repository.save(log);
    }

    public List<Auditoria> listarTodo() {
        return repository.findAll();
    }
}