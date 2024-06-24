package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.localidad.LocalidadFullDto;
import com.entidades.buenSabor.domain.entities.Localidad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocalidadMapper extends BaseMapper<Localidad, LocalidadFullDto> {
}
