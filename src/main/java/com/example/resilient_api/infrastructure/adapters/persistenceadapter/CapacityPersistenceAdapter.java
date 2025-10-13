package com.example.resilient_api.infrastructure.adapters.persistenceadapter;

import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.mapper.CapacityEntityMapper;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.CapacityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapacityPersistenceAdapter implements CapacityPersistencePort {
    
    private final CapacityRepository capacityRepository;
    private final CapacityEntityMapper mapper;

    
    @Override
    public Mono<Capacity> save(Capacity capacity) {
        var entity = mapper.toEntity(capacity);
        return capacityRepository.save(entity)
                .map(savedEntity -> {
                    var savedCapacity = mapper.toDomain(savedEntity);
                    savedCapacity.setTechIds(capacity.getTechIds());
                    return savedCapacity;
                });
    }
    
    @Override
    public Mono<Capacity> findById(String id) {
        return capacityRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Capacity> findByName(String name) {
        return capacityRepository.findByName(name)
                .map(mapper::toDomain);
    }
    
    @Override
    public Flux<Capacity> findAll() {
        return capacityRepository.findAll()
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Void> deleteById(String id) {
        return capacityRepository.deleteById(id);
    }
    
    @Override
    public Mono<Boolean> existsByName(String name) {
        return capacityRepository.countByName(name)
                .map(count -> count > 0);
    }
}