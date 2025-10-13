package com.example.resilient_api.domain.usecase;

import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.model.Capacity;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.CapacityTechRelationPort;
import com.example.resilient_api.domain.spi.TechValidatorGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    private CapacityPersistencePort capacityPersistencePort;
    
    @Mock
    private TechValidatorGateway techValidatorGateway;

    @Mock
    private CapacityTechRelationPort capacityTechRelationPort;

    private CapacityUseCase capacityUseCase;

    @BeforeEach
    void setUp() {
        capacityUseCase = new CapacityUseCase(capacityPersistencePort, techValidatorGateway, capacityTechRelationPort);
    }

    @Test
    void createCapacitySuccess() {
        Capacity capacity = Capacity.builder()
                .name("Java Development")
                .description("Desarrollo en Java")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();

        when(capacityPersistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(techValidatorGateway.validateTechCount(anySet())).thenReturn(Mono.just(true));
        when(techValidatorGateway.validateAllTechsExist(anySet())).thenReturn(Mono.just(true));
        when(capacityPersistencePort.save(any(Capacity.class))).thenReturn(Mono.just(capacity));
        when(capacityTechRelationPort.saveCapacityTechRelations(anyString(), anySet())).thenReturn(Mono.empty());

        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectNext(capacity)
                .verifyComplete();
    }

    @Test
    void createCapacityDuplicateNameThrowsException() {
        Capacity capacity = Capacity.builder()
                .name("Java Development")
                .description("Desarrollo en Java")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();

        when(capacityPersistencePort.existsByName(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void createCapacityInvalidTechCountThrowsException() {
        Capacity capacity = Capacity.builder()
                .name("Java Development")
                .description("Desarrollo en Java")
                .techIds(Set.of("tech1", "tech2"))
                .build();

        when(capacityPersistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(techValidatorGateway.validateTechCount(anySet())).thenReturn(Mono.just(false));

        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void createCapacityInvalidTechsThrowsException() {
        Capacity capacity = Capacity.builder()
                .name("Java Development")
                .description("Desarrollo en Java")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();

        when(capacityPersistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(techValidatorGateway.validateTechCount(anySet())).thenReturn(Mono.just(true));
        when(techValidatorGateway.validateAllTechsExist(anySet())).thenReturn(Mono.just(false));

        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectError(BusinessException.class)
                .verify();
    }
}