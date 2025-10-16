package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.model.CapacityWithTechs;
import com.example.resilient_api.domain.model.Tech;
import com.example.resilient_api.domain.spi.BootcampCapacityPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private BootcampCapacityPort bootcampCapacityPort;
    
    private BootcampUseCase bootcampUseCase;
    
    @BeforeEach
    void setUp() {
        bootcampUseCase = new BootcampUseCase(bootcampCapacityPort);
    }
    
    @Test
    void assignCapacityToBootcampSuccess() {
        String bootcampId = "bootcamp-123";
        String capacityId = "capacity-456";
        
        when(bootcampCapacityPort.assignCapacityToBootcamp(bootcampId, capacityId))
                .thenReturn(Mono.empty());
        
        StepVerifier.create(bootcampUseCase.assignCapacityToBootcamp(bootcampId, capacityId))
                .verifyComplete();
    }
    
    @Test
    void getBootcampCapacitiesSuccess() {
        String bootcampId = "bootcamp-123";
        
        Tech tech1 = Tech.builder().id("tech1").name("Java").build();
        CapacityWithTechs capacity = CapacityWithTechs.builder()
                .id(UUID.randomUUID())
                .name("Backend Development")
                .description("Java development")
                .technologies(List.of(tech1))
                .techCount(1)
                .build();
        
        when(bootcampCapacityPort.getCapacitiesByBootcampId(bootcampId))
                .thenReturn(Flux.just(capacity));
        
        StepVerifier.create(bootcampUseCase.getBootcampCapacities(bootcampId))
                .expectNext(capacity)
                .verifyComplete();
    }
    
    @Test
    void deleteBootcampCapacitiesSuccess() {
        String bootcampId = "bootcamp-123";
        
        when(bootcampCapacityPort.deleteAllByBootcampId(bootcampId))
                .thenReturn(Mono.empty());
        
        StepVerifier.create(bootcampUseCase.deleteBootcampCapacities(bootcampId))
                .verifyComplete();
    }
}