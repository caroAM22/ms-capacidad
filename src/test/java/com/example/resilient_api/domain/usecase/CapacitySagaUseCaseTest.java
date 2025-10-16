package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.model.CapacityWithTechs;
import com.example.resilient_api.domain.spi.BootcampCapacityPort;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.TechSagaGateway;
import com.example.resilient_api.infrastructure.adapters.techvalidatoradapter.CapacityTechRelationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacitySagaUseCaseTest {

    @Mock
    private BootcampCapacityPort bootcampCapacityPort;
    @Mock
    private CapacityPersistencePort capacityPersistencePort;
    @Mock
    private TechSagaGateway techSagaGateway;
    @Mock
    private CapacityTechRelationClient capacityTechRelationClient;

    private CapacitySagaUseCase capacitySagaUseCase;

    @BeforeEach
    void setUp() {
        capacitySagaUseCase = new CapacitySagaUseCase(bootcampCapacityPort, capacityPersistencePort, techSagaGateway, capacityTechRelationClient);
    }

    @Test
    void getCapacitiesByBootcamp_ShouldReturnCapacityIds() {
        String bootcampId = "bootcamp-123";
        CapacityWithTechs capacity1 = CapacityWithTechs.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                .name("Backend")
                .build();
        
        when(bootcampCapacityPort.getCapacitiesByBootcampId(bootcampId))
                .thenReturn(Flux.just(capacity1));

        StepVerifier.create(capacitySagaUseCase.getCapacitiesByBootcamp(bootcampId))
                .expectNext(Arrays.asList("550e8400-e29b-41d4-a716-446655440001"))
                .verifyComplete();
    }

    @Test
    void deleteOrphanCapacities_ShouldDeleteCapacitiesAndTechs() {
        String bootcampId = "bootcamp-123";
        
        // Mock que las capacidades son huérfanas (≤1 bootcamp las usa)
        when(bootcampCapacityPort.countBootcampsByCapacityId("cap1"))
                .thenReturn(Mono.just(1L));
        when(bootcampCapacityPort.countBootcampsByCapacityId("cap2"))
                .thenReturn(Mono.just(1L));
        
        when(capacityTechRelationClient.deleteCapacityTechRelations("cap1"))
                .thenReturn(Mono.empty());
        when(capacityTechRelationClient.deleteCapacityTechRelations("cap2"))
                .thenReturn(Mono.empty());
        when(bootcampCapacityPort.deleteByCapacityId("cap1"))
                .thenReturn(Mono.empty());
        when(bootcampCapacityPort.deleteByCapacityId("cap2"))
                .thenReturn(Mono.empty());
        when(capacityPersistencePort.deleteById("cap1"))
                .thenReturn(Mono.empty());
        when(capacityPersistencePort.deleteById("cap2"))
                .thenReturn(Mono.empty());
        when(techSagaGateway.deleteOrphanTechs(Arrays.asList("cap1", "cap2")))
                .thenReturn(Mono.empty());

        StepVerifier.create(capacitySagaUseCase.deleteOrphanCapacities(bootcampId, Arrays.asList("cap1", "cap2")))
                .verifyComplete();
    }
}