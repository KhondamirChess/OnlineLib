package dev.khondamir.onlinelib.books;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="author_name", nullable=false)
    private String authorName;

    @Column(name="pub_year", nullable=false)
    private Integer publicationYear;

    @Column(name="page_num", nullable=false)
    private Integer pageNumber;

    @Column(name="cost", nullable=false)
    private Integer cost;

    public void setId(Long id) {
        this.id = id;
    }

    public BookEntity() {
    }

    public BookEntity(Long id, String name, String authorName, Integer publicationYear, Integer pageNumber, Integer cost) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.publicationYear = publicationYear;
        this.pageNumber = pageNumber;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getPublishedYear() {
        return publicationYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publicationYear = publishedYear;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }
}
