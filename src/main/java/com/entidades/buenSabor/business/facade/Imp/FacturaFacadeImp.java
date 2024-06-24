package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.FacturaFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.FacturaService;
import com.entidades.buenSabor.domain.dto.factura.FacturaFullDto;
import com.entidades.buenSabor.domain.entities.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaFacadeImp extends BaseFacadeImp<Factura, FacturaFullDto, Long> implements FacturaFacade {

    @Autowired
    private FacturaService facturaService;


    public FacturaFacadeImp(BaseService<Factura, Long> baseService, BaseMapper<Factura, FacturaFullDto> baseMapper) {
        super(baseService, baseMapper);
    }

    @Override
    public Factura crearFactura(Long pedidoId) {
        return facturaService.crearFactura(pedidoId);
    }

}
