package dev.khondamir.onlinelib.books;

import dev.khondamir.onlinelib.author.AuthorService;
import dev.khondamir.onlinelib.books.events.BookEventSender;
import dev.khondamir.onlinelib.books.events.BookKafkaEvent;
import dev.khondamir.onlinelib.books.events.EventType;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {
    private BookRepository bookRepository;
    private final BookEntityConverter entityConverter;
    private final AuthorService authorService;
    private final BookEventSender bookEventSender;

    public BookService(BookRepository bookRepository, BookEntityConverter entityConverter, AuthorService authorService, BookEventSender bookEventSender) {
        this.bookRepository = bookRepository;
        this.entityConverter = entityConverter;

        this.authorService = authorService;
        this.bookEventSender = bookEventSender;
    }

    public List<Book> searchAllBooks(
       BookSearchFilter bookSearchFilter
    ) {
        int pageSize = bookSearchFilter.pageSize() != null
                ? bookSearchFilter.pageSize() : 3;
        int pageNumber = bookSearchFilter.pageNumber() != null
                ? bookSearchFilter.pageNumber() : 0;

        Pageable pageable = Pageable
                .ofSize(pageSize)
                .withPage(pageNumber);

        return bookRepository.searchBooks(
                        bookSearchFilter.authorId(),
                        bookSearchFilter.maxCost(),
                        pageable
                        )
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public Book createBook(Book bookToCreate) {
        checkAuthorExistence(bookToCreate.authorId());
        var bookToSave = entityConverter.toEntity(bookToCreate);
        var savedBook = entityConverter.toDomain(
                bookRepository.save(bookToSave)
        );

        bookEventSender.sendEvent(new BookKafkaEvent(
                savedBook.id(),
                EventType.CREATED,
                savedBook
        ));

        return savedBook;
    }


    public Book findById(Long id) {
        var foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id " + id + " not found"
                ));
        return entityConverter.toDomain(foundBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        bookEventSender.sendEvent(new BookKafkaEvent(
                id,
                EventType.REMOVED,
                null
        ));

        bookRepository.deleteById(id);
    }

    public Book updateBook(
            Long id,
            Book bookToUpdate
    ) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("No book with id " + id + " found");
        }
        checkAuthorExistence(bookToUpdate.authorId());

        bookRepository.updateBook(
                id,
                bookToUpdate.name(),
                bookToUpdate.authorId(),
                bookToUpdate.publicationYear(),
                bookToUpdate.pageNumber(),
                bookToUpdate.cost()
        );
        var updatedBook = entityConverter.toDomain(
                bookRepository.findById(id).orElseThrow());

        bookEventSender.sendEvent(new BookKafkaEvent(
                id,
                EventType.UPDATED,
                null
        ));
        return updatedBook;

    }

    private void checkAuthorExistence(Long authorId) {
        if (!authorService.isAuthorExistsById(authorId)) {
            throw new EntityNotFoundException("Author not exist with id=%s".
                    formatted(authorId));
        }
    }

}
