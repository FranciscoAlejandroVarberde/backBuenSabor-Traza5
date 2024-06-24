package com.entidades.buenSabor.repositories;


import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloInsumoRepository extends BaseRepository<ArticuloInsumo,Long> {
    @Query("SELECT p FROM ArticuloInsumo p WHERE p.sucursal.id = :idSucursal AND p.esParaElaborar = true AND p.eliminado = false")
    List<ArticuloInsumo> insumosParaElaborar(@Param("idSucursal") Long idSucursal);
    @Query("SELECT DISTINCT p FROM ArticuloInsumo p JOIN FETCH p.imagenes i " +
            "WHERE p.sucursal.id = :idSucursal AND p.eliminado = false AND i.eliminado = false")
    List<ArticuloInsumo> insumos(@Param("idSucursal") Long idSucursal);
}
