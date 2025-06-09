package bookapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
public class BookControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnListOfBooks() throws Exception {
        when(bookService.getBooks()).thenReturn(List.of(new Book(1L, "Mock Book")));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Mock Book"));
    }

    @Test
    void shouldCreateNewBook() throws Exception {
        Book newBook = new Book(2L, "New Book");
        when(bookService.addBook(any())).thenReturn(newBook);

        mockMvc.perform(post("/books")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Book"));
    }
}
