package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.promocion.PromocionFullDto;
import com.entidades.buenSabor.domain.dto.sucursal.SucursalFullDto;
import com.entidades.buenSabor.domain.entities.Promocion;
import com.entidades.buenSabor.domain.entities.Sucursal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SucursalMapper.class})
public interface PromocionMapper extends BaseMapper<Promocion, PromocionFullDto>{
    List<PromocionFullDto> promocionesToPromocionFullDto(List<Promocion> promociones);

}
