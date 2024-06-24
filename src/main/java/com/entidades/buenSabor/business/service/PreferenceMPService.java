package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.domain.entities.PreferenceMP;

import java.util.List;

public interface PreferenceMPService{

    PreferenceMP findById(String id);

    List<PreferenceMP> findAll();

    PreferenceMP save(PreferenceMP preferenceMP);

    PreferenceMP update(PreferenceMP preferenceMP);

    void delete(String id);

    PreferenceMP obtenerPorIdPedido(Long idPedido);
}
