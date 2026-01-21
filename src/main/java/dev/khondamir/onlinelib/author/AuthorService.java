package dev.khondamir.onlinelib.author;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorEntityConverter entityConverter;

    public AuthorService(AuthorRepository authorRepository, AuthorEntityConverter entityConverter) {
        this.authorRepository = authorRepository;
        this.entityConverter = entityConverter;
    }

    public Author createAuthor(Author author) {
        if (authorRepository.existsByName(author.name())){
            throw new IllegalArgumentException("Name already taken");
        }
        var entityToSave = entityConverter.toEntity(author);

        return entityConverter.toDomain(
                authorRepository.save(entityToSave)
        );

    }

    public boolean isAuthorExistsById(Long id) {
        return authorRepository.existsById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    @Transactional
    public void deleteAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new IllegalArgumentException("Author does not exist with id=%s"
                    .formatted(authorId));
        }
        authorRepository.deleteAuthorFromBooks(authorId);
        authorRepository.deleteById(authorId);
    }
}
