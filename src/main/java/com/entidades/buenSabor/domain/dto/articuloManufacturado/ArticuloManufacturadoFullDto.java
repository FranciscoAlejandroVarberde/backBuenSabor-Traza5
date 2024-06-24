package com.entidades.buenSabor.domain.dto.articuloManufacturado;

import com.entidades.buenSabor.domain.dto.BaseDto;
import com.entidades.buenSabor.domain.dto.articuloManufacturadoDetalle.ArticuloManufacturadoDetalleFullDto;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaFullDto;
import com.entidades.buenSabor.domain.dto.imagen.ImagenDto;
import com.entidades.buenSabor.domain.dto.sucursal.SucursalShortDto;
import com.entidades.buenSabor.domain.dto.unidadMedida.UnidadMedidaFullDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticuloManufacturadoFullDto extends BaseDto {
    //de articulo
    private String denominacion;
    private Double precioVenta;
    //de imagenArticulo
    private Set<ImagenDto> imagenes;
    //de categoria
    private CategoriaFullDto categoria;
    //propios de la entidad
    private String descripcion;
    private Integer tiempoEstimadoMinutos;
    private String preparacion;
    //de unidad medida
    private UnidadMedidaFullDto unidadMedida;
    //de articuloManufacturadoDetalle
    private Set<ArticuloManufacturadoDetalleFullDto> articuloManufacturadoDetalles;
    //sucursal
    private SucursalShortDto sucursal;
}
