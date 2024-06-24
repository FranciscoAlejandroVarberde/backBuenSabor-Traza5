package com.entidades.buenSabor.domain.entities;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class ImagenEmpresa extends Base{
    private String name; // Nombre de la imagen
    private String url; // URL de la imagen en almacenamiento externo (por ejemplo, Cloudinary)
}
