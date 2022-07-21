package chen.eric.connect4;

/**
 * Error referencing index off the board.
 */
public class InvalidIndexException extends InvalidMoveException {
	public InvalidIndexException() {}

	public InvalidIndexException(String message) { super(message); }

	public InvalidIndexException(Throwable cause) {
		super(cause);
	}

	public InvalidIndexException(String message, Throwable cause) {
		super(message, cause);
	}
}
