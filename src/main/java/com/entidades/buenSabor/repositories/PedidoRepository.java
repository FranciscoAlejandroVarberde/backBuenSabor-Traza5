package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.Cliente;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public interface PedidoRepository extends BaseRepository<Pedido,Long>{

    @Query("SELECT dp.articulo, SUM(dp.cantidad)" +
            "FROM Pedido p " +
            "JOIN p.detallePedidos dp " +
            "WHERE p.fechaPedido BETWEEN :desde AND :hasta AND p.sucursal.id = :sucursalId " +
            "GROUP BY dp.articulo")
    List<Object[]> getRankingInsumos(@Param("sucursalId") Long sucursalId, @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Query(""" 
        SELECT c.email, COUNT(p) FROM Pedido p
        JOIN p.cliente c
        WHERE p.fechaPedido BETWEEN :desde AND :hasta AND p.sucursal.id = :sucursalId
        GROUP BY c.id
    """)
    List<Object[]> getCantidadPedidosPorCliente(@Param("sucursalId") Long sucursalId, @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Query("SELECT CONCAT(DAY (p.fechaPedido), '-', MONTH (p.fechaPedido), '-', YEAR(p.fechaPedido)) , SUM(p.total) FROM Pedido p WHERE p.sucursal.id = :sucursalId AND p.fechaPedido BETWEEN :desde AND :hasta GROUP BY p.fechaPedido")
    List<Object[]> getIngresos(@Param("sucursalId") Long sucursalId, @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Query("SELECT CONCAT(DAY (p.fechaPedido), '-', MONTH (p.fechaPedido), '-', YEAR(p.fechaPedido)) , SUM(p.total) - SUM(p.totalCosto) FROM Pedido p WHERE p.sucursal.id = :sucursalId AND p.fechaPedido BETWEEN :desde AND :hasta GROUP BY p.fechaPedido")
    List<Object[]> getGanancias(@Param("sucursalId") Long sucursalId, @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Query("SELECT dp.articulo, SUM(dp.cantidad)" +
            "FROM Pedido p " +
            "JOIN p.detallePedidos dp " +
            "WHERE p.sucursal.id = :sucursalId " +
            "GROUP BY dp.articulo")
    List<Object[]> getRankingInsumos(@Param("sucursalId") Long sucursalId);

    @Query("""
        SELECT c.email, COUNT(p) FROM Pedido p
        JOIN p.cliente c
        WHERE p.sucursal.id = :sucursalId
        GROUP BY c.id
    """)
    List<Object[]> getCantidadPedidosPorCliente(@Param("sucursalId") Long sucursalId);

    @Query("SELECT CONCAT(DAY (p.fechaPedido), '-', MONTH (p.fechaPedido), '-', YEAR(p.fechaPedido)) , SUM(p.total) FROM Pedido p WHERE p.sucursal.id = :sucursalId GROUP BY p.fechaPedido")
    List<Object[]> getIngresos(@Param("sucursalId") Long sucursalId);

    @Query("SELECT CONCAT(DAY (p.fechaPedido), '-', MONTH (p.fechaPedido), '-', YEAR(p.fechaPedido)) , SUM(p.total) - SUM(p.totalCosto) FROM Pedido p WHERE p.sucursal.id = :sucursalId GROUP BY p.fechaPedido")
    List<Object[]> getGanancias(@Param("sucursalId") Long sucursalId);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.eliminado = false")
    List<Pedido> findByClienteId(@Param("clienteId") Long clienteId);
    @Query("SELECT p FROM Pedido p WHERE p.sucursal.id = :idSucursal AND p.eliminado = false")
    List<Pedido> pedidosSucursal(@Param("idSucursal") Long idSucursal);

    List<Pedido> findByEstadoIn(List<Estado> estados);

    List<Pedido> findByEstado(Estado estado);


}