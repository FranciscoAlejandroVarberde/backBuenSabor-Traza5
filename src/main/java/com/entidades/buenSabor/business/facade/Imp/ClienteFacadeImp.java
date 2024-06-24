package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.ClienteFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.ClienteService;
import com.entidades.buenSabor.domain.dto.cliente.ClienteFullDto;
import com.entidades.buenSabor.domain.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteFacadeImp extends BaseFacadeImp<Cliente, ClienteFullDto, Long> implements ClienteFacade {

    @Autowired
    private ClienteService clienteService;

    public ClienteFacadeImp(BaseService<Cliente, Long> baseService, BaseMapper<Cliente, ClienteFullDto> baseMapper) {
        super(baseService, baseMapper);
    }

    public Cliente findByEmail(String email) {
        return this.clienteService.findByEmail(email);
    }

}
