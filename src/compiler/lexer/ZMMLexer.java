package compiler.lexer;

/**
 * ZMMLexer is a Lexer implementation for ZMM.
 * @author Mike
 */
public class ZMMLexer extends Lexer {

	public static final int INT     = 2;
	public static final int NAME    = 3;
	public static final int EQUALS  = 4;
	public static final int VALUE   = 5;
	public static final int SEMI    = 6;
	public static final int WHILE   = 7;
	public static final int OPAREN  = 8;
	public static final int CPAREN  = 9;
	public static final int OBRACK  = 10;
	public static final int CBRACK  = 11;
	public static final int FOR     = 12;
	public static final int IF      = 13;
	public static final int ELSE    = 14;
	public static final int GREATER = 15;
	public static final int LESS    = 16;
	public static final int OP      = 17;
	public static String[] tokenNames = {"n/a", "<EOF>", "INT", "NAME", "EQUALS", "VALUE", "SEMI", "WHILE", "OPAREN", "CPAREN", "OBRACK", "CBRACK", "FOR", "IF", "ELSE", ">", "<", "OP"};
	
	public ZMMLexer(String input) {
		super(input);
	}

	@Override
	public Token nextToken() {
		while(getC() != EOF) {
			switch(getC()) {
			case ' ':
			case '\t':
			case '\r':
			case '\n':
				WS();
				continue;
			case ';':
				consume();
				return new Token(SEMI, ";", this);
			case '(':
				consume();
				return new Token(OPAREN, "(", this);
			case ')':
				consume();
				return new Token(CPAREN, ")", this);
			case '[':
				consume();
				return new Token(OBRACK, "[", this);
			case ']':
				consume();
				return new Token(CBRACK, "]", this);
			case '=':
				consume();
				return new Token(EQUALS, "=", this);
			case '>':
				consume();
				return new Token(GREATER, ">", this);
			case '<':
				consume();
				return new Token(LESS, "<", this);
			case '+':
				consume();
				return new Token(OP, "+", this);
			case '-':
				consume();
				return new Token(OP, "-", this);
			case '*':
				consume();
				return new Token(OP, "*", this);
			case '/':
				consume();
				return new Token(OP, "/", this);
			default:
				if(isLetter()) {
					return NAME();
				} else if(isNumber()) {
					return VALUE();
				}
				throw new Error("Invalid character: " + getC());
			}
		}
		return new Token(EOF_TYPE, "<EOF>", this);
	}
	
	/**
	 * While the characters are letters create a while, for, if, else, int, or name token.
	 * @return token.
	 */
	private Token NAME() {
		StringBuilder buf = new StringBuilder();
		do {
			buf.append(getC());
			consume();
		} while (isLetter());
		switch(buf.toString().toUpperCase()) {
			case "WHILE":
				return new Token(WHILE, "while", this);
			case "FOR":
				return new Token(FOR, "for", this);
			case "IF":
				return new Token(IF, "if", this);
			case "ELSE":
				return new Token(ELSE, "else", this);
			case "INT":
				return new Token(INT, "int", this);
			default:
				return new Token(NAME, buf.toString(), this);
		}
	}
	
	/**
	 * While the characters are numbers create an value token.
	 * @return value token.
	 */
	private Token VALUE() {
		StringBuilder buf = new StringBuilder();
		do {
			buf.append(getC());
			consume();
		} while (isNumber());
		return new Token(VALUE, buf.toString(), this);
	}

	@Override
	public String getTokenName(int tokenType) {
		return tokenNames[tokenType];
	}

	/**
	 * While the character is whitespace, throw it out.
	 */
	private void WS() {
		while(getC() == ' ' || getC() == '\t' || getC() == '\r' || getC() == '\n') {
				consume();
		}
	}
	
}
