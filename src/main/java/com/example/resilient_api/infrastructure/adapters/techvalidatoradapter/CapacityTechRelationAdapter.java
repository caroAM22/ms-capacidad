package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

import com.example.resilient_api.domain.spi.CapacityTechRelationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CapacityTechRelationAdapter implements CapacityTechRelationPort {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${tech-service.url}")
    private String techServiceUrl;
    
    @Override
    public Mono<Void> saveCapacityTechRelations(String capacityId, Set<String> techIds) {
        return Flux.fromIterable(techIds)
                .flatMap(techId -> saveRelation(capacityId, techId))
                .then();
    }
    
    private Mono<Void> saveRelation(String capacityId, String techId) {
        CapacityTechRelationRequest request = new CapacityTechRelationRequest(capacityId, techId);
        
        return webClientBuilder.build()
                .post()
                .uri(techServiceUrl + "/capacity-tech")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
    
    public record CapacityTechRelationRequest(String capacityId, String techId) {}
}