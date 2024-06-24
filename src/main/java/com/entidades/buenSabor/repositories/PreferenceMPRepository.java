package com.entidades.buenSabor.repositories;

import com.entidades.buenSabor.domain.entities.PreferenceMP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceMPRepository extends JpaRepository<PreferenceMP, String> {

    @Query("SELECT p FROM PreferenceMP p WHERE p.idPedido = :idPedido")
    Optional<PreferenceMP> findByIdPedido(@Param("idPedido") Long idPedido);
}
