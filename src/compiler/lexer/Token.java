package compiler.lexer;

/**
 * The Token class pairs the type of token with the text 
 * associated with it.
 * @author Mike
 */
public class Token {

	public int type;
	public String text;
	private Lexer l;
	
	public Token(int type, String text, Lexer l) {
		this.type = type;
		this.text = text;
		this.l = l;
	}
	
	public String toString() {
		String tname = null;
		if(l instanceof ZMMLexer) 
			tname = ZMMLexer.tokenNames[type];
		else if(l instanceof ASMLexer)
			tname = ASMLexer.tokenNames[type];
		return "<'" + text + "', " + tname + ">";
	}
}
