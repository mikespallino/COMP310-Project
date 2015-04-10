package compiler.lexer;

/**
 * Generic abstract Lexer class. It keeps track of where we 
 * are in the input String stream. 
 * @author Mike
 */
public abstract class Lexer {

	public static final char EOF = (char) -1;
	public static final int EOF_TYPE = 1;
	protected String input;
	private int pointer = 0;
	private char c;
	
	public Lexer(String input) {
		this.input = input;
		setC(input.charAt(pointer));
	}
	
	/**
	 * consume() increments the position in the string stream.
	 * When it gets to the end it will put an end of file character
	 * on to the end of the stream.
	 */
	public void consume() {
		pointer++;
		if(pointer >= input.length()) {
			setC(EOF);
		} else {
			setC(input.charAt(pointer));
		}
	}
	
	/**
	 * match(char target) will match the current character with
	 * a target character and consumes it.
	 * @param target
	 */
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
	
	boolean isLetter() {
		return (getC() >= 'a' && getC() <= 'z') || (getC() >= 'A' && getC() <= 'Z') && getC() != 'r' && getC() != 'R';
	}
	
	boolean isNumber() {
		return ((getC() >= '0' && getC() <= '9') || ((getC() >= 'a' && getC() <= 'f') || (getC() >= 'A' && getC() <= 'F')));
	}
}
