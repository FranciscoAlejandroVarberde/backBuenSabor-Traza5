package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.business.service.CategoriaService;
import com.entidades.buenSabor.domain.dto.articulo.ArticuloDto;
import com.entidades.buenSabor.domain.dto.articuloInsumo.ArticuloInsumoFullDto;
import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoriaService.class, CategoriaMapper.class})
public interface ArticuloMapper extends BaseMapper<Articulo, ArticuloDto>{
    @Override
    Articulo toEntity(ArticuloDto source);
}