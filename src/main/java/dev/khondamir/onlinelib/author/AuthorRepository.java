package dev.khondamir.onlinelib.author;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByName(String name);
}
