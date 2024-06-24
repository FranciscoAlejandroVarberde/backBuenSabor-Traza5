package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.ImagenPromocion;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenPromocionRepository extends BaseRepository<ImagenPromocion,Long>{
    @Modifying
    @Transactional
    @Query("UPDATE ImagenArticulo i SET i.eliminado = true WHERE i.id = :idImagen")
    void deleteImage(@Param("idImagen") Long idImagen);

}
