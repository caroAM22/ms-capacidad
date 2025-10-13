package com.example.resilient_api.domain.api;

import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.domain.model.CapacityWithTechs;
import com.example.resilient_api.domain.model.Page;
import com.example.resilient_api.domain.model.PageRequest;
import reactor.core.publisher.Mono;

public interface CapacityServicePort {
    Mono<Capacity> registerCapacity(Capacity capacity);
    Mono<Page<CapacityWithTechs>> getAllCapacities(PageRequest pageRequest);
    Mono<CapacityWithTechs> getCapacityById(String id);
}