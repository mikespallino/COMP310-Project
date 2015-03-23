package compiler.parser;

/**
 * Generic exception to be thrown when the Token doesn't match.
 * @author Mike
 */
public class MismatchedTokenException extends Exception {

	private static final long serialVersionUID = 1L;

	public MismatchedTokenException(String message) {
		super(message);
	}
	
}
