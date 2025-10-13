package com.example.resilient_api.domain.spi;

import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.domain.model.Page;
import com.example.resilient_api.domain.model.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityPersistencePort {
    Mono<Capacity> save(Capacity capacity);
    Mono<Capacity> findById(String id);
    Mono<Capacity> findByName(String name);
    Flux<Capacity> findAll();
    Mono<Page<Capacity>> findAllPaginated(PageRequest pageRequest);
    Mono<Long> count();
    Mono<Void> deleteById(String id);
    Mono<Boolean> existsByName(String name);
}