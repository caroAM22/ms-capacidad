package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechValidatorClient {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${tech-service.url}")
    private String techServiceUrl;
    
    public Mono<TechResponse> getTechById(String id) {
        return webClientBuilder.build()
                .get()
                .uri(techServiceUrl + "/tech/{id}", id)
                .retrieve()
                .bodyToMono(TechResponse.class)
                .onErrorReturn(new TechResponse(null, null, null));
    }
    
    public reactor.core.publisher.Flux<TechResponse> getTechsByCapacityId(String capacityId) {
        return webClientBuilder.build()
                .get()
                .uri(techServiceUrl + "/capacity/{capacityId}/techs", capacityId)
                .retrieve()
                .bodyToFlux(TechResponse.class);
    }
    
    public record TechResponse(String id, String name, String description) {}
}