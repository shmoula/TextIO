package cz.shmoula.textio;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.shmoula.textio.domain.Book;
import cz.shmoula.textio.exception.BadRequestException;
import cz.shmoula.textio.exception.BookNotFoundException;
import cz.shmoula.textio.service.BookRepository;

/**
 * Prace s knihami
 * Popis API je na http://docs.textio.apiary.io/
 * @author vbalak
 *
 */
@Controller
public class BookController {
	public static final String HEADER_AUTHOR = "author";
	public static final String HEADER_TITLE = "title";

	final Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookRepository bookRepository;

	/**
	 * Ziskani seznamu vsech knizek v databazi
	 * @return
	 */
	@RequestMapping(value = "/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Iterable<Book> getBookList() {
		return bookRepository.findAll();
	}
	
	/**
	 * Vrati obsah knizky specifikovane jejim ID, nebo 404 v pripade jejiho nenalezeni
	 * @param id Identifikator knizky
	 * @return
	 */
	@RequestMapping(value = "/book/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<String> getBook(@PathVariable("id") Long id) {
		Book book = bookRepository.findOne(id);
		
		if(book == null)
			throw new BookNotFoundException("Kniha nenalezena: " + id.toString());
			
		return new ResponseEntity<String>(book.getContent(), new HttpHeaders(), HttpStatus.OK);
	}
	
	/**
	 * Vytvori novou knizku
	 * @param content Telo pozadavku - obsah knizky v textove podobe
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/book", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ResponseEntity<Book> postBook(@RequestBody String content, HttpServletRequest request) {
		String author = request.getHeader(HEADER_AUTHOR);
		String title = request.getHeader(HEADER_TITLE);
		
		if(author == null || author.isEmpty() || title == null || title.isEmpty())
			throw new BadRequestException("Malo poskytnutych informaci. Je nutne vyplnit hlavicky 'author' a 'title'.");
		
		Book book = new Book();
		book.setAuthor(author);
		book.setTitle(title);
		book.setContent(content);
			
		book = bookRepository.save(book);
		
		return new ResponseEntity<Book>(book, new HttpHeaders(), HttpStatus.OK);
	}
}