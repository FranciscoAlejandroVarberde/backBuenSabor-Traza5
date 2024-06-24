package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import com.entidades.buenSabor.domain.entities.Pedido;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenArticuloRepository extends BaseRepository<ImagenArticulo,Long>{
    @Modifying
    @Transactional
    @Query("UPDATE ImagenArticulo i SET i.eliminado = true WHERE i.id = :idImagen")
    void deleteImage(@Param("idImagen") Long idImagen);

}
