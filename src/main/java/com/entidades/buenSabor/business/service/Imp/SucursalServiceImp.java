package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.mapper.PedidoMapper;
import com.entidades.buenSabor.business.mapper.SucursalMapper;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CloudinaryService;
import com.entidades.buenSabor.business.service.SucursalService;
import com.entidades.buenSabor.domain.dto.sucursal.SucursalFullDto;
import com.entidades.buenSabor.domain.entities.*;
import com.entidades.buenSabor.repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class SucursalServiceImp extends BaseServiceImp<Sucursal,Long> implements SucursalService {
   @Autowired
   SucursalRepository sucursalRepository;
   @Autowired
   DomicilioRepository domicilioRepository;
   @Autowired
   EmpresaRepository empresaRepository;
    @Autowired
    ImagenSucursalRepository imagenSucursalRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private SucursalMapper sucursalMapper;
    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImp.class);
    @Override
    public List<SucursalFullDto> sucursalEmpresa(Long idEmpresa) {
        List<Sucursal> sucursales = this.sucursalRepository.sucursalEmpresa(idEmpresa);
        return sucursalMapper.sucursalesToSucursalFullDto(sucursales);
    }
    @Override
    @Transactional
    public Sucursal guardarSucursal(Sucursal sucursal) {
        var domicilio = sucursal.getDomicilio();
        domicilioRepository.save(domicilio);
        var empresa = empresaRepository.findById(sucursal.getEmpresa().getId());
        if(empresa.isEmpty()){
            throw new RuntimeException("No se puede guardar el empresa");
        }
        sucursal.setDomicilio(domicilio);

        return sucursalRepository.save(sucursal);
    }
    @Override
    public Sucursal actualizarSucursal(Long id, Sucursal sucursal) {
        // Buscar la sucursal existente en el repositorio
        Optional<Sucursal> sucursalOptional = sucursalRepository.findById(id);
        if (sucursalOptional.isEmpty()) {
            throw new RuntimeException("No se puede encontrar la sucursal con el ID proporcionado: " + id);
        }

        // Obtener la sucursal existente del Optional
        Sucursal sucursalExistente = sucursalOptional.get();

        // Copiar los datos actualizados de la sucursal proporcionada a la existente
        sucursalExistente.setNombre(sucursal.getNombre());
        sucursalExistente.setHorarioApertura(sucursal.getHorarioApertura());
        sucursalExistente.setHorarioCierre(sucursal.getHorarioCierre());
        sucursalExistente.setEsCasaMatriz(sucursal.isEsCasaMatriz());

        // Actualizar el domicilio y la empresa de la sucursal
        Domicilio domicilio = domicilioRepository.findById(sucursal.getDomicilio().getId())
                .orElseThrow(() -> new RuntimeException("No se puede encontrar el domicilio con el ID proporcionado: " + sucursal.getDomicilio().getId()));
        domicilioRepository.save(sucursal.getDomicilio());
        Empresa empresa = empresaRepository.findById(sucursal.getEmpresa().getId())
                .orElseThrow(() -> new RuntimeException("No se puede encontrar la empresa con el ID proporcionado: " + sucursal.getEmpresa().getId()));
        sucursalExistente.setDomicilio(domicilio);
        sucursalExistente.setEmpresa(empresa);

        // Eliminar las imágenes que ya no se necesitan
        Set<ImagenSucursal> nuevasImagenes = sucursal.getImagenes();
        Set<ImagenSucursal> imagenesAEliminar = new HashSet<>(sucursalExistente.getImagenes());
        imagenesAEliminar.removeAll(nuevasImagenes);
        imagenesAEliminar.forEach(imagen -> imagenSucursalRepository.delete(imagen));
        // Loggear las imágenes que se van a eliminar
        logger.info("Imágenes a eliminar:");
        imagenesAEliminar.forEach(imagen -> logger.info("ID: {}, URL: {}", imagen.getId(), imagen.getUrl()));

        // Verificar y guardar las nuevas imágenes
        Set<ImagenSucursal> imagenesAGuardar = new HashSet<>();
        for (ImagenSucursal imagen : nuevasImagenes) {
            // Verificar si el ID y la URL no son nulos
            if (imagen.getId() != null && imagen.getUrl() != null) {
                imagenesAGuardar.add(imagen);
            }
        }
        sucursalExistente.setImagenes(imagenesAGuardar);
        // Loggear las imágenes de la sucursal
        logger.info("Imágenes de la sucursal:");
        imagenesAGuardar.forEach(imagen -> logger.info("ID: {}, URL: {}", imagen.getId(), imagen.getUrl()));

        // Guardar la sucursal actualizada en el repositorio
        return sucursalRepository.save(sucursalExistente);
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImagesBySucursalId(Long id) {
        try {
            // Consultar todas las imágenes desde la base de datos
            List<ImagenSucursal> images = baseRepository.getById(id).getImagenes().stream().toList();
            List<Map<String, Object>> imageList = new ArrayList<>();

            // Convertir cada imagen en un mapa de atributos para devolver como JSON
            for (ImagenSucursal image : images) {
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
        }    }

    @Override
    public ResponseEntity<String> uploadImages(MultipartFile[] files, Long idSucursal) {
        List<String> urls = new ArrayList<>();
        var sucursal = baseRepository.getById(idSucursal);
        try {
            // Iterar sobre cada archivo recibido
            for (MultipartFile file : files) {
                // Verificar si el archivo está vacío
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                // Crear una entidad Image y establecer su nombre y URL (subida a Cloudinary)
                ImagenSucursal image = new ImagenSucursal();
                image.setName(file.getOriginalFilename()); // Establecer el nombre del archivo original
                image.setUrl(cloudinaryService.uploadFile(file)); // Subir el archivo a Cloudinary y obtener la URL

                // Verificar si la URL de la imagen es nula (indicativo de fallo en la subida)
                if (image.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }

                //Se asignan las imagenes a la empresa
                sucursal.getImagenes().add(image);
                //Se guarda la imagen en la base de datos
                imagenSucursalRepository.save(image);
                // Agregar la URL de la imagen a la lista de URLs subidas
                urls.add(image.getUrl());
            }

            //se actualiza el insumo en la base de datos con las imagenes
            baseRepository.save(sucursal);

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
            imagenSucursalRepository.deleteImage(id);

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
