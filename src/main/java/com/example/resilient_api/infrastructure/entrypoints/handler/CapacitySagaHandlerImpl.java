package com.example.resilient_api.infrastructure.entrypoints.handler;

import com.example.resilient_api.domain.usecase.CapacitySagaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapacitySagaHandlerImpl {
    
    private final CapacitySagaUseCase capacitySagaUseCase;
    
    public Mono<ServerResponse> getCapacitiesByBootcamp(ServerRequest request) {
        String bootcampId = request.pathVariable("bootcampId");
        
        return capacitySagaUseCase.getCapacitiesByBootcamp(bootcampId)
                .flatMap(capacities -> ServerResponse.ok().bodyValue(capacities))
                .onErrorResume(e -> ServerResponse.badRequest().build());
    }
    
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> deleteOrphanCapacities(ServerRequest request) {
        String bootcampId = request.pathVariable("bootcampId");
        log.info("Received DELETE request for bootcamp: {}", bootcampId);
        
        return request.bodyToMono(List.class)
                .doOnNext(body -> log.info("Received body type: {}, content: {}", body != null ? body.getClass().getSimpleName() : "null", body))
                .cast(List.class)
                .flatMap(capacityIds -> {
                    log.info("Processing {} capacity IDs: {}", capacityIds.size(), capacityIds);
                    return capacitySagaUseCase.deleteOrphanCapacities(bootcampId, (List<String>) capacityIds);
                })
                .then(ServerResponse.noContent().build())
                .doOnSuccess(v -> log.info("Successfully processed deleteOrphanCapacities for bootcamp: {}", bootcampId))
                .onErrorResume(e -> {
                    log.error("Error in deleteOrphanCapacities for bootcamp {}: {}", bootcampId, e.getMessage(), e);
                    return ServerResponse.badRequest()
                            .bodyValue("Error: " + e.getMessage());
                });
    }
}