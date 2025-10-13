package com.example.resilient_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.constants.Constants;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Capacity {
    private UUID id;
    private String name;
    private String description;
    private Set<String> techIds; // IDs de las tecnologías asociadas
    
    public void validateTechCount() {
        if (techIds == null || techIds.size() < Constants.TECH_MIN_COUNT || techIds.size() > Constants.TECH_MAX_COUNT) {
            throw new BusinessException(TechnicalMessage.CAPACITY_TECH_COUNT_INVALID);
        }
    }
}