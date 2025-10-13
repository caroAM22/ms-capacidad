package com.example.resilient_api.infrastructure.adapters.persistenceadapter.repository;

import com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity.CapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CapacityRepository extends ReactiveCrudRepository<CapacityEntity, String> {
    
    @Query("SELECT * FROM capacities WHERE name = :name")
    Mono<CapacityEntity> findByName(String name);
    
    @Query("SELECT COUNT(*) FROM capacities WHERE name = :name")
    Mono<Long> countByName(String name);
}