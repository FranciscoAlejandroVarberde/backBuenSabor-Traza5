package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaCreateDto;
import com.entidades.buenSabor.domain.entities.Categoria;

import java.util.List;

public interface CategoriaService extends BaseService<Categoria, Long> {
    List<CategoriaCreateDto> categoriaSucursal(Long idSucursal);
    List<CategoriaCreateDto> categoriaInsumoSucursal(Long idSucursal);
    List<CategoriaCreateDto> categoriaManufacturadoSucursal(Long idSucursal);

}
