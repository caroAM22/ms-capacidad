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
@Table("capacities")
public class CapacityEntity implements Persistable<String> {
    @Id
    private String id;
    
    @Column("name")
    private String name;
    
    @Column("description")
    private String description;
    
    @Override
    public boolean isNew() {
        return true;
    }
}