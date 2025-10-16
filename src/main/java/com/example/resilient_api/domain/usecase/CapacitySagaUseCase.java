package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.spi.BootcampCapacityPort;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.TechSagaGateway;
import com.example.resilient_api.infrastructure.adapters.techvalidatoradapter.CapacityTechRelationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CapacitySagaUseCase {
    
    private final BootcampCapacityPort bootcampCapacityPort;
    private final CapacityPersistencePort capacityPersistencePort;
    private final TechSagaGateway techSagaGateway;
    private final CapacityTechRelationClient capacityTechRelationClient;
    
    public Mono<List<String>> getCapacitiesByBootcamp(String bootcampId) {
        return bootcampCapacityPort.getCapacitiesByBootcampId(bootcampId)
                .map(capacity -> capacity.getId().toString())
                .collectList();
    }
    
    public Mono<Void> deleteOrphanCapacities(String bootcampId, List<String> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .filterWhen(this::checkIfCapacityIsOrphan)
                .collectList()
                .flatMap(orphanCapacities -> {
                    if (orphanCapacities.isEmpty()) {
                        log.info("No orphan capacities found for bootcamp {}", bootcampId);
                        return Mono.empty();
                    }
                    log.info("Deleting {} orphan capacities: {}", orphanCapacities.size(), orphanCapacities);
                    return techSagaGateway.deleteOrphanTechs(orphanCapacities)
                            .then(deleteCapacityTechRelations(orphanCapacities))
                            .then(deleteCapacityRelations(orphanCapacities))
                            .then(deleteCapacities(orphanCapacities));
                });
    }
    
    private Mono<Boolean> checkIfCapacityIsOrphan(String capacityId) {
        return bootcampCapacityPort.countBootcampsByCapacityId(capacityId)
                .map(count -> count <= 1); // Solo es huérfana si la usa 1 o menos bootcamps
    }
    
    private Mono<Void> deleteCapacityTechRelations(List<String> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .flatMap(capacityTechRelationClient::deleteCapacityTechRelations)
                .then();
    }
    
    private Mono<Void> deleteCapacityRelations(List<String> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .flatMap(bootcampCapacityPort::deleteByCapacityId)
                .then();
    }
    
    private Mono<Void> deleteCapacities(List<String> capacityIds) {
        return Flux.fromIterable(capacityIds)
                .flatMap(capacityPersistencePort::deleteById)
                .then();
    }
}