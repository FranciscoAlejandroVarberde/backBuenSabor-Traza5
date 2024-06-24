package com.entidades.buenSabor.business.mapper;


import com.entidades.buenSabor.domain.dto.empresa.EmpresaCreateDto;
import com.entidades.buenSabor.domain.dto.empresa.EmpresaFullDto;
import com.entidades.buenSabor.domain.entities.Empresa;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpresaMapper extends BaseMapper<Empresa, EmpresaCreateDto> {


    EmpresaFullDto toLargeDto(Empresa empresa);


}
