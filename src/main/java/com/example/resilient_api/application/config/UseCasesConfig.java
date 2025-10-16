package com.example.resilient_api.application.config;

import com.example.resilient_api.domain.api.*;
import com.example.resilient_api.domain.spi.*;
import com.example.resilient_api.domain.usecase.*;
import com.example.resilient_api.infrastructure.adapters.techvalidatoradapter.CapacityTechRelationClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public CapacityServicePort capacityServicePort(CapacityPersistencePort capacityPersistencePort, TechValidatorGateway techValidatorGateway, CapacityTechRelationPort capacityTechRelationPort) {
                return new CapacityUseCase(capacityPersistencePort, techValidatorGateway, capacityTechRelationPort);
        }
        
        @Bean
        public BootcampServicePort bootcampServicePort(BootcampCapacityPort bootcampCapacityPort) {
                return new BootcampUseCase(bootcampCapacityPort);
        }
        
        @Bean
        public CapacitySagaUseCase capacitySagaUseCase(BootcampCapacityPort bootcampCapacityPort, CapacityPersistencePort capacityPersistencePort, TechSagaGateway techSagaGateway, CapacityTechRelationClient capacityTechRelationClient) {
                return new CapacitySagaUseCase(bootcampCapacityPort, capacityPersistencePort, techSagaGateway, capacityTechRelationClient);
        }
}
