package dev.khondamir.onlinelib.author;

import dev.khondamir.onlinelib.books.BookDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AuthorDto(
        @Null
        Long id,
        @NotBlank
        String name,
        @Min(0)
        Integer birthYear,
        @Size(max = 0)
        List<BookDto> books
) {

}
