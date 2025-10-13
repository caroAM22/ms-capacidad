package com.example.resilient_api.infrastructure.entrypoints.handler;

import com.example.resilient_api.domain.api.CapacityServicePort;
import com.example.resilient_api.domain.exceptions.BusinessException;
import com.example.resilient_api.domain.model.PageRequest;
import com.example.resilient_api.infrastructure.entrypoints.dto.CapacityDTO;
import com.example.resilient_api.infrastructure.entrypoints.mapper.CapacityMapper;
import com.example.resilient_api.infrastructure.entrypoints.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapacityHandlerImpl {
    
    private final CapacityServicePort capacityServicePort;
    private final CapacityMapper capacityMapper;
    
    public Mono<ServerResponse> createCapacity(ServerRequest request) {
        return request.bodyToMono(CapacityDTO.class)
                .map(capacityMapper::toDomain)
                .flatMap(capacityServicePort::registerCapacity)
                .map(capacityMapper::toDTO)
                .flatMap(capacity -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(APIResponse.builder()
                                .data(capacity)
                                .message("Capacity created successfully")
                                .build()))
                .onErrorResume(BusinessException.class, ex -> 
                        ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(APIResponse.builder()
                                        .message(ex.getMessage())
                                        .build())
                );
    }
    
    public Mono<ServerResponse> getAllCapacities(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("name");
        String direction = request.queryParam("direction").orElse("ASC");
        
        PageRequest pageRequest = PageRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(PageRequest.SortDirection.valueOf(direction))
                .build();
        
        return capacityServicePort.getAllCapacities(pageRequest)
                .flatMap(capacityPage -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(APIResponse.builder()
                                .data(capacityPage)
                                .message("Capacities retrieved successfully")
                                .build()));
    }
}