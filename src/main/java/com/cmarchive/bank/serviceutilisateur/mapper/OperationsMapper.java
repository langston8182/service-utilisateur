package com.cmarchive.bank.serviceutilisateur.mapper;

import com.cmarchive.bank.ressource.model.OperationDtos;
import com.cmarchive.bank.serviceutilisateur.modele.Operations;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OperationMapper.class)
public interface OperationsMapper {

    @Mapping(target = "operations", source = "operationDtos")
    Operations mapVersOperations(OperationDtos operationDtos);

    @InheritInverseConfiguration
    OperationDtos mapVersOperationDtos(Operations operations);
}
