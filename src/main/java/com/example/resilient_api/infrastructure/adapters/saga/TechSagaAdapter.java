package com.example.resilient_api.infrastructure.adapters.saga;

import com.example.resilient_api.domain.spi.TechSagaGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TechSagaAdapter implements TechSagaGateway {

    private final WebClient techWebClient;

    @Override
    public Mono<Void> deleteOrphanTechs(List<String> capacityIds) {
        return techWebClient.method(HttpMethod.DELETE)
                .uri("/saga/techs/orphan")
                .bodyValue(capacityIds)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Deleted orphan techs for capacities: {}", capacityIds));
    }

    @Override
    public Mono<Void> restoreTechs(List<String> techIds) {
        return techWebClient.post()
                .uri("/saga/techs/restore")
                .bodyValue(techIds)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Restored techs: {}", techIds));
    }
}