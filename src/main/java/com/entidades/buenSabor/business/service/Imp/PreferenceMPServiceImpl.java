package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.PreferenceMPService;
import com.entidades.buenSabor.domain.entities.PreferenceMP;
import com.entidades.buenSabor.repositories.PreferenceMPRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PreferenceMPServiceImpl implements PreferenceMPService {

    private final PreferenceMPRepository preferenceMPRepository;

    public PreferenceMPServiceImpl(PreferenceMPRepository preferenceMPRepository) {
        this.preferenceMPRepository = preferenceMPRepository;
    }

    @Override
    public PreferenceMP findById(String id) {
        return preferenceMPRepository.findById(id).orElse(null);
    }

    @Override
    public List<PreferenceMP> findAll() {
        return preferenceMPRepository.findAll();
    }

    @Override
    public PreferenceMP save(PreferenceMP preferenceMP) {
        return preferenceMPRepository.save(preferenceMP);
    }

    @Override
    public PreferenceMP update(PreferenceMP preferenceMP) {
        // Verificar si la entidad existe en la base de datos
        if (preferenceMPRepository.existsById(preferenceMP.getId())) {
            return preferenceMPRepository.save(preferenceMP);
        } else {
            // Manejar el caso en el que la entidad no existe
            return null;
        }
    }

    @Override
    public void delete(String id) {
        preferenceMPRepository.deleteById(id);
    }

    @Override
    public PreferenceMP obtenerPorIdPedido(Long idPedido) {
        return preferenceMPRepository.findByIdPedido(idPedido)
                .orElseThrow(() -> new RuntimeException("PreferenceMP no encontrado para el pedido"));
    }
}

