package dev.khondamir.onlinelib.books.events;

import dev.khondamir.onlinelib.books.Book;

public record BookKafkaEvent(
        Long bookId,
        EventType eventType,
        Book book
) {
}
