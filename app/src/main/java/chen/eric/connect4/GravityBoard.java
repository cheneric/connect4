package chen.eric.connect4;

public class GravityBoard implements Board {
	public static final int DEFAULT_NUM_COLUMNS = 7;
	public static final int DEFAULT_NUM_ROWS = 6;
	public static final int DEFAULT_WIN_COUNT = 4;

	private static final char BOUNDARY_ROW_CHAR = '-';
	private static final String OPEN_COLUMN = "| ";
	private static final String CLOSE_COLUMN = " ";
	private static final String CLOSE_ROW = "|\n";
	private static final char NULL_TOKEN_CHAR = ' ';

	private final Column[] columns;
	private final int numberOfRows;
	private final int winCount;
	private final WinChecker[] winCheckers;

	public GravityBoard() {
		this(DEFAULT_NUM_COLUMNS, DEFAULT_NUM_ROWS, DEFAULT_WIN_COUNT);
	}

	public GravityBoard(int numberOfColumns, int numberOfRows, int winCount) {
		this.columns = createBoard(numberOfColumns, numberOfRows);
		this.numberOfRows = numberOfRows;
		this.winCount = winCount;
		this.winCheckers = new WinChecker[] {
			new VerticalWinChecker(),
			new HorizontalWinChecker(),
			new UpRightWinChecker(),
			new UpLeftWinChecker()};
	}

	protected Column[] createBoard(int numberOfColumns, int numberOfRows) {
		final Column[] columns = new Column[numberOfColumns];
		for (int count = 0; count < numberOfColumns; count++) {
			columns[count] = new Column(numberOfRows);
		}
		return columns;
	}

	@Override
	public BoardState move(Token token, int columnIndex) throws InvalidTokenException, InvalidColumnIndexException, ColumnFullException {
		try {
			final int rowIndex =
				columns[columnIndex]
					.add(token);
			return calculateBoardState(token, columnIndex, rowIndex);
		}
		catch (ArrayIndexOutOfBoundsException exception) {
			throw new InvalidColumnIndexException(exception);
		}
	}

	protected BoardState calculateBoardState(Token token, int columnIndex, int rowIndex) throws ArrayIndexOutOfBoundsException {
		BoardState boardState;
		if (isWinner(token, columnIndex, rowIndex)) {
			boardState = new BoardState(token);
		}
		else if (isFull()) {
			boardState = new BoardState(BoardState.State.draw);
		}
		else {
			boardState = new BoardState(BoardState.State.ongoing);
		}
		return boardState;
	}

	protected boolean isWinner(Token token, int columnIndex, int rowIndex) {
		boolean isWinner = false;
		for (final WinChecker winChecker : winCheckers) {
			if (winChecker.isWinner(token, columnIndex, rowIndex)) {
				isWinner = true;
				break;
			}
		}
		return isWinner;
	}

