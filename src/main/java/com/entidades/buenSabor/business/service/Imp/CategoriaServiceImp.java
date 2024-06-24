package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.mapper.CategoriaMapper;
import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.CategoriaService;
import com.entidades.buenSabor.domain.dto.categoria.CategoriaCreateDto;
import com.entidades.buenSabor.domain.entities.Categoria;
import com.entidades.buenSabor.domain.entities.Promocion;
import com.entidades.buenSabor.domain.entities.Sucursal;
import com.entidades.buenSabor.repositories.CategoriaRepository;
import com.entidades.buenSabor.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoriaServiceImp extends BaseServiceImp<Categoria, Long> implements CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    CategoriaMapper categoriaMapper;
    @Override
    public List<CategoriaCreateDto> categoriaSucursal(Long idSucursal) {
        // Obtener la lista de categorías que están asociadas a la sucursal
        List<Categoria> categorias = this.categoriaRepository.categoriaSucursal(idSucursal);

        // Convertir la lista de categorías en un DTO utilizando el mapper
        List<CategoriaCreateDto> categoriaCreateDTOs = categoriaMapper.categoriasToCategoriaCreateDto(categorias);

        // Devolver la lista de DTOs procesados
        return categoriaCreateDTOs;
    }
    @Override
    public List<CategoriaCreateDto> categoriaInsumoSucursal(Long idSucursal) {
        // Obtener la lista de categorías que están asociadas a la sucursal
        List<Categoria> categorias = this.categoriaRepository.categoriaInsumosSucursal(idSucursal);

        // Convertir la lista de categorías en un DTO utilizando el mapper
        List<CategoriaCreateDto> categoriaCreateDTOs = categoriaMapper.categoriasToCategoriaCreateDto(categorias);

        // Devolver la lista de DTOs procesados
        return categoriaCreateDTOs;
    }@Override
    public List<CategoriaCreateDto> categoriaManufacturadoSucursal(Long idSucursal) {
        // Obtener la lista de categorías que están asociadas a la sucursal
        List<Categoria> categorias = this.categoriaRepository.categoriaManufacturadosSucursal(idSucursal);

        // Convertir la lista de categorías en un DTO utilizando el mapper
        List<CategoriaCreateDto> categoriaCreateDTOs = categoriaMapper.categoriasToCategoriaCreateDto(categorias);

        // Devolver la lista de DTOs procesados
        return categoriaCreateDTOs;
    }
    @Override
    public Categoria create(Categoria request) {
        // Guardar la instancia de Categoria en la base de datos para asegurarse de que esté gestionada por el EntityManager
        Categoria categoriaPersistida = categoriaRepository.save(request);

        Set<Sucursal> sucursales = categoriaPersistida.getSucursales();
        Set<Sucursal> sucursalesPersistidas = new HashSet<>();

        // Verificar y asociar sucursales existentes
        if (sucursales != null && !sucursales.isEmpty()) {
            for (Sucursal sucursal : sucursales) {
                // Verificar si la sucursal existe en la base de datos
                Optional<Sucursal> optionalSucursal = sucursalRepository.findById(sucursal.getId());
                if (optionalSucursal.isPresent()) {
                    Sucursal sucursalBd = optionalSucursal.get();
                    sucursalBd.getCategorias().add(categoriaPersistida); // Asociar la categoria a la sucursal
                    sucursalesPersistidas.add(sucursalBd);
                } else {
                    throw new RuntimeException("La sucursal con id " + sucursal.getId() + " no se ha encontrado");
                }
            }
            categoriaPersistida.setSucursales(sucursalesPersistidas); // Establecer las sucursales asociadas a la promoción
            categoriaRepository.save(categoriaPersistida); // Guardar la promoción actualizada con las sucursales asociadas
        }

        return categoriaPersistida;
    }

    @Override
    public Categoria update(Categoria request, Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con id " + id + " no se ha encontrado"));

        // Obtener todas las subcategorías actuales asociadas a la categoría
        Set<Categoria> subcategoriasActuales = new HashSet<>(categoria.getSubCategorias());

        // Limpiar todas las subcategorías asociadas a la categoría
        categoria.getSubCategorias().clear();

        // Guardar la categoría actualizada en la base de datos para que las subcategorías se desvinculen
        categoriaRepository.save(categoria);

        // Eliminar las relaciones entre las subcategorías y la categoría
        for (Categoria subcategoria : subcategoriasActuales) {
            subcategoria.setParent(null); // Eliminar la relación padre-hijo
            categoriaRepository.save(subcategoria); // Guardar la subcategoría actualizada
        }

        // Eliminar las subcategorías que ya no están en la categoría
        for (Categoria subcategoria : subcategoriasActuales) {
            if (!request.getSubCategorias().contains(subcategoria)) {
                categoriaRepository.delete(subcategoria);
            }
        }

        // Agregar las nuevas subcategorías proporcionadas en la solicitud
        Set<Categoria> subcategorias = request.getSubCategorias();
        Set<Categoria> subcategoriasPersistidas = new HashSet<>();

        if (subcategorias != null && !subcategorias.isEmpty()) {
            for (Categoria subcategoria : subcategorias) {
                if (subcategoria.getId() != null) {
                    // Si la subcategoría ya existe, obtenerla y actualizarla
                    Categoria subcategoriaBd = categoriaRepository.findById(subcategoria.getId())
                            .orElseThrow(() -> new RuntimeException("La subcategoría con id " + subcategoria.getId() + " no se ha encontrado"));
                    subcategoriaBd.setDenominacion(subcategoria.getDenominacion());
                    subcategoriaBd.setEsInsumo(request.isEsInsumo());
                    subcategoriaBd.setParent(categoria); // Establecer la relación padre-hijo
                    subcategoriasPersistidas.add(subcategoriaBd);
                } else {
                    // Si la subcategoría es nueva, solo agregarla a la categoría
                    subcategoria.setParent(categoria); // Establecer la relación padre-hijo
                    subcategoria.setEsInsumo(request.isEsInsumo());
                    subcategoriasPersistidas.add(subcategoria);
                }
            }
            categoria.setSubCategorias(subcategoriasPersistidas);
        }

        // Actualizar otras propiedades de la categoría si es necesario
        categoria.setEsInsumo(request.isEsInsumo());
        categoria.setDenominacion(request.getDenominacion());

        // Guardar la categoría actualizada en la base de datos
        return categoriaRepository.save(categoria);
    }

}
