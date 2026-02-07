package dev.khondamir.onlinelib.books.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<BookPurchaseEntity, Long> {

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

}
