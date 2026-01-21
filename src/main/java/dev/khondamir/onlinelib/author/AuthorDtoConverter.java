package dev.khondamir.onlinelib.author;

import dev.khondamir.onlinelib.books.BookDto;
import dev.khondamir.onlinelib.books.BookDtoConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDtoConverter{
    private final BookDtoConverter bookDtoConverter;

    public AuthorDtoConverter(BookDtoConverter bookDtoConverter) {
        this.bookDtoConverter = bookDtoConverter;
    }

    public Author toDomain(AuthorDto author){
        return new Author(
                author.id(),
                author.name(),
                author.birthYear(),
                author.books() == null
                ? List.of()
                : author.books().stream()
                        .map(bookDtoConverter::toDomain) // должен возвращать Book
                        .toList()
        );
    }

    public AuthorDto toDto(Author author){
        return new AuthorDto(
                author.id(),
                author.name(),
                author.birthYear(),
                author.books().stream()
                        .map(bookDtoConverter::toDto)
                        .toList()
        );
    }
}
