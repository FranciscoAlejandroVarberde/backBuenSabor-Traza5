package com.entidades.buenSabor.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
@Audited
public class Categoria extends Base {
    private String denominacion;
    private boolean esInsumo;

    // Relación muchos a uno con la misma entidad (padre)
    @ManyToOne
    @JoinColumn(name = "categoria_padre_id") // Nombre de la columna en la tabla de categoría que referencia al padre
    private Categoria parent;

    @ManyToMany(mappedBy = "categorias")
    @Builder.Default
    @JsonIgnore
    private Set<Sucursal> sucursales = new HashSet<>();

    @OneToMany(mappedBy = "parent") // MappedBy indica el nombre del campo en Categoria que representa la relación
    @Builder.Default
    @JsonIgnore
    private Set<Categoria> subCategorias = new HashSet<>();

    @OneToMany(mappedBy = "categoria")
    @Builder.Default
    @JsonIgnore
    private Set<Articulo> articulos = new HashSet<>();

    // Otros atributos y métodos de la clase
}
