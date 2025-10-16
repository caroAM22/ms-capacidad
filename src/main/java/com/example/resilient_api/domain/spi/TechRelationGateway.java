package com.example.resilient_api.domain.spi;

import reactor.core.publisher.Mono;
import java.util.Set;

public interface TechRelationGateway {
    Mono<Void> saveCapacityTechRelations(String capacityId, Set<String> techIds);
}