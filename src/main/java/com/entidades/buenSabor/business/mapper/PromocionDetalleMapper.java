package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.dto.promocionDetalle.PromocionDetalleFullDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.entities.PromocionDetalle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface PromocionDetalleMapper extends BaseMapper<PromocionDetalle, PromocionDetalleFullDto>{
}
