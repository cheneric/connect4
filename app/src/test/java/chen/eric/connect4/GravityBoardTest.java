package chen.eric.connect4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GravityBoardTest {
	/**
	 * Tests that {@link GravityBoard#move(Token, int)} throws {@link InvalidColumnIndexException}
	 * for negative or over-the-limit column indexes.
	 */
	@Test
	protected void testMoveInvalidColumnIndexException() {
		final int NUMBER_OF_COLUMNS = 2;
		final GravityBoard board = new GravityBoard(NUMBER_OF_COLUMNS, 3, 2);

		assertThrows(
			InvalidColumnIndexException.class,
			() -> board.move(Token.Black, -1),
			"Failed to throw InvalidColumnIndexException for negative column index");

		final int OVER_LIMIT_COLUMN_INDEX = NUMBER_OF_COLUMNS + 1;
		assertThrows(
			InvalidColumnIndexException.class,
			() -> board.move(Token.Red, OVER_LIMIT_COLUMN_INDEX),
			"Failed to throw InvalidColumnIndexException for over limit column index (" + OVER_LIMIT_COLUMN_INDEX + ")");
	}

	/**
	 * Tests that {@link GravityBoard#move(Token, int)} throws {@link ColumnFullException}
	 * when a token is added to a full column.
	 */
	@Test
	protected void testMoveColumnFullException() {
		final int NUMBER_OF_COLUMNS = 3;
		final int NUMBER_OF_ROWS = 2;
		final int COLUMN_INDEX = NUMBER_OF_COLUMNS / 2;
		final GravityBoard board = new GravityBoard(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS, 2);
		for (int count = 0; count < NUMBER_OF_ROWS; count++) {
			board.move(Token.Black, COLUMN_INDEX);
		}
		assertThrows(
			ColumnFullException.class,
			() -> board.move(Token.Red, COLUMN_INDEX),
			"Failed to throw ColumnFullException");
	}

	@Test
	protected void testToString() {
		final GravityBoard board0_0 = new GravityBoard(0,0, 0);
		assertEquals("""
				-
				""",
			board0_0.toString(),
			"0 column, 0 row board string representation mismatch");

		final GravityBoard board0_1 = new GravityBoard(0,1, 0);
		assertEquals("""
				-
				""",
			board0_1.toString(),
			"0 column, 1 row board string representation mismatch");

		final GravityBoard board1_0 = new GravityBoard(1,0, 0);
		assertEquals("""
				-----
				""",
			board1_0.toString(),
			"1 column, 0 row board string representation mismatch");

		final GravityBoard board3_2 = new GravityBoard(3,2, 2);
		assertEquals("""
			|   |   |   |
			|   |   |   |
			-------------
			""",
			board3_2.toString(),
			"Empty 3 column, 2 row board string representation mismatch");
		board3_2.move(Token.Black, 0);
		board3_2.move(Token.Red, 1);
		board3_2.move(Token.Black, 2);
		board3_2.move(Token.Red, 2);
		board3_2.move(Token.Black, 1);
		board3_2.move(Token.Red, 0);
		assertEquals("""
			| R | B | R |
			| B | R | B |
			-------------
			""",
			board3_2.toString(),
			"Filled 3 column, 2 row board string representation mismatch");
	}

	/**
	 * Tests checking vertical win condition.`
	 */
	@Test
	protected void testVerticalWinChecker() {
		final GravityBoard board = new GravityBoard(7, 6, 4);
 		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState(), "Vertical 1");
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 1).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState(), "Vertical 2");
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 2).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState(), "Vertical 3");
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState());
		final BoardState boardState = board.move(Token.Black, 0);
		assertEquals(BoardState.State.winner, boardState.getState(), "Expected win state not reported");
		assertEquals(Token.Black, boardState.getWinner(), "Winning token did not match expected");
	}

	/**
	 * Tests checking horizontal win condition.
	 */
	@Test
	protected void testHorizontalWinChecker() {
		final GravityBoard board = new GravityBoard(7, 6, 4);
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 1).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 2).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 4).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 5).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 6).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 2).getState(), "Horizontal 1");
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 1).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState(), "Horizontal 2");
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 1).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 5).getState(), "Horizontal 4");
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 1).getState());
		final BoardState boardState = board.move(Token.Red, 4);
		assertEquals(BoardState.State.winner, boardState.getState(), "Expected win state not reported");
		assertEquals(Token.Red, boardState.getWinner(), "Winning token did not match expected");
	}

	/**
	 * Tests checking up right diagonal win condition.
	 */
	@Test
	protected void testUpRightWinChecker() {
		final GravityBoard board = new GravityBoard(7, 6, 4);
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 4).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 5).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 6).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState(), "Up Right 1");
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 4).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 5).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 6).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 5).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 5).getState(), "Up Right 3");
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 6).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 6).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 6).getState(), "Up Right 4");
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState());
		final BoardState boardState = board.move(Token.Red, 4);
		assertEquals(BoardState.State.winner, boardState.getState(), "Expected win state not reported");
		assertEquals(Token.Red, boardState.getWinner(), "Winning token did not match expected");
	}

	/**
	 * Tests checking up left diagonal win condition.
	 */
	@Test
	protected void testUpLeftWinChecker() {
		final GravityBoard board = new GravityBoard(7, 6, 4);
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 0).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 0).getState(), "Up Left 4");
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 1).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 1).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 2).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 3).getState(), "Up Left 1");
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState());
		assertEquals(BoardState.State.ongoing, board.move(Token.Black, 2).getState(), "Up Left 2");
		assertEquals(BoardState.State.ongoing, board.move(Token.Red, 3).getState());
		final BoardState boardState = board.move(Token.Black, 1);
		assertEquals(BoardState.State.winner, boardState.getState(), "Expected win state not reported");
		assertEquals(Token.Black, boardState.getWinner(), "Winning token did not match expected");
	}
}
