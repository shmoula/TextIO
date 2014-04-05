package cz.shmoula.textio.exception;


public class BookNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5065061862055911722L;

	public BookNotFoundException(String message) {
		super(message);
	}
}
