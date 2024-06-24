package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.categoria.CategoriaCreateDto;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaFullDto;
import com.entidades.buenSabor.domain.dto.cliente.ClienteFullDto;
import com.entidades.buenSabor.domain.entities.Categoria;
import com.entidades.buenSabor.domain.entities.Cliente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SucursalMapper.class})
public interface CategoriaMapper extends BaseMapper<Categoria, CategoriaCreateDto>{
    List<CategoriaCreateDto> categoriasToCategoriaCreateDto(List<Categoria> categorias);

}
