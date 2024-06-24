package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.Cliente;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.repositories.PedidoRepository;
import com.lowagie.text.DocumentException;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;

public interface PedidoService extends BaseService<Pedido, Long> {

    List<Object[]> getRankingInsumo(Long sucursalId, Instant desde, Instant hasta);
    List<Object[]> getRankingInsumo(Long sucursalId);
    List<Object[]> getCantidadPedidosPorCliente(Long sucursalId, Instant desde, Instant hasta);
    List<Object[]> getCantidadPedidosPorCliente(Long sucursalId);
    List<Object[]> getIngresos(Long sucursalId, Instant desde, Instant hasta);
    List<Object[]> getIngresos(Long sucursalId);
    List<Object[]> getGanancias(Long sucursalId, Instant desde, Instant hasta);
    List<Object[]> getGanancias(Long sucursalId);

    Pedido cambiarEstado(Long pedidoId, Estado nuevoEstado);
    List<Pedido> getPedidosFiltrados(String rol);

    List<PedidoFullDto> findByClienteId(Long idCliente);
    List<PedidoFullDto> pedidosSucursal(Long idSucursal);

    Pedido getPedidoById(Long pedidoId);

}
