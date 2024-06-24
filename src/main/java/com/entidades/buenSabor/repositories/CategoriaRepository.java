package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.Categoria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria,Long>{
    @Query("SELECT c FROM Categoria c JOIN c.sucursales s WHERE s.id = :idSucursal AND c.eliminado = false")
    List<Categoria> categoriaSucursal(@Param("idSucursal") Long idSucursal);
    @Query("SELECT c FROM Categoria c JOIN c.sucursales s WHERE s.id = :idSucursal AND c.esInsumo = true AND c.eliminado = false")
    List<Categoria> categoriaInsumosSucursal(@Param("idSucursal") Long idSucursal);
    @Query("SELECT c FROM Categoria c JOIN c.sucursales s WHERE s.id = :idSucursal AND c.esInsumo = false AND c.eliminado = false")
    List<Categoria> categoriaManufacturadosSucursal(@Param("idSucursal") Long idSucursal);
}
