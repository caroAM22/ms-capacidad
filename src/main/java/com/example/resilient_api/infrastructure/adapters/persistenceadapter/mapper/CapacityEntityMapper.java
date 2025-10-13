package com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper;

import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CapacityEntityMapper {
    
    public Capacity toDomain(CapacityEntity entity) {
        return Capacity.builder()
                .id(entity.getId() != null ? UUID.fromString(entity.getId()) : null)
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
    
    public CapacityEntity toEntity(Capacity capacity) {
        return CapacityEntity.builder()
                .id(capacity.getId() != null ? capacity.getId().toString() : null)
                .name(capacity.getName())
                .description(capacity.getDescription())
                .build();
    }
}