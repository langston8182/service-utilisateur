package com.cmarchive.bank.serviceutilisateur.modele.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OperationsDto {

    private List<OperationDto> operationDtos;
}
