package com.example.resilient_api.domain.spi;

import com.example.resilient_api.domain.model.Tech;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import com.example.resilient_api.domain.constants.Constants;

public interface TechValidatorGateway {
    Mono<Boolean> validateTechExists(String techId);
    Mono<Boolean> validateAllTechsExist(Set<String> techIds);
    Flux<Tech> getTechsByIds(Set<String> techIds);
    Flux<Tech> getTechsByCapacityId(String capacityId);
    
    default Mono<Boolean> validateTechCount(Set<String> techIds) {
        if (techIds == null || techIds.size() < Constants.TECH_MIN_COUNT || techIds.size() > Constants.TECH_MAX_COUNT) {
            return Mono.just(false);
        }
        return Mono.just(true);
    }
}