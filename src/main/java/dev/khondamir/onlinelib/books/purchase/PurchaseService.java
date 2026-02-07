package dev.khondamir.onlinelib.books.purchase;

import dev.khondamir.onlinelib.books.BookController;
import dev.khondamir.onlinelib.books.BookService;
import dev.khondamir.onlinelib.users.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class PurchaseService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseService.class);


    private final BookService bookService;

    private final PurchaseRepository purchaseRepository;

    public PurchaseService(BookService bookService, PurchaseRepository purchaseRepository) {
        this.bookService = bookService;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public void performBookPurchase(
            User user,
            Long bookId
    ){
        var book = bookService.findById(bookId);
        if (purchaseRepository.existsByUserIdAndBookId(user.id(), bookId)) {
            throw new IllegalArgumentException("The user already has this book");
        }
        var purchase = new BookPurchaseEntity(
                null,
                bookId,
                user.id(),
                Timestamp.from(Instant.now()),
                book.cost()
        );
        purchaseRepository.save(purchase);
        log.info("Book has been successfully purchased: userId={}, bookId={}", user.id(), bookId);
    }
}
