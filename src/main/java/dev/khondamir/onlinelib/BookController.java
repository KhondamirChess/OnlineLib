package dev.khondamir.onlinelib;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getAllBooks(
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "maxCost", required = false) Integer maxCost

    ) {
        log.info("getAllBooks");
        return bookService.searchAllBooks(authorName, maxCost);
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(
            @RequestBody Book bookToCreate
    ) {
        log.info("Get request to create book: book={}", bookToCreate);
        var createdBook = bookService.createBook(bookToCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("my-header", "123")
                .body(createdBook);
    }

    @GetMapping("/books/{id}")
    public Book findById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request to find book: id={}", id);
        return bookService.findById(id);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteById(
            @RequestParam Long id
    ) {
        log.info("Get request to delete book: id={}", id);
        bookService.deleteBook(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/books/{id}")
    public Book updateBook(
            @PathVariable("id") Long id,
            @RequestBody @Valid Book bookToUpdate
    ) {
        log.info("Get request to update book: id={}, bookToUpdate={}", id, bookToUpdate);
        return bookService.updateBook(id, bookToUpdate);
    }



}
