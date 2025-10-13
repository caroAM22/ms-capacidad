package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.api.CapacityServicePort;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.enums.TechnicalMessage;
import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.domain.model.CapacityWithTechs;
import com.example.resilient_api.domain.model.Page;
import com.example.resilient_api.domain.model.PageRequest;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.CapacityTechRelationPort;
import com.example.resilient_api.domain.spi.TechValidatorGateway;
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
    public Mono<Page<CapacityWithTechs>> getAllCapacities(PageRequest pageRequest) {
        return capacityPersistencePort.findAllPaginated(pageRequest)
                .flatMap(capacityPage -> enrichCapacitiesWithTechs(capacityPage, pageRequest));
    }
    
    private Mono<Page<CapacityWithTechs>> enrichCapacitiesWithTechs(Page<Capacity> capacityPage, PageRequest pageRequest) {
        if (capacityPage.getContent().isEmpty()) {
            return Mono.just(Page.of(java.util.Collections.emptyList(), pageRequest, 0L));
        }
        
        java.util.List<Mono<CapacityWithTechs>> capacityMonos = capacityPage.getContent().stream()
                .map(this::buildCapacityWithTechs)
                .collect(java.util.stream.Collectors.toList());
        
        return Mono.zip(capacityMonos, objects -> 
                java.util.Arrays.stream(objects)
                    .map(obj -> (CapacityWithTechs) obj)
                    .collect(java.util.stream.Collectors.toList())
        )
        .map(capacitiesWithTechs -> applySorting(capacitiesWithTechs, pageRequest))
        .map(sortedList -> Page.of(sortedList, pageRequest, capacityPage.getTotalElements()));
    }
    
    private Mono<CapacityWithTechs> buildCapacityWithTechs(Capacity capacity) {
        return capacityTechRelationPort.getTechIdsByCapacityId(capacity.getId().toString())
                .flatMap(techIds -> 
                    techValidatorGateway.getTechsByIds(techIds)
                        .collectList()
                        .map(techs -> CapacityWithTechs.builder()
                            .id(capacity.getId())
                            .name(capacity.getName())
                            .description(capacity.getDescription())
                            .technologies(techs)
                            .techCount(techs.size())
                            .build())
                );
    }
    
    private java.util.List<CapacityWithTechs> applySorting(java.util.List<CapacityWithTechs> capacities, PageRequest pageRequest) {
        java.util.List<CapacityWithTechs> sortedList = new java.util.ArrayList<>(capacities);
        if ("name".equals(pageRequest.getSortBy())) {
            sortedList.sort((a, b) -> pageRequest.getDirection() == PageRequest.SortDirection.ASC ? 
                a.getName().compareTo(b.getName()) : b.getName().compareTo(a.getName()));
        } else if ("techCount".equals(pageRequest.getSortBy())) {
            sortedList.sort((a, b) -> pageRequest.getDirection() == PageRequest.SortDirection.ASC ? 
                Integer.compare(a.getTechCount(), b.getTechCount()) : Integer.compare(b.getTechCount(), a.getTechCount()));
        }
        return sortedList;
    }
    
    @Override
    public Mono<CapacityWithTechs> getCapacityById(String id) {
        return capacityPersistencePort.findById(id)
                .flatMap(this::buildCapacityWithTechs);
    }
}