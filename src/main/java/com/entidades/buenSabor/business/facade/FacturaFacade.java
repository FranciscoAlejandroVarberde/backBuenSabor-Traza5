package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.factura.FacturaFullDto;
import com.entidades.buenSabor.domain.entities.Factura;

public interface FacturaFacade extends BaseFacade<FacturaFullDto, Long> {

    Factura crearFactura(Long pedidoId);
}
