package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaCreateDto;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaFullDto;

import java.util.List;

public interface CategoriaFacade extends BaseFacade<CategoriaCreateDto, Long> {
    List<CategoriaCreateDto> categoriaSucursal(Long idSucursal);
    List<CategoriaCreateDto> categoriaInsumoSucursal(Long idSucursal);
    List<CategoriaCreateDto> categoriaManufacturadoSucursal(Long idSucursal);

}
