package dev.khondamir.onlinelib.author;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("""
                    UPDATE BookEntity b
                    SET b.authorId = NULL 
                    WHERE b.authorId = :authorId
            """)
    void deleteAuthorFromBooks(Long authorId);
}
