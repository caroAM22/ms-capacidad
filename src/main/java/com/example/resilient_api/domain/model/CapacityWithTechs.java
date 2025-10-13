package com.example.resilient_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapacityWithTechs {
    private UUID id;
    private String name;
    private String description;
    private List<Tech> technologies;
    private int techCount;
}