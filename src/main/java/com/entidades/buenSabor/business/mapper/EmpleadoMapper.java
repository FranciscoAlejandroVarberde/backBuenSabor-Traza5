package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.empleado.EmpleadoFullDto;
import com.entidades.buenSabor.domain.entities.Empleado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends BaseMapper<Empleado, EmpleadoFullDto>{
}
