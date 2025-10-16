package com.example.resilient_api.domain.api;

import com.example.resilient_api.domain.model.CapacityWithTechs;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampServicePort {
    Mono<Void> assignCapacityToBootcamp(String bootcampId, String capacityId);
    Flux<CapacityWithTechs> getBootcampCapacities(String bootcampId);
    Mono<Void> deleteBootcampCapacities(String bootcampId);
}