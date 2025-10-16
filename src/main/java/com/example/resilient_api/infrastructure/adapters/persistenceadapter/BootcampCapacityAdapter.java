package com.example.resilient_api.infrastructure.adapters.persistenceadapter;

import com.example.resilient_api.domain.model.CapacityWithTechs;
import com.example.resilient_api.domain.spi.BootcampCapacityPort;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.TechValidatorGateway;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.BootcampCapacityEntity;
import com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository.BootcampCapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootcampCapacityAdapter implements BootcampCapacityPort {
    
    private final BootcampCapacityRepository bootcampCapacityRepository;
    private final CapacityPersistencePort capacityPersistencePort;
    private final TechValidatorGateway techValidatorGateway;
    
    @Override
    public Mono<Void> assignCapacityToBootcamp(String bootcampId, String capacityId) {
        BootcampCapacityEntity entity = BootcampCapacityEntity.builder()
                .id(UUID.randomUUID().toString())
                .bootcampId(bootcampId)
                .capacityId(capacityId)
                .build();
        
        return bootcampCapacityRepository.save(entity).then();
    }
    
    @Override
    public Flux<CapacityWithTechs> getCapacitiesByBootcampId(String bootcampId) {
        return bootcampCapacityRepository.findByBootcampId(bootcampId)
                .flatMap(entity -> capacityPersistencePort.findById(entity.getCapacityId()))
                .flatMap(capacity -> 
                    techValidatorGateway.getTechsByCapacityId(capacity.getId().toString())
                        .collectList()
                        .map(techs -> CapacityWithTechs.builder()
                            .id(capacity.getId())
                            .name(capacity.getName())
                            .description(capacity.getDescription())
                            .technologies(techs)
                            .techCount(techs.size())
                            .build())
                );
    }
    
    @Override
    public Mono<Void> deleteAllByBootcampId(String bootcampId) {
        return bootcampCapacityRepository.deleteByBootcampId(bootcampId);
    }
    
    @Override
    public Mono<Long> countBootcampsByCapacityId(String capacityId) {
        return bootcampCapacityRepository.countByCapacityId(capacityId);
    }
    
    @Override
    public Mono<Void> deleteByCapacityId(String capacityId) {
        return bootcampCapacityRepository.deleteByCapacityId(capacityId);
    }
}