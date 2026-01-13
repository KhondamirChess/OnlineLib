package dev.khondamir.onlinelib;

import dev.khondamir.onlinelib.books.BookDto;
import dev.khondamir.onlinelib.books.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BookDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateBook() throws Exception {
        var book = new BookDto(
                null, "some-book", "Pasha", 2024, 100, 6000
        );

        String bookJson = objectMapper.writeValueAsString(book);

        String createdBookJson = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto bookDtoResponse = objectMapper.readValue(createdBookJson, BookDto.class);

        Assertions.assertNotNull(bookDtoResponse.id());
        Assertions.assertEquals(book.name(), bookDtoResponse.name());
    }

    @Test
    void shouldReturnNotFoundWhenBookNotPresent() throws Exception {
        mockMvc.perform(get("/books/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }
}