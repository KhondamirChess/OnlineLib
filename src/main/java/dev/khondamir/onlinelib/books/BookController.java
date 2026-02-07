package dev.khondamir.onlinelib.books;


import dev.khondamir.onlinelib.books.purchase.PurchaseService;
import dev.khondamir.onlinelib.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    private final BookDtoConverter dtoConverter;

    private final AuthenticationService authenticationService;

    private final PurchaseService purchaseService;

    public BookController(BookService bookService, BookDtoConverter dtoConverter, AuthenticationService authenticationService, PurchaseService purchaseService) {
        this.bookService = bookService;
        this.dtoConverter = dtoConverter;
        this.authenticationService = authenticationService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/")
    public String ok() {
        return "OnlineLib API is running";
    }

    @GetMapping("/books")
    public List<BookDto> getAllBooks(
            @Valid BookSearchFilter bookSearchFilter

    ) {
        log.info("getAllBooks");
        return bookService.searchAllBooks(bookSearchFilter)
                .stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @PostMapping("/books")
    public ResponseEntity<BookDto> createBook(
            @RequestBody BookDto bookDtoToCreate
    ) {
        log.info("Get request to create book: book={}", bookDtoToCreate);
        var createdBook = bookService.createBook(
                dtoConverter.toDomain(bookDtoToCreate)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdBook));
    }

    @GetMapping("/books/{id}")
    public BookDto findById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request to find book: id={}", id);
        var foundBook = bookService.findById(id);
        return dtoConverter.toDto(foundBook);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        log.info("Get request to delete book: id={}", id);
        bookService.deleteBook(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/books/{id}")
    public BookDto updateBook(
            @PathVariable("id") Long id,
            @RequestBody @Valid BookDto bookDtoToUpdate
    ) {
        log.info("Get request to update book: id={}, bookToUpdate={}", id, bookDtoToUpdate);
        var updatedBook = bookService.updateBook(
                id,
                dtoConverter.toDomain(bookDtoToUpdate)
        );
        return dtoConverter.toDto(updatedBook);
    }


    @PostMapping("/books/{id}/purchase")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> purchaseBook(
        @PathVariable("id") Long bookId
    ){
        log.info("Get request to purchase book: bookId={}", bookId);
        var currentUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        purchaseService.performBookPurchase(currentUser, bookId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
