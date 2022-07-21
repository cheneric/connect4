package chen.eric.connect4;

/**
 * Error referencing a row index off the board.
 */
public class InvalidRowIndexException extends InvalidColumnIndexException {
	public InvalidRowIndexException() {}

	public InvalidRowIndexException(String message) { super(message); }

	public InvalidRowIndexException(Throwable cause) {
		super(cause);
	}

	public InvalidRowIndexException(String message, Throwable cause) {
		super(message, cause);
	}
}
