package dev.khondamir.onlinelib.author;

import dev.khondamir.onlinelib.books.BookEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@NamedEntityGraph(
        name = "author-with-books",
        attributeNodes = {
                @NamedAttributeNode("books")
        }
)
@NamedEntityGraph(
        name = "author-without-books",
        attributeNodes = {
        }
)
@Table(name="authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String name;

    private Integer birthYear;

    @OneToMany
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Set<BookEntity> books;

    public AuthorEntity(Long id, String name,Integer birthYear, Set<BookEntity> books ) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.birthYear = birthYear;
    }

    public AuthorEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Set<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(Set<BookEntity> books) {
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
