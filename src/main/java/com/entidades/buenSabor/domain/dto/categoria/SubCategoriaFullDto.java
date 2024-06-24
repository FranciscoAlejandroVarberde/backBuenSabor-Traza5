package com.entidades.buenSabor.domain.dto.categoria;

import com.entidades.buenSabor.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class SubCategoriaFullDto extends BaseDto {
    private String denominacion;

    private boolean esInsumo;
}