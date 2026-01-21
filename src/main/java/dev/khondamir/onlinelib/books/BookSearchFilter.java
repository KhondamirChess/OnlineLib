package dev.khondamir.onlinelib.books;

import jakarta.validation.constraints.Min;

public record BookSearchFilter(
        Long authorId,
        Integer maxCost,
        @Min(0)
        Integer pageNumber,
        @Min(3)
        Integer pageSize
) {
}
