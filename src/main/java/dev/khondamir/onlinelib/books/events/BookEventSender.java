package dev.khondamir.onlinelib.books.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookEventSender {

    private static final Logger log = LoggerFactory.getLogger(BookEventSender.class);

    private final KafkaTemplate<Long, BookKafkaEvent> kafkaTemplate;

    public BookEventSender(KafkaTemplate<Long, BookKafkaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(BookKafkaEvent bookKafkaEvent) {
        log.info("Sending event: event={}", bookKafkaEvent);
        var result = kafkaTemplate.send(
                "books-topic",
                bookKafkaEvent.bookId(),
                bookKafkaEvent
        );

        result.thenAccept(sendResult->{
            log.info("Send successful");
        });
    }
}
