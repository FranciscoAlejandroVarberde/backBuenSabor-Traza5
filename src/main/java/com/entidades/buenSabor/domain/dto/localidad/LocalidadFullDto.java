package com.entidades.buenSabor.domain.dto.localidad;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.dto.provincia.ProvinciaFullDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocalidadFullDto extends BaseDto {

    private String nombre;
    private ProvinciaFullDto provincia;
}
