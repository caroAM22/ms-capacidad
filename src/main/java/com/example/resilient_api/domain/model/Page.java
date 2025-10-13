package com.example.resilient_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    
    public static <T> Page<T> of(List<T> content, PageRequest pageRequest, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageRequest.getSize());
        return Page.<T>builder()
                .content(content)
                .page(pageRequest.getPage())
                .size(pageRequest.getSize())
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(pageRequest.getPage() == 0)
                .last(pageRequest.getPage() >= totalPages - 1)
                .build();
    }
}