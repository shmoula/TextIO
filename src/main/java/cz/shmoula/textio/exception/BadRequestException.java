package cz.shmoula.textio.exception;

public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = -3473899552808302327L;

	public BadRequestException(String message) {
		super(message);
	}
}
