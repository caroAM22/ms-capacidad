package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.api.BootcampServicePort;
import com.example.resilient_api.domain.model.CapacityWithTechs;
import com.example.resilient_api.domain.spi.BootcampCapacityPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BootcampUseCase implements BootcampServicePort {
    
    private final BootcampCapacityPort bootcampCapacityPort;
    
    public BootcampUseCase(BootcampCapacityPort bootcampCapacityPort) {
        this.bootcampCapacityPort = bootcampCapacityPort;
    }
    
    @Override
    public Mono<Void> assignCapacityToBootcamp(String bootcampId, String capacityId) {
        return bootcampCapacityPort.assignCapacityToBootcamp(bootcampId, capacityId);
    }
    
    @Override
    public Flux<CapacityWithTechs> getBootcampCapacities(String bootcampId) {
        return bootcampCapacityPort.getCapacitiesByBootcampId(bootcampId);
    }
    
    @Override
    public Mono<Void> deleteBootcampCapacities(String bootcampId) {
        return bootcampCapacityPort.deleteAllByBootcampId(bootcampId);
    }
}