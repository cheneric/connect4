package chen.eric.connect4;

/**
 * Invalid game move.
 */
public class InvalidMoveException extends RuntimeException {
	public InvalidMoveException() {}

	public InvalidMoveException(String message) { super(message); }

	public InvalidMoveException(Throwable cause) { super(cause); }

	public InvalidMoveException(String message, Throwable cause) { super(message, cause); }
}
