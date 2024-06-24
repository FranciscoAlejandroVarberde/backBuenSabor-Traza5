package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CloudinaryService;
import com.entidades.buenSabor.business.service.EmpresaService;

import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.Empresa;
import com.entidades.buenSabor.domain.entities.ImagenArticulo;
import com.entidades.buenSabor.domain.entities.ImagenEmpresa;
import com.entidades.buenSabor.repositories.EmpresaRepository;
import com.entidades.buenSabor.repositories.ImagenEmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class EmpresaServiceImp extends BaseServiceImp<Empresa,Long> implements EmpresaService {

    @Autowired
    SucursalService sucursalService;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    ImagenEmpresaRepository imagenEmpresaRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImp.class);

    @Override
    public Empresa addSucursal(Long idEmpresa, Long idSucursal) {
        Empresa empresa = empresaRepository.findWithSucursalesById(idEmpresa);
        empresa.getSucursales().add(sucursalService.getById(idSucursal));
        return empresa;
    }
    @Override
    public Empresa update(Empresa updatedEmpresa, Long idEmpresa) {
        // Buscar la empresa existente en el repositorio
        Empresa existingEmpresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Actualizar los campos básicos de la empresa
        existingEmpresa.setNombre(updatedEmpresa.getNombre());
        existingEmpresa.setCuil(updatedEmpresa.getCuil());
        existingEmpresa.setRazonSocial(updatedEmpresa.getRazonSocial());

        // Actualizar las imágenes de la empresa
        Set<ImagenEmpresa> nuevasImagenes = updatedEmpresa.getImagenes();
        Set<ImagenEmpresa> imagenesAEliminar = new HashSet<>(existingEmpresa.getImagenes());
        imagenesAEliminar.removeAll(nuevasImagenes);
        imagenesAEliminar.forEach(imagen -> imagenEmpresaRepository.delete(imagen));
        // Loggear las imágenes que se van a eliminar
        logger.info("Imágenes a eliminar de la empresa:");
        imagenesAEliminar.forEach(imagen -> logger.info("ID: {}, URL: {}", imagen.getId(), imagen.getUrl()));

        // Verificar y guardar las nuevas imágenes
        Set<ImagenEmpresa> imagenesAGuardar = new HashSet<>();
        for (ImagenEmpresa imagen : nuevasImagenes) {
            // Verificar si el ID y la URL no son nulos
            if (imagen.getId() != null && imagen.getUrl() != null) {
                imagenesAGuardar.add(imagen);
            }
        }
        existingEmpresa.setImagenes(imagenesAGuardar);
        // Loggear las imágenes de la empresa
        logger.info("Imágenes de la empresa:");
        imagenesAGuardar.forEach(imagen -> logger.info("ID: {}, URL: {}", imagen.getId(), imagen.getUrl()));

        // Guardar la empresa actualizada en el repositorio
        return empresaRepository.save(existingEmpresa);
    }


    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImagesByEmpresaId(Long id) {
        try {
            // Consultar todas las imágenes desde la base de datos
            List<ImagenEmpresa> images = baseRepository.getById(id).getImagenes().stream().toList();
            List<Map<String, Object>> imageList = new ArrayList<>();

            // Convertir cada imagen en un mapa de atributos para devolver como JSON
            for (ImagenEmpresa image : images) {
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("name", image.getName());
                imageMap.put("url", image.getUrl());
                imageList.add(imageMap);
            }

            // Devolver la lista de imágenes como ResponseEntity con estado OK (200)
            return ResponseEntity.ok(imageList);
        } catch (Exception e) {
            e.printStackTrace();
            // Devolver un error interno del servidor (500) si ocurre alguna excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> uploadImages(MultipartFile[] files, Long idEmpresa) {
        List<String> urls = new ArrayList<>();
        var empresa = baseRepository.getById(idEmpresa);
        try {
            // Iterar sobre cada archivo recibido
            for (MultipartFile file : files) {
                // Verificar si el archivo está vacío
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                // Crear una entidad Image y establecer su nombre y URL (subida a Cloudinary)
                ImagenEmpresa image = new ImagenEmpresa();
                image.setName(file.getOriginalFilename()); // Establecer el nombre del archivo original
                image.setUrl(cloudinaryService.uploadFile(file)); // Subir el archivo a Cloudinary y obtener la URL

                // Verificar si la URL de la imagen es nula (indicativo de fallo en la subida)
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }

                //Se asignan las imagenes a la empresa
                empresa.getImagenes().add(image);
                //Se guarda la imagen en la base de datos
                imagenEmpresaRepository.save(image);
                // Agregar la URL de la imagen a la lista de URLs subidas
                urls.add(image.getUrl());
            }

            //se actualiza el insumo en la base de datos con las imagenes
            baseRepository.save(empresa);

            // Convertir la lista de URLs a un objeto JSON y devolver como ResponseEntity con estado OK (200)
            return new ResponseEntity<>("{\"status\":\"OK\", \"urls\":" + urls + "}", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            // Devolver un error (400) si ocurre alguna excepción durante el proceso de subida
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }    }

    @Override
    public ResponseEntity<String> deleteImage(String publicId, Long id) {
        try {
            // Eliminar la imagen de la base de datos usando su identificador
            imagenEmpresaRepository.deleteImage(id);

            // Llamar al servicio de Cloudinary para eliminar la imagen por su publicId
            ResponseEntity<String> cloudinaryResponse = cloudinaryService.deleteImage(publicId, id);
            // Retornar la respuesta del servicio de Cloudinary junto con la respuesta del repositorio
            return new ResponseEntity<>("{\"status\":\"SUCCESS\", \"message\":\"Imagen eliminada exitosamente.\"}", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            // Devolver un error (400) si ocurre alguna excepción durante la eliminación
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}