	protected boolean isFull() {
		final Column[] columns = this.columns;
		final int numberOfColumns = columns.length;
		for (int count = 0; count < numberOfColumns; count++) {
			if (!columns[count].isFull()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		try {
			final Column[] columns = this.columns;
			final int numberOfColumns = columns.length;
			final int numberOfRows = this.numberOfRows;

			final StringBuffer stringBuffer = new StringBuffer();
			for (int rowCount = numberOfRows - 1; rowCount >= 0; rowCount--) {
				if (numberOfColumns > 0) {
					for (int columnCount = 0; columnCount < numberOfColumns; columnCount++) {
						final Token token =
							columns[columnCount]
								.get(rowCount);
						stringBuffer.append(OPEN_COLUMN)
							.append(token == null ?
								NULL_TOKEN_CHAR :
								token.getCharValue())
							.append(CLOSE_COLUMN);
					}
					stringBuffer.append(CLOSE_ROW);
				}
			}
			appendBoundaryRow(stringBuffer);
			return stringBuffer.toString();
		}
		catch (ArrayIndexOutOfBoundsException exception) {
			throw new InvalidColumnIndexException(exception);
		}
	}

	protected void appendBoundaryRow(StringBuffer stringBuffer) {
		final int columnCharCount = OPEN_COLUMN.length() + CLOSE_COLUMN.length() + 1;
		final int boundaryRowLength = columns.length * columnCharCount + 1;
		for (int count = 0; count < boundaryRowLength; count++) {
			stringBuffer.append(BOUNDARY_ROW_CHAR);
		}
		stringBuffer.append("\n");
	}

	protected static class Column {
		private final Token[] tokens;
		private int size;

		public Column(int maxSize) {
			tokens = new Token[maxSize];
		}

		public Token get(int rowIndex) throws InvalidRowIndexException {
			try {
				return tokens[rowIndex];
			}
			catch (ArrayIndexOutOfBoundsException exception) {
				throw new InvalidRowIndexException(exception);
			}
		}

		/**
		 * Returns the current number of tokens in the column.
		 *
		 * @return the current number of tokens in the column.
		 */
		public int getSize() {
			return size;
		}

		/**
		 * Adds the token to the column.
		 *
		 * @param token the token to add to the column.
		 * @return the row index whre the token was added.
		 * @throws ColumnFullException if the column is full.
		 */
		public int add(Token token) throws ColumnFullException {
			final int size = this.size;
			if (isFull()) {
				throw new ColumnFullException("Column full (" + size + " >= " + tokens.length + ")");
			}
			tokens[this.size++] = token;
			return size;
		}

		public boolean isFull() {
			return size >= tokens.length;
		}
	}

	protected interface WinChecker {
		/**
		 * Returns the number of matching tokens counting canonical "left"
		 * of the last token played.
		 *
		 * @param token the last played token.
		 * @param startColumnIndex the last played column index.
		 * @param startRowIndex the last played row index.
		 * @return the number of matching tokens counting canonical "left"
		 * of the last token played.
		 */
		int countLeft(Token token, int startColumnIndex, int startRowIndex);

		/**
		 * Returns the number of matching tokens counting canonical "right"
		 * of the last token played.
		 *
		 * @param token the last played token.
		 * @param startColumnIndex the last played column index.
		 * @param startRowIndex the last played row index.
		 * @return the number of matching tokens counting canonical "right"
		 * of the last token played.
		 */
		int countRight(Token token, int startColumnIndex, int startRowIndex);

		/**
		 * Indicates whether the last token played resulted in a winner.
		 *
		 * @param token the last played token.
		 * @param startColumnIndex the last played column index.
		 * @param startRowIndex the last played row index.
		 * @return <code>true</code> if the last token played is the winner;
		 * otherwise, returns <code>false</code>.
		 */
		boolean isWinner(Token token, int startColumnIndex, int startRowIndex);
	}

	protected abstract class AbstractWinChecker implements WinChecker {
		@Override
		public boolean isWinner(Token token, int startColumnIndex, int startRowIndex) {
			return 1
				+ countLeft(token, startColumnIndex, startRowIndex)
				+ countRight(token, startColumnIndex, startRowIndex)
				>= GravityBoard.this.winCount;
		}
	}

	protected class VerticalWinChecker extends AbstractWinChecker implements WinChecker {
		/**
		 * Count matching tokens down.
		 */
		@Override
		public int countLeft(Token token, int startColumnIndex, int startRowIndex) {
			int matchingCount = 0;
			final Column column = GravityBoard.this.columns[startColumnIndex];
			for (int nextRowIndex = startRowIndex - 1; nextRowIndex >= 0; nextRowIndex--) {
				if (token.equals(
					column.get(nextRowIndex)))
				{
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}

		/**
		 * No matching tokens up from last played token due to gravity.
		 */
		@Override
		public int countRight(Token token, int startColumnIndex, int startRowIndex) {
			return 0;
		}
	}

	protected class HorizontalWinChecker extends AbstractWinChecker implements WinChecker {
		/**
		 * Count matching tokens left.
		 */
		@Override
		public int countLeft(Token token, int startColumnIndex, int startRowIndex) {
			int matchingCount = 0;
			for (int nextColumnIndex = startColumnIndex - 1; nextColumnIndex >= 0; nextColumnIndex--) {
				if (token.equals(
					GravityBoard.this.columns[nextColumnIndex]
						.get(startRowIndex)))
				{
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}

		/**
		 * Count matching tokens right.
		 */
		@Override
		public int countRight(Token token, int startColumnIndex, int startRowIndex) {
			final int numberOfColumns = GravityBoard.this.columns.length;
			int matchingCount = 0;
			for (int nextColumnIndex = startColumnIndex + 1; nextColumnIndex < numberOfColumns; nextColumnIndex++) {
				if (token.equals(
					GravityBoard.this.columns[nextColumnIndex]
						.get(startRowIndex)))
				{
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}
	}

	protected class UpRightWinChecker extends AbstractWinChecker implements WinChecker {
		/**
		 * Count matching tokens down and left.
		 */
		@Override
		public int countLeft(Token token, int startColumnIndex, int startRowIndex) {
			int matchingCount = 0;
			int nextColumnIndex = startColumnIndex - 1;
			int nextRowIndex = startRowIndex - 1;
			while (nextColumnIndex >= 0 && nextRowIndex >= 0) {
				if (token.equals(GravityBoard.this.columns[nextColumnIndex--].get(nextRowIndex--))) {
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}

		/**
		 * Count matching tokens up and right.
		 */
		@Override
		public int countRight(Token token, int startColumnIndex, int startRowIndex) {
			final int numberOfColumns = GravityBoard.this.columns.length;
			int matchingCount = 0;
			int nextColumnIndex = startColumnIndex + 1;
			int nextRowIndex = startRowIndex + 1;
			while (nextColumnIndex < numberOfColumns
				&& nextRowIndex < GravityBoard.this.columns[nextColumnIndex].getSize())
			{
				if (token.equals(GravityBoard.this.columns[nextColumnIndex++].get(nextRowIndex++))) {
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}
	}

	protected class UpLeftWinChecker extends AbstractWinChecker implements WinChecker {
		/**
		 * Count matching tokens down and right.
		 */
		@Override
		public int countLeft(Token token, int startColumnIndex, int startRowIndex) {
			final int numberOfColumns = GravityBoard.this.columns.length;
			int matchingCount = 0;
			int nextColumnIndex = startColumnIndex + 1;
			int nextRowIndex = startRowIndex - 1;
			while (nextColumnIndex < numberOfColumns && nextRowIndex >= 0) {
				if (token.equals(GravityBoard.this.columns[nextColumnIndex++].get(nextRowIndex--))) {
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}

		/**
		 * Count matching tokens up and left.
		 */
		@Override
		public int countRight(Token token, int startColumnIndex, int startRowIndex) {
			int matchingCount = 0;
			int nextColumnIndex = startColumnIndex - 1;
			int nextRowIndex = startRowIndex + 1;
			while (nextColumnIndex >= 0 && nextRowIndex < GravityBoard.this.columns[nextColumnIndex].getSize()) {
				if (token.equals(GravityBoard.this.columns[nextColumnIndex--].get(nextRowIndex++))) {
					++matchingCount;
				}
				else {
					break;
				}
			}
			return matchingCount;
		}
	}
}
