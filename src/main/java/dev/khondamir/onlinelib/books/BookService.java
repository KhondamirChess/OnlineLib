package dev.khondamir.onlinelib.books;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {
    private BookRepository bookRepository;
    private final BookEntityConverter entityConverter;

    public BookService(BookRepository bookRepository, BookEntityConverter entityConverter) {
        this.bookRepository = bookRepository;
        this.entityConverter = entityConverter;
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
                        bookSearchFilter.authorName(),
                        bookSearchFilter.maxCost(),
                        pageable
                        )
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public Book createBook(Book bookToCreate) {
        var bookToSave = new BookEntity(
                null,
                bookToCreate.name(),
                bookToCreate.authorName(),
                bookToCreate.publicationYear(),
                bookToCreate.pageNumber(),
                bookToCreate.cost()
        );

        var savedEntity = bookRepository.save(bookToSave);


        return new Book(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getAuthorName(),
                savedEntity.getPublishedYear(),
                savedEntity.getPageNumber(),
                savedEntity.getCost()
        );
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
        bookRepository.updateBook(
                id,
                bookToUpdate.name(),
                bookToUpdate.authorName(),
                bookToUpdate.publicationYear(),
                bookToUpdate.pageNumber(),
                bookToUpdate.cost()
        );

        return entityConverter.toDomain(
                bookRepository.findById(id).orElseThrow()
        );
    }
}
