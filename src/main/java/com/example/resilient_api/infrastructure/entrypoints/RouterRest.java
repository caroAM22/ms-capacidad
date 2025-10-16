package com.example.resilient_api.infrastructure.entrypoints;

import com.example.resilient_api.infrastructure.entrypoints.handler.BootcampHandlerImpl;
import com.example.resilient_api.infrastructure.entrypoints.handler.CapacityHandlerImpl;
import com.example.resilient_api.infrastructure.entrypoints.handler.CapacitySagaHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    
    @Bean
    public RouterFunction<ServerResponse> capacityRoutes(CapacityHandlerImpl capacityHandler) {
        return route(POST("/capacities").and(accept(MediaType.APPLICATION_JSON)), capacityHandler::createCapacity)
                .andRoute(GET("/capacities"), capacityHandler::getAllCapacities)
                .andRoute(GET("/capacities/{id}"), capacityHandler::getCapacityById);
    }
    
    @Bean
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandlerImpl bootcampHandler) {
        return route(POST("/bootcamp-capacity").and(accept(MediaType.APPLICATION_JSON)), bootcampHandler::createBootcampCapacity)
                .andRoute(GET("/bootcamp-capacity/{bootcampId}"), bootcampHandler::getBootcampCapacities)
                .andRoute(DELETE("/bootcamp-capacity/{bootcampId}"), bootcampHandler::deleteBootcampCapacities);
    }
    
    @Bean
    public RouterFunction<ServerResponse> sagaRoutes(CapacitySagaHandlerImpl sagaHandler) {
        return route(GET("/saga/capacities/bootcamp/{bootcampId}"), sagaHandler::getCapacitiesByBootcamp)
                .andRoute(DELETE("/saga/capacities/orphan/{bootcampId}"), sagaHandler::deleteOrphanCapacities);
    }
}