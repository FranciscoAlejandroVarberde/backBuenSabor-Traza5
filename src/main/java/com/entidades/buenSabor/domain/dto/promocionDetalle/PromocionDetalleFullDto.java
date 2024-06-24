package com.entidades.buenSabor.domain.dto.promocionDetalle;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.dto.articulo.ArticuloDto;
import com.entidades.buenSabor.domain.dto.articuloManufacturado.ArticuloManufacturadoFullDto;
import com.entidades.buenSabor.domain.dto.articuloManufacturado.ArticuloManufacturadoShortDto;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromocionDetalleFullDto extends BaseDto {
    private Integer cantidad;
    private ArticuloDto articulo;
}
