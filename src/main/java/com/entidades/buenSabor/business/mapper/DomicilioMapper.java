package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.domicilio.DomicilioFullDto;
import com.entidades.buenSabor.domain.entities.Domicilio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LocalidadMapper.class)
public interface DomicilioMapper extends BaseMapper<Domicilio, DomicilioFullDto> {


}
