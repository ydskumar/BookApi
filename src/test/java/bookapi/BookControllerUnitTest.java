package bookapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.util.List;

import org.junit.jupiter.api.Test;

public class BookControllerUnitTest {

	@Test
	void testGetAllBooks() {
		// Arrange
		BookService mockService = mock(BookService.class);
		BookController controller = new BookController(mockService);

		List<Book> mockBooks = List.of(new Book(1L, "Mock Book"));
		when(mockService.getBooks()).thenReturn(mockBooks);

		// Act
		List<Book> result = controller.getAllBooks();

		// Assert
		assertEquals(1, result.size());
		assertEquals("Mock Book", result.get(0).getTitle());
	}

	@Test
	void testCreateBook() {
		// Arrange
		BookService mockService = mock(BookService.class);
		BookController controller = new BookController(mockService);

		Book book = new Book(2L, "New Book");
		when(mockService.addBook(book)).thenReturn(book);

		// Act
		Book result = controller.createBook(book);

		// Assert
		assertNotNull(result);
		assertEquals("New Book", result.getTitle());
	}
}
