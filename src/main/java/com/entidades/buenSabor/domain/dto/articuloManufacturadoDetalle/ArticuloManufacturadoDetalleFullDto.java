package com.entidades.buenSabor.domain.dto.articuloManufacturadoDetalle;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.dto.articuloInsumo.ArticuloInsumoFullDto;
import com.entidades.buenSabor.domain.dto.articuloInsumo.ArticuloInsumoShortDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticuloManufacturadoDetalleFullDto extends BaseDto {
    private Integer cantidad;
    private ArticuloInsumoShortDto articuloInsumo;
}
