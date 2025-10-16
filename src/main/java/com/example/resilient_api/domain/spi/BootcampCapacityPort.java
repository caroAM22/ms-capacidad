package com.example.resilient_api.domain.spi;

import com.example.resilient_api.domain.model.CapacityWithTechs;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampCapacityPort {
    Mono<Void> assignCapacityToBootcamp(String bootcampId, String capacityId);
    Flux<CapacityWithTechs> getCapacitiesByBootcampId(String bootcampId);
    Mono<Void> deleteAllByBootcampId(String bootcampId);
    Mono<Long> countBootcampsByCapacityId(String capacityId);
    Mono<Void> deleteByCapacityId(String capacityId);
}