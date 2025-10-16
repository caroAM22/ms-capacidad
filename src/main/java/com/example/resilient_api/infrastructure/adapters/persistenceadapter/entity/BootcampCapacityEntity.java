package com.example.resilient_api.infrastructure.adapters.persistenceadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("bootcamp_capacity")
public class BootcampCapacityEntity implements Persistable<String> {
    
    @Id
    private String id;
    
    @Column("bootcamp_id")
    private String bootcampId;
    
    @Column("capacity_id")
    private String capacityId;
    
    @Override
    public boolean isNew() {
        return true;
    }
}