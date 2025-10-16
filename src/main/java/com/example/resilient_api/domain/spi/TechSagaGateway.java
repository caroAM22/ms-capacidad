package com.example.resilient_api.domain.spi;

import reactor.core.publisher.Mono;
import java.util.List;

public interface TechSagaGateway {
    Mono<Void> deleteOrphanTechs(List<String> capacityIds);
    Mono<Void> restoreTechs(List<String> techIds);
}