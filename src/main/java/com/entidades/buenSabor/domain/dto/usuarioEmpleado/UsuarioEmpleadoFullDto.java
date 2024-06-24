package com.entidades.buenSabor.domain.dto.usuarioEmpleado;

import com.entidades.buenSabor.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioEmpleadoFullDto extends BaseDto {
    private String auth0id;
    private String username;
}
