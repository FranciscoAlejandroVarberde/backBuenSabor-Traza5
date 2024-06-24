package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.usuarioCliente.UsuarioDto;
import com.entidades.buenSabor.domain.entities.Usuario;

public interface UsuarioFacade extends BaseFacade<UsuarioDto, Long> {

    Usuario obtenerUsuarioPorEmail(String email);

}
