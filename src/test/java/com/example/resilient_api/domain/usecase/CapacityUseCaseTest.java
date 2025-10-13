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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void registerCapacitySuccess() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Java development")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();
        
        Capacity savedCapacity = Capacity.builder()
                .id(UUID.randomUUID())
                .name("Backend Development")
                .description("Java development")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();
        
        when(capacityPersistencePort.existsByName("Backend Development")).thenReturn(Mono.just(false));
        when(techValidatorGateway.validateTechCount(any())).thenReturn(Mono.just(true));
        when(techValidatorGateway.validateAllTechsExist(any())).thenReturn(Mono.just(true));
        when(capacityPersistencePort.save(any())).thenReturn(Mono.just(savedCapacity));
        when(capacityTechRelationPort.saveCapacityTechRelations(anyString(), any())).thenReturn(Mono.empty());
        
        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectNext(savedCapacity)
                .verifyComplete();
    }
    
    @Test
    void registerCapacity_CapacityAlreadyExists() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Java development")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();
        
        when(capacityPersistencePort.existsByName("Backend Development")).thenReturn(Mono.just(true));

        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectError(BusinessException.class)
                .verify();
    }
    
    @Test
    void registerCapacityInvalidTechCount() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Java development")
                .techIds(Set.of("tech1", "tech2"))
                .build();
        
        when(capacityPersistencePort.existsByName("Backend Development")).thenReturn(Mono.just(false));
        when(techValidatorGateway.validateTechCount(any())).thenReturn(Mono.just(false));
        
        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectError(BusinessException.class)
                .verify();
    }
    
    @Test
    void registerCapacityInvalidTechs() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Java development")
                .techIds(Set.of("tech1", "tech2", "tech3"))
                .build();
        
        when(capacityPersistencePort.existsByName("Backend Development")).thenReturn(Mono.just(false));
        when(techValidatorGateway.validateTechCount(any())).thenReturn(Mono.just(true));
        when(techValidatorGateway.validateAllTechsExist(any())).thenReturn(Mono.just(false));
        
        StepVerifier.create(capacityUseCase.registerCapacity(capacity))
                .expectError(BusinessException.class)
                .verify();
    }
    
    @Test
    void getAllCapacitiesSuccess() {
        Capacity capacity1 = Capacity.builder()
                .id(UUID.randomUUID())
                .name("Backend Development")
                .description("Java development")
                .build();
        
        Capacity capacity2 = Capacity.builder()
                .id(UUID.randomUUID())
                .name("Frontend Development")
                .description("React development")
                .build();
        
        when(capacityPersistencePort.findAll()).thenReturn(Flux.just(capacity1, capacity2));
        
        StepVerifier.create(capacityUseCase.getAllCapacities())
                .expectNext(capacity1)
                .expectNext(capacity2)
                .verifyComplete();
    }
}