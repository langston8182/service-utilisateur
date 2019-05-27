package com.cmarchive.bank.serviceutilisateur.mapper;

import com.cmarchive.bank.ressource.model.OperationPermanenteDtos;
import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanentes;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OperationPermanenteMapper.class)
public interface OperationPermanentesMapper {

    @Mapping(target = "operationPermanentes", source = "operationPermanenteDtos")
    OperationPermanentes mapVersOperationPermanentes(OperationPermanenteDtos operationPermanenteDtos);

    @InheritInverseConfiguration
    OperationPermanenteDtos mapVersOperationPermanenteDtos(OperationPermanentes operationPermanentes);
}
