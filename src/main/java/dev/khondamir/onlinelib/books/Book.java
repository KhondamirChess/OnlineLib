package dev.khondamir.onlinelib.books;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;


public record Book(
        Long id,
        String name,
        Long authorId,
        Integer publicationYear,
        Integer pageNumber,
        Integer cost
) {

}
