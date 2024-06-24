package com.entidades.buenSabor.business.facade;

import com.entidades.buenSabor.business.facade.Base.BaseFacade;
import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.Rol;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;

public interface PedidoFacade extends BaseFacade<PedidoFullDto, Long> {

    SXSSFWorkbook getRankingInsumo(Long sucursalId, Instant desde, Instant hasta);

    List<Object[]> getRankingInsumoData(Long sucursalId);

    List<Object[]> getCantidadDePedidosPorData(Long sucursalId);

    List<Object[]> getIngresosData(Long sucursalId);

    List<Object[]> getGananciasData(Long sucursalId);

    SXSSFWorkbook getCantidadDePedidosPorCliente(Long sucursalId, Instant desde, Instant hasta);

    Pedido cambiarEstado(Long pedidoId, Estado nuevoEstado);
    List<Pedido> getPedidosFiltrados(String rol);

    List<PedidoFullDto> findByClienteId(Long clienteId);
    List<PedidoFullDto> pedidosSucursal(Long idSucursal);

    ByteArrayOutputStream generatePedidoPDF(Long pedidoId);
}
