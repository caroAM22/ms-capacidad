package com.example.resilient_api.infrastructure.entrypoints.handler;

import com.example.resilient_api.domain.api.BootcampServicePort;
import com.example.resilient_api.infrastructure.entrypoints.dto.BootcampCapacityDTO;
import com.example.resilient_api.infrastructure.entrypoints.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampHandlerImpl {
    
    private final BootcampServicePort bootcampServicePort;
    
    public Mono<ServerResponse> createBootcampCapacity(ServerRequest request) {
        return request.bodyToMono(BootcampCapacityDTO.class)
                .flatMap(dto -> bootcampServicePort.assignCapacityToBootcamp(dto.getBootcampId(), dto.getCapacityId()))
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(APIResponse.builder()
                                .message("Capacity assigned to bootcamp successfully")
                                .build()));
    }
    
    public Mono<ServerResponse> getBootcampCapacities(ServerRequest request) {
        String bootcampId = request.pathVariable("bootcampId");
        
        return bootcampServicePort.getBootcampCapacities(bootcampId)
                .collectList()
                .flatMap(capacities -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(APIResponse.builder()
                                .data(capacities)
                                .message("Bootcamp capacities retrieved successfully")
                                .build()));
    }
    
    public Mono<ServerResponse> deleteBootcampCapacities(ServerRequest request) {
        String bootcampId = request.pathVariable("bootcampId");
        
        return bootcampServicePort.deleteBootcampCapacities(bootcampId)
                .then(ServerResponse.noContent().build());
    }
}