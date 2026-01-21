package dev.khondamir.onlinelib.author;

import dev.khondamir.onlinelib.books.Book;

import java.util.List;

public record Author(
        Long id,
        String name,
        Integer birthYear,
        List<Book> books
) {
}
