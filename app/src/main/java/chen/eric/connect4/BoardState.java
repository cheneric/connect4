package chen.eric.connect4;

public class BoardState {
	public enum State {
		ongoing,
		draw,
		winner
	}

	private final State state;
	private final Token winner;

	public BoardState(State state) {
		if (state == null) {
			throw new IllegalArgumentException("Board state cannot be null");
		}
		if (state == State.winner) {
			throw new IllegalArgumentException("For winner state, use BoardState(Token) constructor");
		}
		this.state = state;
		this.winner = null;
	}

	public BoardState(Token winner) {
		if (winner == null) {
			throw new IllegalArgumentException("Winner cannot be null");
		}
		this.state = State.winner;
		this.winner = winner;
	}

	public State getState() {
		return state;
	}

	public Token getWinner() {
		return winner;
	}
}
