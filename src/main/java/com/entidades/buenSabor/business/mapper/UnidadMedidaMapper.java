package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.unidadMedida.UnidadMedidaFullDto;
import com.entidades.buenSabor.domain.entities.UnidadMedida;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnidadMedidaMapper extends BaseMapper<UnidadMedida, UnidadMedidaFullDto> {
}
