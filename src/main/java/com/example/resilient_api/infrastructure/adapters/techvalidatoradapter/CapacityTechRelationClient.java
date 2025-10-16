package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapacityTechRelationClient {
    
    private final WebClient techWebClient;
    
    public Mono<Void> deleteCapacityTechRelations(String capacityId) {
        return techWebClient.delete()
                .uri("/capacity-tech/{capacityId}", capacityId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Deleted capacity-tech relations for capacity: {}", capacityId))
                .onErrorResume(ex -> {
                    log.error("Error deleting capacity-tech relations for capacity {}: {}", capacityId, ex.getMessage());
                    return Mono.empty();
                });
    }
}