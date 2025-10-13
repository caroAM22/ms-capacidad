package com.example.resilient_api.application.config;

import com.example.resilient_api.domain.api.CapacityServicePort;
import com.example.resilient_api.domain.spi.CapacityPersistencePort;
import com.example.resilient_api.domain.spi.TechValidatorGateway;
import com.example.resilient_api.domain.usecase.CapacityUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.resilient_api.domain.spi.CapacityTechRelationPort;

@Configuration
public class UseCasesConfig {

        @Bean
        public CapacityServicePort capacityServicePort(CapacityPersistencePort capacityPersistencePort, TechValidatorGateway techValidatorGateway, CapacityTechRelationPort capacityTechRelationPort) {
                return new CapacityUseCase(capacityPersistencePort, techValidatorGateway, capacityTechRelationPort);
        }
}
