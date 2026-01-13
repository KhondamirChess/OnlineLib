package dev.khondamir.onlinelib.books;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

//    List<BookEntity> findAllByAuthorNameIsAndCostLessThan(
//            String authorName,
//            Integer costIsLessThan
//    );

    @Query("""
            SELECT b from BookEntity b                       
            WHERE (:authorName IS NULL OR b.authorName = :authorName)
            AND (:cost IS NULL OR b.cost = :cost)
            
            """)
    List<BookEntity> searchBooks(
            String authorName,
            @Param("cost") Integer maxCost,
            Pageable pageable
    );

    @Query(value = """
            SELECT * from books b                       
            WHERE (:authorName IS NULL OR b.author_name = :authorName)
            AND (:cost IS NULL OR b.cost = :cost)
            """, nativeQuery = true)
    List<BookEntity> searchBooksNative(
            String authorName,
            @Param("cost") Integer maxCost
    );

    @Transactional
    @Modifying
    @Query("""
    UPDATE BookEntity b
    SET b.name = :name,
    b.authorName = :authorName,
    b.publicationYear = :pubYear,
    b.pageNumber = :pageNum,
    b.cost = :cost
    where b.id = :id
""")
    void updateBook(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("authorName") String authorName,
            @Param("pubYear") Integer publicationYear,
            @Param("pageNum") Integer pageNumber,
            @Param("cost") Integer cost
    );

    Book findById(long id);
}
