package com.entidades.buenSabor.domain.dto.detallePedido;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.dto.articulo.ArticuloDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetallePedidoFullDto extends BaseDto {
    private Integer cantidad;
    private Double subTotal;
    private ArticuloDto articulo;
}
