package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.CategoriaFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.CategoriaService;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaCreateDto;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaFullDto;
import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaFacadeImp extends BaseFacadeImp<Categoria, CategoriaCreateDto, Long> implements CategoriaFacade {
    @Autowired

    private CategoriaService categoriaService;
    public CategoriaFacadeImp(BaseService<Categoria, Long> baseService, BaseMapper<Categoria, CategoriaCreateDto> baseMapper) {
        super(baseService, baseMapper);
    }
    public List<CategoriaCreateDto> categoriaSucursal(Long idSucursal) {
        return this.categoriaService.categoriaSucursal(idSucursal);
    }
    public List<CategoriaCreateDto> categoriaInsumoSucursal(Long idSucursal) {
        return this.categoriaService.categoriaInsumoSucursal(idSucursal);
    }
    public List<CategoriaCreateDto> categoriaManufacturadoSucursal(Long idSucursal) {
        return this.categoriaService.categoriaManufacturadoSucursal(idSucursal);
    }
}
