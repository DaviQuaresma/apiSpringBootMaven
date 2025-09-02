package com.movies.moviesApiRest.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @Builder .Default
    private Integer page = 1;

    @Builder .Default
    private Integer size = 10;

    @Builder .Default
    private String sortField = "id";

    @Builder .Default
    private Sort.Direction direction = Sort.Direction.DESC;
}
