package compiler.lexer;


public abstract class Lexer {

	public static final char EOF = (char) -1;
	public static final int EOF_TYPE = 1;
	String input;
	int pointer = 0;
	private char c;
	
	public Lexer(String input) {
		this.input = input;
		setC(input.charAt(pointer));
	}
	
	public void consume() {
		pointer++;
		if(pointer >= input.length()) {
			setC(EOF);
		} else {
			setC(input.charAt(pointer));
		}
	}
	
	public void match(char target) {
		if(getC() == target) {
			consume();
		} else {
			throw new Error("Expecting: " + target + "; Found: " + getC());
		}
	}
	
	public abstract Token nextToken();
	public abstract String getTokenName(int tokenType);

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}
}
