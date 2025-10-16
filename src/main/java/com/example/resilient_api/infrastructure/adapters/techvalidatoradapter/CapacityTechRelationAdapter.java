package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

import com.example.resilient_api.domain.spi.CapacityTechRelationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CapacityTechRelationAdapter implements CapacityTechRelationPort {
    
    private final WebClient webClient;
    
    @Override
    public Mono<Void> saveCapacityTechRelations(String capacityId, Set<String> techIds) {
        return Flux.fromIterable(techIds)
                .flatMap(techId -> webClient.post()
                        .uri("http://localhost:8080/capacity-tech")
                        .bodyValue(Map.of("capacityId", capacityId, "techId", techId))
                        .retrieve()
                        .bodyToMono(String.class))
                .then();
    }
    
    @Override
    public Mono<Set<String>> getTechIdsByCapacityId(String capacityId) {
        return webClient.get()
                .uri("http://localhost:8080/capacity/{capacityId}/techs", capacityId)
                .retrieve()
                .bodyToFlux(Map.class)
                .map(tech -> (String) tech.get("id"))
                .collect(java.util.stream.Collectors.toSet());
    }

}