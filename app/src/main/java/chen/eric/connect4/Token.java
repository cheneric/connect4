package chen.eric.connect4;

public enum Token {
	Black('B'),
	Red('R');

	private final char charValue;

	private Token(char charValue) {
		this.charValue = charValue;
	}

	public char getCharValue() {
		return charValue;
	}
}
