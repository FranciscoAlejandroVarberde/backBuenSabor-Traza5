package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.articuloManufacturadoDetalle.ArticuloManufacturadoDetalleFullDto;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturadoDetalle;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel =  "spring", uses = LocalidadMapper.class)
public interface ArticuloManufacturadoDetalleMapper extends BaseMapper<ArticuloManufacturadoDetalle, ArticuloManufacturadoDetalleFullDto> {

    @Mapping(source = "articuloInsumo.id", target="articuloInsumo.id")
    ArticuloManufacturadoDetalleFullDto toDTO(ArticuloManufacturadoDetalle source);

    @InheritInverseConfiguration
    ArticuloManufacturadoDetalle toEntity(ArticuloManufacturadoDetalleFullDto source);

}
