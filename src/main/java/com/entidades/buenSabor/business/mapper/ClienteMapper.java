package com.entidades.buenSabor.business.mapper;

import com.entidades.buenSabor.domain.dto.cliente.ClienteFullDto;
import com.entidades.buenSabor.domain.entities.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ClienteMapper extends BaseMapper<Cliente, ClienteFullDto>{
}
