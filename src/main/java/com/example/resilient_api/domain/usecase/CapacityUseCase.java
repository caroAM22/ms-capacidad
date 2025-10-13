package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.api.CapacityServicePort;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.CapacityTechRelationPort;
import com.example.resilient_api.domain.spi.TechValidatorGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class CapacityUseCase implements CapacityServicePort {
    
    private final CapacityPersistencePort capacityPersistencePort;
    private final TechValidatorGateway techValidatorGateway;
    private final CapacityTechRelationPort capacityTechRelationPort;
    
    public CapacityUseCase(CapacityPersistencePort capacityPersistencePort, TechValidatorGateway techValidatorGateway, CapacityTechRelationPort capacityTechRelationPort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.techValidatorGateway = techValidatorGateway;
        this.capacityTechRelationPort = capacityTechRelationPort;
    }
    
    @Override
    public Mono<Capacity> registerCapacity(Capacity capacity) {
        return capacityPersistencePort.existsByName(capacity.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<Boolean>error(new BusinessException(TechnicalMessage.CAPACITY_ALREADY_EXISTS));
                    }
                    return techValidatorGateway.validateTechCount(capacity.getTechIds());
                })
                .flatMap(validCount -> {
                    if (!validCount) {
                        return Mono.<Boolean>error(new BusinessException(TechnicalMessage.INVALID_PARAMETERS));
                    }
                    return techValidatorGateway.validateAllTechsExist(capacity.getTechIds());
                })
                .flatMap(allTechsExist -> {
                    if (!allTechsExist) {
                        return Mono.<Capacity>error(new BusinessException(TechnicalMessage.TECH_INVALID));
                    }
                    capacity.setId(UUID.randomUUID());
                    return capacityPersistencePort.save(capacity)
                            .flatMap(savedCapacity -> 
                                capacityTechRelationPort.saveCapacityTechRelations(savedCapacity.getId().toString(), savedCapacity.getTechIds())
                                        .thenReturn(savedCapacity)
                            );
                });
    }
    
    @Override
    public Flux<Capacity> getAllCapacities() {
        return capacityPersistencePort.findAll();
    }
}