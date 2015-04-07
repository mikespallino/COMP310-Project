package compiler.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * ASMLexer is an lexer that tokenizes a string of input data.
 * @author Mike
 */
public class ASMLexer extends Lexer {
	
	public static final int REGISTER = 2;
	public static final int INSTRUCTION = 3;
	public static final int COMMA    = 4;
	public static final int IMMD     = 5;
	public static final int NWLN     = 6;
	public static String[] tokenNames = {"n/a", "<EOF>", "REGISTER", "INSTRUCTION", "COMMA", "IMMD", "NWLN"};
	
	private List<Token> parsedTokens;
	
	public ASMLexer(String input) {
		super(input);
		parsedTokens = new ArrayList<Token>();
	}
	
	public String getTokenName(int index) {
		return tokenNames[index];
	}
	
	boolean isR() {
		return (getC() == 'r' || getC() == 'R');
	}
	
	@Override
	public Token nextToken() {
		Token t;
		while(getC() != EOF) {
			switch(getC()) {
			case ' ':
			case '\t':
			case '\r':
				WS();
				continue;
			case '\n':
				consume();
				t = new Token(NWLN, "\n", this);
				parsedTokens.add(t);
				return t;
			case ',':
				consume();
				t = new Token(COMMA, ",", this);
				parsedTokens.add(t);
				return t;
			default:
				if(isLetter()) {
					return WORD();
				} else if(isR()){
					return REGISTER();
				} else if(isNumber()) {
					return IMMEDIATE_VALUE();
				}
				throw new Error("Invalid character: " + getC());
			}
		}
		t = new Token(EOF_TYPE, "<EOF>", this);
		parsedTokens.add(t);
		return t;
	}
	
	/**
	 * While the characters are numbers create an immediate value token.
	 * @return an immediate value token
	 */
	private Token IMMEDIATE_VALUE() {
		Token t;
		StringBuilder buf = new StringBuilder();
		int count = 0;
		do {
			buf.append(getC());
			count ++;
			consume();
			if(count == 8 && isNumber()) {
				throw new Error("Immediate values can only be 8 bits. Value: " +  buf.toString() + getC());
			}
		} while (isNumber());
		t = new Token(IMMD, buf.toString(), this);
		parsedTokens.add(t);
		return t;
	}

	/**
	 * While the characters are letters create an instruction token.
	 * @return instruction token.
	 */
	private Token WORD() {
		Token t;
		StringBuilder buf = new StringBuilder();
		do {
			buf.append(getC());
			consume();
		} while (isLetter() || getC() == 'r' || getC() == 'R');
		switch(buf.toString()) {
			case "ADD":
			case "SUB":
			case "MUL":
			case "DIV":
			case "AND":
			case "OR":
			case "XNOR":
			case "MOV":
			case "JZE":
			case "JZN":
			case "JZG":
			case "JZL":
			case "LOAD":
			case "STOR":
				t = new Token(INSTRUCTION, buf.toString(), this);
				parsedTokens.add(t);
				return t;
			default:
				throw new Error("Invalid instruction: " + buf.toString());
		}
	}
	
	/**
	 * While the character following an R is a number create a register token.
	 * @returna register token.
	 */
	private Token REGISTER() {
		Token t;
		StringBuilder buf = new StringBuilder();
		do {
			buf.append(getC());
			consume();
		} while (isNumber());
		t = new Token(REGISTER, buf.toString(), this);
		parsedTokens.add(t);
		return t;
	}
	
	/**
	 * While the character is whitespace, throw it out.
	 */
	private void WS() {
		while(getC() == ' ' || getC() == '\t' || getC() == '\r') {
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
