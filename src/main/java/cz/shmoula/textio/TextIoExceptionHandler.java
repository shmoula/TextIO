package cz.shmoula.textio;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cz.shmoula.textio.exception.BadRequestException;
import cz.shmoula.textio.exception.BookNotFoundException;

/**
 * Generator chybovych odpovedi v pripade problemu
 * @author vbalak
 *
 */
@ControllerAdvice
public class TextIoExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
	 * Nenalezeni knihy - vraci 404 a prazdne JSON telo
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ BookNotFoundException.class })
	protected ResponseEntity<Object> handleNotFoundException(RuntimeException e, WebRequest request) {
		BookNotFoundException exception = (BookNotFoundException) e;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("error", exception.getMessage());

		return handleExceptionInternal(e, "{}", headers, HttpStatus.NOT_FOUND, request);
	}
	
	/**
	 * Neuplne specifikovany pozadavek (napr pri chybejicim parametru pri vytvareni knihy
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ BadRequestException.class })
	protected ResponseEntity<Object> handleBadRequestException(RuntimeException e, WebRequest request) {
		BadRequestException exception = (BadRequestException) e;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("error", exception.getMessage());

		return handleExceptionInternal(e, "{}", headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * Interpretace NPE
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ NullPointerException.class })
	protected ResponseEntity<Object> handleNullPointerException(RuntimeException e, WebRequest request) {
		BadRequestException exception = (BadRequestException) e;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("error", exception.getMessage());

		return handleExceptionInternal(e, "{}", headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
