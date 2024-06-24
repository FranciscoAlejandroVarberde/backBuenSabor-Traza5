package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.mapper.PromocionMapper;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CloudinaryService;
import com.entidades.buenSabor.business.service.PromocionService;
import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.dto.promocion.PromocionFullDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class PromocionServiceImp extends BaseServiceImp<Promocion, Long> implements PromocionService {
    @Autowired
    ImagenPromocionRepository imagenPromocionRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    PromocionRepository promocionRepository;
    @Autowired
    PromocionDetalleRepository promocionDetalleRepository;
    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    SucursalRepository sucursalRepository;
    @Autowired
    PromocionMapper promocionMapper;
    @Override
    public List<PromocionFullDto> promocionSucursal(Long idSucursal) {
        List<Promocion> promociones = this.promocionRepository.promocionSucursal(idSucursal);
        return promocionMapper.promocionesToPromocionFullDto(promociones);
    }

    @Override
    public Promocion create(Promocion request) {
        // Guardar la instancia de Promocion en la base de datos para asegurarse de que esté gestionada por el EntityManager
        Promocion promocionPersistida = promocionRepository.save(request);

        Set<Sucursal> sucursales = promocionPersistida.getSucursales();
        Set<Sucursal> sucursalesPersistidas = new HashSet<>();
        // Verificar y asociar sucursales existentes
        if (sucursales != null && !sucursales.isEmpty()) {
            for (Sucursal sucursal : sucursales) {
                // Verificar si la sucursal existe en la base de datos
                Optional<Sucursal> optionalSucursal = sucursalRepository.findById(sucursal.getId());
                if (optionalSucursal.isPresent()) {
                    Sucursal sucursalBd = optionalSucursal.get();
                    sucursalBd.getPromociones().add(promocionPersistida); // Asociar la promoción a la sucursal
                    sucursalesPersistidas.add(sucursalBd);
                } else {
                    throw new RuntimeException("La sucursal con id " + sucursal.getId() + " no se ha encontrado");
                }
            }
            promocionPersistida.setSucursales(sucursalesPersistidas); // Establecer las sucursales asociadas a la promoción
            promocionRepository.save(promocionPersistida); // Guardar la promoción actualizada con las sucursales asociadas
        }
        Set<PromocionDetalle> detalles = request.getPromocionDetalle();
        Set<PromocionDetalle> detallesPersistidos = new HashSet<>();

        if (detalles != null && !detalles.isEmpty()) {
            for (PromocionDetalle detalle : detalles) {
                Articulo articulo = detalle.getArticulo();
                if (articulo == null || articulo.getId() == null) {
                    throw new RuntimeException("El artículo del detalle no puede ser nulo.");
                }
                articulo = articuloRepository.findById(detalle.getArticulo().getId())
                        .orElseThrow(() -> new RuntimeException("Artículo con id " + detalle.getArticulo().getId() + " inexistente"));
                detalle.setArticulo(articulo);
                PromocionDetalle savedDetalle = promocionDetalleRepository.save(detalle);
                detallesPersistidos.add(savedDetalle);
            }
            request.setPromocionDetalle(detallesPersistidos);
        } else {
            throw new IllegalArgumentException("El pedido debe contener un detalle o más.");
        }
        return promocionPersistida;
    }
    @Override
    public Promocion update(Promocion request, Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La promoción con id " + id + " no se ha encontrado"));

        // Obtener todas las sucursales asociadas a la promoción
        Set<Sucursal> sucursalesActuales = promocion.getSucursales();

        // Eliminar las relaciones entre las sucursales y la promoción
        for (Sucursal sucursal : sucursalesActuales) {
            sucursal.getPromociones().remove(promocion);
            sucursalRepository.save(sucursal); // Guardar la sucursal actualizada
        }

        // Limpiar todas las sucursales asociadas a la promoción
        promocion.getSucursales().clear();

        // Agregar las nuevas sucursales proporcionadas en la solicitud
        Set<Sucursal> sucursales = request.getSucursales();
        Set<Sucursal> sucursalesPersistidas = new HashSet<>();

        if (sucursales != null && !sucursales.isEmpty()) {
            for (Sucursal sucursal : sucursales) {
                Sucursal sucursalBd = sucursalRepository.findById(sucursal.getId())
                        .orElseThrow(() -> new RuntimeException("La sucursal con id " + sucursal.getId() + " no se ha encontrado"));
                sucursalBd.getPromociones().add(promocion);
                sucursalesPersistidas.add(sucursalBd);
            }
            promocion.setSucursales(sucursalesPersistidas);
        }

        return super.update(request, id);
    }


    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImagesByPromocionId(Long id) {
        try {
            // Consultar todas las imágenes desde la base de datos
            List<ImagenPromocion> images = baseRepository.getById(id)
                    .getImagenes()
                    .stream()
                    // Filtrar las imágenes no eliminadas
                    .filter(image -> !image.isEliminado())
                    .toList();
            List<Map<String, Object>> imageList = new ArrayList<>();

            // Convertir cada imagen en un mapa de atributos para devolver como JSON
            for (ImagenPromocion image : images) {
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
    public ResponseEntity<String> uploadImages(MultipartFile[] files, Long idSucursal) {
        List<String> urls = new ArrayList<>();
        var promocion = baseRepository.getById(idSucursal);
        try {
            // Iterar sobre cada archivo recibido
            for (MultipartFile file : files) {
                // Verificar si el archivo está vacío
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                // Crear una entidad Image y establecer su nombre y URL (subida a Cloudinary)
                ImagenPromocion image = new ImagenPromocion();
                image.setName(file.getOriginalFilename()); // Establecer el nombre del archivo original
                image.setUrl(cloudinaryService.uploadFile(file)); // Subir el archivo a Cloudinary y obtener la URL

                // Verificar si la URL de la imagen es nula (indicativo de fallo en la subida)
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }

                //Se asignan las imagenes a la promocion
                promocion.getImagenes().add(image);
                //Se guarda la imagen en la base de datos
                imagenPromocionRepository.save(image);
                // Agregar la URL de la imagen a la lista de URLs subidas
                urls.add(image.getUrl());
            }

            //se actualiza el insumo en la base de datos con las imagenes
            baseRepository.save(promocion);

            // Convertir la lista de URLs a un objeto JSON y devolver como ResponseEntity con estado OK (200)
            return new ResponseEntity<>("{\"status\":\"OK\", \"urls\":" + urls + "}", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            // Devolver un error (400) si ocurre alguna excepción durante el proceso de subida
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> deleteImage(String publicId, Long id) {
        try {
            // Eliminar la imagen de la base de datos usando su identificador
            imagenPromocionRepository.deleteImage(id);
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
