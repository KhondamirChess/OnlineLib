package dev.khondamir.onlinelib.author;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    private final AuthorDtoConverter dtoConverter;

    public AuthorController(AuthorService authorService, AuthorDtoConverter dtoConverter) {
        this.authorService = authorService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(
            @RequestBody @Valid AuthorDto authorToCreate
    ){
        log.info("Creating author: {}", authorToCreate);
        Author createdAuthor = authorService.createAuthor(
                dtoConverter.toDomain(authorToCreate)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdAuthor));
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors(){
        log.info("Getting all authors");
        return authorService.getAllAuthors()
                .stream()
                .map(dtoConverter::toDto)
                .toList();
    }

}
