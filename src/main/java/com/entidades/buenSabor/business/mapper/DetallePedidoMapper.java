package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.detallePedido.DetallePedidoFullDto;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ArticuloMapper.class })
public interface DetallePedidoMapper extends BaseMapper<DetallePedido, DetallePedidoFullDto>{
}
