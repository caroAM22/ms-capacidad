package com.example.resilient_api.infrastructure.entrypoints.mapper;

import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.infrastructure.entrypoints.dto.CapacityDTO;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CapacityMapper {
    
    public CapacityDTO toDTO(Capacity capacity) {
        return CapacityDTO.builder()
                .id(capacity.getId() != null ? capacity.getId().toString() : null)
                .name(capacity.getName())
                .description(capacity.getDescription())
                .techIds(capacity.getTechIds())
                .build();
    }
    
    public Capacity toDomain(CapacityDTO capacityDTO) {
        return Capacity.builder()
                .id(capacityDTO.getId() != null ? UUID.fromString(capacityDTO.getId()) : null)
                .name(capacityDTO.getName())
                .description(capacityDTO.getDescription())
                .techIds(capacityDTO.getTechIds())
                .build();
    }
}