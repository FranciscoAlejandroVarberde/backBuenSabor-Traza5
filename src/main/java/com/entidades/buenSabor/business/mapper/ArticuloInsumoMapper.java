package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.articuloInsumo.ArticuloInsumoFullDto;
import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel =  "spring", uses = LocalidadMapper.class)
public interface ArticuloInsumoMapper extends BaseMapper<ArticuloInsumo, ArticuloInsumoFullDto> {
    List<ArticuloInsumoFullDto> insumosToInsumoFullDtos(List<ArticuloInsumo> insumos);

}
