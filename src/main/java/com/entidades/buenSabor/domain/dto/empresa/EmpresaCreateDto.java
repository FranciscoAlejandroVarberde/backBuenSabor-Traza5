package com.entidades.buenSabor.domain.dto.empresa;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.dto.imagen.ImagenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmpresaCreateDto extends BaseDto {

    private String nombre;
    private String razonSocial;
    private Long cuil;
    private Set<ImagenDto> imagenes;
}

