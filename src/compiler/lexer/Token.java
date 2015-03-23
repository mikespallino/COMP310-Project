package compiler.lexer;

/**
 * The Token class pairs the type of token with the text 
 * associated with it.
 * @author Mike
 */
public class Token {

	public int type;
	public String text;
	
	public Token(int type, String text) {
		this.type = type;
		this.text = text;
	}
	
	public String toString() {
		String tname = ASMLexer.tokenNames[type];
		return "<'" + text + "', " + tname + ">";
	}
}
