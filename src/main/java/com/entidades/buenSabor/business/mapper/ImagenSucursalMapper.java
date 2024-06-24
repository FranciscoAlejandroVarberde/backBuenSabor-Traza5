package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.imagen.ImagenDto;
import com.entidades.buenSabor.domain.entities.ImagenEmpresa;
import com.entidades.buenSabor.domain.entities.ImagenSucursal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImagenSucursalMapper extends BaseMapper<ImagenSucursal, ImagenDto> {
}
