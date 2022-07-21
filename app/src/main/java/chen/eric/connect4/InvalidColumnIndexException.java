package chen.eric.connect4;

/**
 * Error referencing a column index off the board.
 */
public class InvalidColumnIndexException extends InvalidIndexException {
	public InvalidColumnIndexException() {}

	public InvalidColumnIndexException(String message) {
		super(message);
	}

	public InvalidColumnIndexException(Throwable cause) {
		super(cause);
	}

	public InvalidColumnIndexException(String message, Throwable cause) {
		super(message, cause);
	}
}
