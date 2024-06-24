package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.mapper.ArticuloManufacturadoMapper;
import com.entidades.buenSabor.business.service.ArticuloManufacturadoService;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CloudinaryService;
import com.entidades.buenSabor.domain.dto.articuloInsumo.ArticuloInsumoFullDto;
import com.entidades.buenSabor.domain.dto.articuloManufacturado.ArticuloManufacturadoFullDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.ArticuloInsumoRepository;
import com.entidades.buenSabor.repositories.ArticuloManufacturadoDetalleRepository;
import com.entidades.buenSabor.repositories.ArticuloManufacturadoRepository;
import com.entidades.buenSabor.repositories.ImagenArticuloRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
@Service
public class ArticuloManufacturadoServiceImp extends BaseServiceImp<ArticuloManufacturado, Long> implements ArticuloManufacturadoService {
    @Autowired
    ImagenArticuloRepository imagenArticuloRepository;
    @Autowired
    private CloudinaryService cloudinaryService; // Servicio para interactuar con Cloudinary
    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;
    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;
    @Autowired
    private ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;
    @Autowired
    private ArticuloManufacturadoMapper articuloManufacturadoMapper;
    @Override
    public List<ArticuloManufacturadoFullDto> manufacturados(Long idSucursal) {
        List<ArticuloManufacturado> manufacturados = this.articuloManufacturadoRepository.manufacturados(idSucursal);
        return articuloManufacturadoMapper.manufacturadosToManufacturadoFullDtos(manufacturados);
    }
    @Override
    public ArticuloManufacturado create(ArticuloManufacturado request) {
        // Validación y persistencia de los detalles
        Set<ArticuloManufacturadoDetalle> detalles = request.getArticuloManufacturadoDetalles();
        Set<ArticuloManufacturadoDetalle> detallesPersistidos = new HashSet<>();

        if (detalles != null && !detalles.isEmpty()) {
            for (ArticuloManufacturadoDetalle detalle : detalles) {
                ArticuloInsumo articuloInsumo = detalle.getArticuloInsumo();
                if (articuloInsumo == null || articuloInsumo.getId() == null) {
                    throw new RuntimeException("El artículo del detalle no puede ser nulo.");
                }
                articuloInsumo = articuloInsumoRepository.findById(detalle.getArticuloInsumo().getId())
                        .orElseThrow(() -> new RuntimeException("Artículo con id " + detalle.getArticuloInsumo().getId() + " inexistente"));
                detalle.setArticuloInsumo(articuloInsumo);
                ArticuloManufacturadoDetalle savedDetalle = articuloManufacturadoDetalleRepository.save(detalle);
                detallesPersistidos.add(savedDetalle);
            }
            request.setArticuloManufacturadoDetalles(detallesPersistidos);
        } else {
            throw new IllegalArgumentException("El articuloManufacturado debe contener un detalle o más.");
        }

        return articuloManufacturadoRepository.save(request);
    }
    @Override
    public ArticuloManufacturado update(ArticuloManufacturado request, Long id) {
        // Verificar si el artículo manufacturado existe
        ArticuloManufacturado existingArticulo = articuloManufacturadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo manufacturado con id " + id + " inexistente"));

        // Validación y persistencia de los detalles
        Set<ArticuloManufacturadoDetalle> nuevosDetalles = request.getArticuloManufacturadoDetalles();
        Set<ArticuloManufacturadoDetalle> detallesPersistidos = new HashSet<>();

        if (nuevosDetalles != null && !nuevosDetalles.isEmpty()) {
            for (ArticuloManufacturadoDetalle nuevoDetalle : nuevosDetalles) {
                ArticuloInsumo articuloInsumo = nuevoDetalle.getArticuloInsumo();
                if (articuloInsumo == null || articuloInsumo.getId() == null) {
                    throw new RuntimeException("El artículo del detalle no puede ser nulo.");
                }
                articuloInsumo = articuloInsumoRepository.findById(nuevoDetalle.getArticuloInsumo().getId())
                        .orElseThrow(() -> new RuntimeException("Artículo con id " + nuevoDetalle.getArticuloInsumo().getId() + " inexistente"));
                nuevoDetalle.setArticuloInsumo(articuloInsumo);

                if (nuevoDetalle.getId() == 0) {
                    // Es un nuevo detalle, agregarlo
                    ArticuloManufacturadoDetalle savedDetalle = articuloManufacturadoDetalleRepository.save(nuevoDetalle);
                    detallesPersistidos.add(savedDetalle);
                } else {
                    // Es un detalle existente, actualizarlo
                    ArticuloManufacturadoDetalle existingDetalle = articuloManufacturadoDetalleRepository.findById(nuevoDetalle.getId())
                            .orElseThrow(() -> new RuntimeException("Detalle con id " + nuevoDetalle.getId() + " inexistente"));
                    existingDetalle.setCantidad(nuevoDetalle.getCantidad());
                    existingDetalle.setArticuloInsumo(articuloInsumo); // actualizar el artículo insumo
                    ArticuloManufacturadoDetalle savedDetalle = articuloManufacturadoDetalleRepository.save(existingDetalle);
                    detallesPersistidos.add(savedDetalle);
                }
            }

            // Eliminar detalles que ya no están en la solicitud
            Set<ArticuloManufacturadoDetalle> existingDetalles = existingArticulo.getArticuloManufacturadoDetalles();
            for (ArticuloManufacturadoDetalle existingDetalle : existingDetalles) {
                if (nuevosDetalles.stream().noneMatch(d -> d.getId() != null && d.getId().equals(existingDetalle.getId()))) {
                    articuloManufacturadoDetalleRepository.delete(existingDetalle);
                }
            }

            request.setArticuloManufacturadoDetalles(detallesPersistidos);
        } else {
            throw new IllegalArgumentException("El articuloManufacturado debe contener un detalle o más.");
        }

        // Actualizar los campos del artículo manufacturado existente
        existingArticulo.setDenominacion(request.getDenominacion());
        existingArticulo.setDescripcion(request.getDescripcion());
        existingArticulo.setPrecioVenta(request.getPrecioVenta());
        existingArticulo.setTiempoEstimadoMinutos(request.getTiempoEstimadoMinutos());
        existingArticulo.setArticuloManufacturadoDetalles(detallesPersistidos);

        return articuloManufacturadoRepository.save(existingArticulo);
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImagesByArticuloManufacturadoId(Long id) {
        try {
            // Consultar todas las imágenes desde la base de datos que no estén eliminadas
            ArticuloManufacturado articulo = baseRepository.getById(id);
            List<ImagenArticulo> images = articulo.getImagenes().stream()
                    .filter(imagen -> !imagen.isEliminado()) // Filtrar imágenes no eliminadas
                    .toList();

            List<Map<String, Object>> imageList = new ArrayList<>();

            // Convertir cada imagen en un mapa de atributos para devolver como JSON
            for (ImagenArticulo image : images) {
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
    public ResponseEntity<String> uploadImages(MultipartFile[] files, Long idArticuloManufacturado) {
        List<String> urls = new ArrayList<>();
        var articuloManufacturado = baseRepository.getById(idArticuloManufacturado);
        try {
            // Iterar sobre cada archivo recibido
            for (MultipartFile file : files) {
                // Verificar si el archivo está vacío
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                // Crear una entidad Image y establecer su nombre y URL (subida a Cloudinary)
                ImagenArticulo image = new ImagenArticulo();
                image.setName(file.getOriginalFilename()); // Establecer el nombre del archivo original
                image.setUrl(cloudinaryService.uploadFile(file)); // Subir el archivo a Cloudinary y obtener la URL

                // Verificar si la URL de la imagen es nula (indicativo de fallo en la subida)
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }

                //Se asignan las imagenes al insumo
                articuloManufacturado.getImagenes().add(image);
                //Se guarda la imagen en la base de datos
                imagenArticuloRepository.save(image);
                // Agregar la URL de la imagen a la lista de URLs subidas
                urls.add(image.getUrl());
            }

            //se actualiza el insumo en la base de datos con las imagenes
            baseRepository.save(articuloManufacturado);

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
            imagenArticuloRepository.deleteImage(id);
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
