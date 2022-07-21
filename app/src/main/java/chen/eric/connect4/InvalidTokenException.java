package chen.eric.connect4;

/**
 * An invalid {@link Token} was received.
 */
public class InvalidTokenException extends InvalidMoveException {
	public InvalidTokenException() {}

	public InvalidTokenException(String message) { super(message); }

	public InvalidTokenException(Throwable cause) { super(cause); }

	public InvalidTokenException(String message, Throwable cause) { super(message, cause); }
}
