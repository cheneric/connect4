package chen.eric.connect4;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Game implements Runnable {

	private final Board board;
	private final int maxColumnIndex;

	public Game() {
		this.board = new GravityBoard();
		this.maxColumnIndex = GravityBoard.DEFAULT_NUM_COLUMNS - 1;
	}

	@Override
	public void run() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			Token player = Token.Black;
			BoardState boardState = null;
			System.out.println(board);
			do {
				System.out.println(player + "'s turn.");
				boolean isColumnIndexValid = false;
				do {
					final int columnIndex = readColumnIndex(scanner);
					try {
						boardState = board.move(player, columnIndex);
						isColumnIndexValid = true;
					} catch (ColumnFullException exception) {
						System.out.println("Column " + columnIndex + " is full - please choose another column.\n");
					}
				}
				while (!isColumnIndexValid);
				System.out.println("\n" + board);
				player = nextPlayer(player);
			}
			while (boardState.getState() == BoardState.State.ongoing);

			switch (boardState.getState()) {
				case draw:
					System.out.println("Draw.");
					break;
				case winner:
					System.out.println(boardState.getWinner() + " wins!");
			}
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	protected int readColumnIndex(Scanner scanner) {
		int intValue = -1;
		while (intValue < 0 || intValue > maxColumnIndex) {
			try {
				System.out.print("What column would you like to play in (0-" + maxColumnIndex + ")? ");
				intValue = scanner.nextInt();
			}
			catch (InputMismatchException exception) {
				// skip invalid input
				scanner.next();
			}
		}
		return intValue;
	}

	protected Token nextPlayer(Token currentPlayer) {
		return (currentPlayer == Token.Black ? Token.Red : Token.Black);
	}

	public static void main(String... args) {
		final Game game = new Game();
		game.run();
	}
}
