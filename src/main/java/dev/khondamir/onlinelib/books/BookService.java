package dev.khondamir.onlinelib.books;

import dev.khondamir.onlinelib.author.AuthorService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {
    private BookRepository bookRepository;
    private final BookEntityConverter entityConverter;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, BookEntityConverter entityConverter, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.entityConverter = entityConverter;

        this.authorService = authorService;
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
        var savedEntity = bookRepository.save(bookToSave);

        return entityConverter.toDomain(savedEntity);
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

        return entityConverter.toDomain(
                bookRepository.findById(id).orElseThrow()
        );
    }

    private void checkAuthorExistence(Long authorId) {
        if (!authorService.isAuthorExistsById(authorId)) {
            throw new EntityNotFoundException("Author not exist with id=%s".
                    formatted(authorId));
        }
    }

}
