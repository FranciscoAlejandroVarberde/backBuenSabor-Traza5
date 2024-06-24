package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.imagen.ImagenDto;
import com.entidades.buenSabor.domain.entities.ImagenPromocion;
import com.entidades.buenSabor.domain.entities.ImagenSucursal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ImagenPromocionMapper  extends BaseMapper<ImagenPromocion, ImagenDto> {
}
