package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.ArticuloInsumoFacade;
import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.ArticuloInsumoService;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.domain.dto.articuloInsumo.ArticuloInsumoFullDto;
import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.ArticuloInsumo;
import com.entidades.buenSabor.domain.entities.ArticuloManufacturado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class ArticuloInsumoFacadeImp extends BaseFacadeImp<ArticuloInsumo, ArticuloInsumoFullDto, Long> implements ArticuloInsumoFacade {
    public ArticuloInsumoFacadeImp(BaseService<ArticuloInsumo,Long> baseService, BaseMapper<ArticuloInsumo, ArticuloInsumoFullDto> baseMapper){
        super(baseService, baseMapper);
    }
    @Autowired
    ArticuloInsumoService articuloInsumoService;
    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImagesByInsumoId(Long id) {
        return articuloInsumoService.getAllImagesByInsumoId(id);
    }
    @Override
    public List<ArticuloInsumoFullDto> insumosParaElaborar(Long idSucursal) {
        return this.articuloInsumoService.insumosParaElaborar(idSucursal);
    }
    @Override
    public List<ArticuloInsumoFullDto> insumos(Long idSucursal) {
        return this.articuloInsumoService.insumos(idSucursal);
    }
    @Override
    public ResponseEntity<String> uploadImages(MultipartFile[] files, Long id) {
        return articuloInsumoService.uploadImages(files,id);
    }

    @Override
    public ResponseEntity<String> deleteImage(String publicId, Long id) {
        return articuloInsumoService.deleteImage(publicId, id);
    }
    @Override
    public ResponseEntity<Number> descontarStock(ArticuloInsumo articuloInsumo, Integer cantidad) {
        return articuloInsumoService.descontarStock(articuloInsumo, cantidad);
    }
}
