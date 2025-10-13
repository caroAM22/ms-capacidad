package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

import com.example.resilient_api.domain.spi.CapacityTechRelationPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class CapacityTechRelationAdapter implements CapacityTechRelationPort {
    
    @Override
    public Mono<Void> saveCapacityTechRelations(String capacityId, Set<String> techIds) {
        // Mock implementation - just return success
        return Mono.empty();
    }
    
    @Override
    public Mono<Set<String>> getTechIdsByCapacityId(String capacityId) {
        // Mock implementation - return empty set for now
        return Mono.just(java.util.Collections.emptySet());
    }

}