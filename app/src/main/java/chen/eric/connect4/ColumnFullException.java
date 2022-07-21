package chen.eric.connect4;

/**
 * Error adding token to a full column.
 */
public class ColumnFullException extends InvalidMoveException {
	public ColumnFullException() {}

	public ColumnFullException(String message) {
		super(message);
	}

	public ColumnFullException(Throwable cause) {
		super(cause);
	}

	public ColumnFullException(String message, Throwable cause) {
		super(message, cause);
	}
}
