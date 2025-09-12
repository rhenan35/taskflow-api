package com.rhenan.taskflow.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PageRequest(
    @NotNull(message = "Número da página é obrigatório")
    @Min(value = 0, message = "Número da página deve ser maior ou igual a 0")
    Integer page,
    
    @NotNull(message = "Tamanho da página é obrigatório")
    @Min(value = 1, message = "Tamanho da página deve ser maior que 0")
    Integer size,
    
    String sortBy,
    String sortDirection
) {
    public static PageRequest of(Integer page, Integer size) {
        return PageRequest.builder()
            .page(page != null ? page : 0)
            .size(size != null ? size : 10)
            .sortBy("createdAt")
            .sortDirection("desc")
            .build();
    }
    
    public static PageRequest of(Integer page, Integer size, String sortBy, String sortDirection) {
        return PageRequest.builder()
            .page(page != null ? page : 0)
            .size(size != null ? size : 10)
            .sortBy(sortBy != null ? sortBy : "createdAt")
            .sortDirection(sortDirection != null ? sortDirection : "desc")
            .build();
    }
}