package compiler.lexer;

import java.util.ArrayList;
import java.util.List;

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
	public static final int IF      = 12;
	public static final int ELSE    = 13;
	public static final int GREATER = 14;
	public static final int LESS    = 15;
	public static final int OP      = 16;
	public static String[] tokenNames = {"n/a", "<EOF>", "INT", "NAME", "EQUALS", "VALUE", "SEMI", "WHILE", "OPAREN", "CPAREN", "OBRACK", "CBRACK", "IF", "ELSE", ">", "<", "OP"};
	
	private List<Token> parsedTokens;
	
	public ZMMLexer(String input) {
		super(input);
		 parsedTokens = new ArrayList<Token>();
	}

	@Override
	public Token nextToken() {
		while(getC() != EOF) {
			Token t;
			switch(getC()) {
			case ' ':
			case '\t':
			case '\r':
			case '\n':
				WS();
				continue;
			case ';':
				consume();
				t = new Token(SEMI, ";", this);
				parsedTokens.add(t);
				return t;
			case '(':
				consume();
				t = new Token(OPAREN, "(", this);
				parsedTokens.add(t);
				return t;
			case ')':
				consume();
				t = new Token(CPAREN, ")", this);
				parsedTokens.add(t);
				return t;
			case '[':
				consume();
				t = new Token(OBRACK, "[", this);
				parsedTokens.add(t);
				return t;
			case ']':
				consume();
				t = new Token(CBRACK, "]", this);
				parsedTokens.add(t);
				return t;
			case '=':
				consume();
				t = new Token(EQUALS, "=", this);
				parsedTokens.add(t);
				return t;
			case '>':
				consume();
				t = new Token(GREATER, ">", this);
				parsedTokens.add(t);
				return t;
			case '<':
				consume();
				t = new Token(LESS, "<", this);
				parsedTokens.add(t);
				return t;
			case '+':
				consume();
				t = new Token(OP, "+", this);
				parsedTokens.add(t);
				return t;
			case '-':
				consume();
				t = new Token(OP, "-", this);
				parsedTokens.add(t);
				return t;
			case '*':
				consume();
				t = new Token(OP, "*", this);
				parsedTokens.add(t);
				return t;
			case '/':
				consume();
				t = new Token(OP, "/", this);
				parsedTokens.add(t);
				return t;
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
		Token t;
		do {
			buf.append(getC());
			consume();
		} while (isLetter());
		switch(buf.toString().toUpperCase()) {
			case "WHILE":
				t = new Token(WHILE, "while", this);
				parsedTokens.add(t);
				return t;
			case "IF":
				t = new Token(IF, "if", this);
				parsedTokens.add(t);
				return t;
			case "ELSE":
				t = new Token(ELSE, "else", this);
				parsedTokens.add(t);
				return t;
			case "INT":
				t = new Token(INT, "int", this);
				parsedTokens.add(t);
				return t;
			default:
				t = new Token(NAME, buf.toString(), this);
				parsedTokens.add(t);
				return t;
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
		Token t = new Token(VALUE, buf.toString(), this);
		parsedTokens.add(t);
		return t;
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

	public List<Token> getParsedTokens() {
		return parsedTokens;
	}

	public void setParsedTokens(List<Token> parsedTokens) {
		this.parsedTokens = parsedTokens;
	}
	
}
