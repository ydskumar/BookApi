package bookapi;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

	  public List<Book> books = new ArrayList<>();

	    public List<Book> getBooks() {
	        return books;
	    }

	    public Book addBook(Book book ){
	        books.add(book);
	        return book;
	    }
}
