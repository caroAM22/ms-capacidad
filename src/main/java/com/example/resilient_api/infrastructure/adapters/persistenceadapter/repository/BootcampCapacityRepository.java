package com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository;

import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.BootcampCapacityEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampCapacityRepository extends R2dbcRepository<BootcampCapacityEntity, String> {
    Flux<BootcampCapacityEntity> findByBootcampId(String bootcampId);
    Mono<Void> deleteByBootcampId(String bootcampId);
    Mono<Long> countByCapacityId(String capacityId);
    Mono<Void> deleteByCapacityId(String capacityId);
}