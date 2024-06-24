package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.localidad.LocalidadFullDto;

import java.util.List;

public interface LocalidadFacade extends BaseFacade<LocalidadFullDto, Long> {

    List<LocalidadFullDto> findByProvinciaId(Long id);
}
