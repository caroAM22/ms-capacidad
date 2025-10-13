package com.example.resilient_api.infrastructure.adapters.techvalidatoradapter;

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
    public Mono<Boolean> validateAllTechsExist(Set<String> techIds) {
        return Flux.fromIterable(techIds)
                .flatMap(this::validateTechExists)
                .all(exists -> exists);
    }
}