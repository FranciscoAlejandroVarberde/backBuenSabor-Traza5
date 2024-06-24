package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.FacturaFacadeImp;
import com.entidades.buenSabor.domain.dto.empresa.EmpresaCreateDto;
import com.entidades.buenSabor.domain.dto.factura.FacturaFullDto;
import com.entidades.buenSabor.domain.entities.Factura;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factura")
@CrossOrigin("*")
public class FacturaController extends BaseControllerImp<Factura, FacturaFullDto, Long, FacturaFacadeImp> {

    public FacturaController(FacturaFacadeImp facade) {super (facade); }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaFullDto> getById(@PathVariable Long id){
        return super.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<FacturaFullDto>> getAll() {
        return super.getAll();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<FacturaFullDto> create(@RequestBody FacturaFullDto entity){
        return super.create(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<FacturaFullDto> edit(@RequestBody FacturaFullDto entity, @PathVariable Long id){
        return super.edit(entity, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', SUPERADMIN)")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        return super.deleteById(id);
    }

    @PostMapping("/crear/{pedidoId}")
    @PreAuthorize("hasAnyAuthority('CAJERO', 'ADMIN', 'SUPERADMIN')")
    public ResponseEntity<Factura> crearFactura(@PathVariable Long pedidoId) {
        Factura factura = this.facade.crearFactura(pedidoId);
        return ResponseEntity.ok(factura);
    }

}
