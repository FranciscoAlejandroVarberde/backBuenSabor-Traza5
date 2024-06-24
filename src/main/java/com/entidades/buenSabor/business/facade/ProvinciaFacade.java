package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.provincia.ProvinciaFullDto;

import java.util.List;

public interface ProvinciaFacade extends BaseFacade<ProvinciaFullDto, Long> {
    List<ProvinciaFullDto> findByPaisId(Long id);
}
