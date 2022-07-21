package chen.eric.connect4;

public interface Board {
	/**
	 * Make a move with <code>token</code> at <code>columnIndex</code>.
	 *
	 * @param token the token to make the move with.
	 * @param columnIndex the column index to make the move at.
	 * @return BoardState the board state after the move.
	 * @throws InvalidTokenException if <code>token</code> is <code>null</code>.
	 * @throws InvalidColumnIndexException if <code>columnIndex</code> is invalid.
	 * @throws ColumnFullException if <code>columnIndex</code> is full.
	 */
	BoardState move(Token token, int columnIndex) throws InvalidTokenException, InvalidColumnIndexException, ColumnFullException;
}
