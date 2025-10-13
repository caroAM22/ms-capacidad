package com.example.resilient_api.domain.spi;

import reactor.core.publisher.Mono;

import java.util.Set;

import com.example.resilient_api.domain.constants.Constants;

public interface TechValidatorGateway {
    Mono<Boolean> validateTechExists(String techId);
    Mono<Boolean> validateAllTechsExist(Set<String> techIds);
    
    default Mono<Boolean> validateTechCount(Set<String> techIds) {
        if (techIds == null || techIds.size() < Constants.TECH_MIN_COUNT || techIds.size() > Constants.TECH_MAX_COUNT) {
            return Mono.just(false);
        }
        return Mono.just(true);
    }
}