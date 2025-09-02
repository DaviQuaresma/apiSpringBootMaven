package com.movies.moviesApiRest.responses;

import com.movies.moviesApiRest.models.PaginationRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

@Data
@NoArgsConstructor
public class PagingResult<T> {
    private Collection<T> content;
    private Integer totalPages;
    private long totalElements;
    private Integer size;
    private Integer page;
    private boolean empty;

    public PagingResult(
            Collection<T> content,
            Integer totalPages,
            long totalElements,
            Integer size,
            Integer page,
            boolean empty
    ) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
        this.empty = empty;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PaginationUtils {

        public static Pageable getPageable(PaginationRequest request) {
            return PageRequest.of(
                    request.getPage() - 1,
                    request.getSize(),
                    request.getDirection(),
                    request.getSortField()
            );
        }
    }
}
