package com.entidades.buenSabor.business.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// Interfaz que define los métodos para operaciones con imágenes
public interface ImageService {

     // Método para obtener todas las imágenes almacenadas
     ResponseEntity<List<Map<String, Object>>> getAllImages();

     // Método para subir imágenes al sistema
     ResponseEntity<String> uploadImages(MultipartFile[] files);

     // Método para eliminar una imagen por su identificador público y UUID
     ResponseEntity<String> deleteImage(String publicId, UUID uuid);

}
