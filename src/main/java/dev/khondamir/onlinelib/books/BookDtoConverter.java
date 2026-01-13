package dev.khondamir.onlinelib.books;

import org.springframework.stereotype.Component;

@Component
public class BookDtoConverter {
    public BookDto toDto(Book book) {
        return new BookDto(
                book.id(),
                book.name(),
                book.authorName(),
                book.publicationYear(),
                book.pageNumber(),
                book.cost()
        );
    }

    public Book toDomain(BookDto book) {
        return new Book(
                book.id(),
                book.name(),
                book.authorName(),
                book.publicationYear(),
                book.pageNumber(),
                book.cost()
        );
    }
}
