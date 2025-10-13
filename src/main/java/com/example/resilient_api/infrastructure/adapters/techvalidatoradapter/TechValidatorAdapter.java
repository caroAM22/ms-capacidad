package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.TechValidatorGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TechValidatorAdapter implements TechValidatorGateway {
    
    private final TechValidatorClient techValidatorClient;
    
    @Override
    public Mono<Boolean> validateTechExists(String techId) {
        return techValidatorClient.getTechById(techId)
                .map(response -> response.id() != null);
    }
    
    @Override
    public Mono<Boolean> validateTechCount(Set<String> techIds) {
        return Mono.just(techIds.size() >= 3 && techIds.size() <= 20);
    }
    
    @Override
    public Mono<Boolean> validateAllTechsExist(Set<String> techIds) {
        return Flux.fromIterable(techIds)
                .flatMap(this::validateTechExists)
                .all(exists -> exists);
    }
    
    @Override
    public Flux<Tech> getTechsByIds(Set<String> techIds) {
        return Flux.fromIterable(techIds)
                .flatMap(techId -> 
                    techValidatorClient.getTechById(techId)
                        .filter(response -> response.id() != null)
                        .map(response -> Tech.builder()
                            .id(response.id())
                            .name(response.name())
                            .build())
                );
    }
}