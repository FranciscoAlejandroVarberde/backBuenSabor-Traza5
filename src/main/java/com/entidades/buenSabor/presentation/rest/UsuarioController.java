package com.entidades.buenSabor.presentation.rest;

import com.entidades.buenSabor.business.facade.Imp.UsuarioFacadeImp;
import com.entidades.buenSabor.domain.dto.unidadMedida.UnidadMedidaFullDto;
import com.entidades.buenSabor.domain.dto.usuarioCliente.UsuarioDto;
import com.entidades.buenSabor.domain.entities.Usuario;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.presentation.rest.Base.BaseControllerImp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarioCliente")
@CrossOrigin("*")
public class UsuarioController extends BaseControllerImp<Usuario, UsuarioDto, Long, UsuarioFacadeImp> {

    public UsuarioController(UsuarioFacadeImp facade) {super (facade); }

    @GetMapping("role/{email}")
    @CrossOrigin("*")
    @PreAuthorize("isAuthenticated()")
    public Usuario getUsuarioPorEmail(@PathVariable String email) {
        return this.facade.obtenerUsuarioPorEmail(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getById(@PathVariable Long id){
        return super.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> getAll() {
        return super.getAll();
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<UsuarioDto> create(@RequestBody UsuarioDto entity){
        return super.create(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<UsuarioDto> edit(@RequestBody UsuarioDto entity, @PathVariable Long id){
        return super.edit(entity, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        return super.deleteById(id);
    }

}
