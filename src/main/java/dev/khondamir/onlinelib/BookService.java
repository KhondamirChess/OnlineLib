package dev.khondamir.onlinelib;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class BookService {

    private Long idCounter;
    private Map<Long, Book> bookMap;

    public BookService() {
        this.idCounter = 1L;
        this.bookMap = new HashMap<>();
    }

    public List<Book> searchAllBooks(
            String authorName,
            Integer maxCost
    ) {
        return bookMap.values()
                .stream()
                .filter(book -> authorName == null || book.authonName().equals(authorName))
                .filter(book -> maxCost == null || book.cost() > maxCost)
                .toList();
    }

    public Book createBook(Book bookToCreate) {
        var newId = idCounter++;
        var newBook = new Book(
                newId,
                bookToCreate.name(),
                bookToCreate.authonName(),
                bookToCreate.publicationYear(),
                bookToCreate.pageNumber(),
                bookToCreate.cost()
        );
        bookMap.put(newId, newBook);
        return newBook;
    }

    public Book findById(Long id) {
        return Optional.ofNullable(bookMap.get(id))
                .orElseThrow(() -> new RuntimeException(
                        "Book with id " + id + " not found"
                ));
    }

    public void deleteBook(Long id) {
        var result = bookMap.remove(id);
        if (result == null) {
            throw new RuntimeException(
                    "Book with id " + id + " not found"
            );
        }
    }

    public Book updateBook(
            Long id,
            Book bookToUpdate
    ) {
        if (bookMap.get(id) == null) {
            throw new RuntimeException("Book with id " + id + " not found");
        }
        var updatedBook = new Book(
                id,
                bookToUpdate.name(),
                bookToUpdate.authonName(),
                bookToUpdate.publicationYear(),
                bookToUpdate.pageNumber(),
                bookToUpdate.cost()
        );
        bookMap.put(id, bookToUpdate);
        return updatedBook;
    }
}
