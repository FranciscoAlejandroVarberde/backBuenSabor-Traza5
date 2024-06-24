package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.UsuarioFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.UsuarioService;
import com.entidades.buenSabor.domain.dto.usuarioCliente.UsuarioDto;
import com.entidades.buenSabor.domain.entities.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioFacadeImp extends BaseFacadeImp<Usuario, UsuarioDto, Long> implements UsuarioFacade {

    private UsuarioService usuarioService;

    public UsuarioFacadeImp(BaseService<Usuario, Long> baseService, BaseMapper<Usuario, UsuarioDto> baseMapper, UsuarioService usuarioService) {
        super(baseService, baseMapper);
        this.usuarioService = usuarioService;
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        return this.usuarioService.obtenerUsuarioPorEmail(email);
    }
}
