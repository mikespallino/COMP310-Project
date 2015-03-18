package compiler.lexer;

public class ListLexer extends Lexer {
	
	public static final int REGISTER = 2;
	public static final int INSTRUCTION = 3;
	public static final int COMMA    = 4;
	public static final int IMMD     = 5;
	public static final int NWLN     = 6;
	public static String[] tokenNames = {"n/a", "<EOF>", "REGISTER", "INSTRUCTION", "COMMA", "IMMD", "NWLN"};
	
	public ListLexer(String input) {
		super(input);
	}
	
	public String getTokenName(int index) {
		return tokenNames[index];
	}
	
	boolean isLetter() {
		return (getC() >= 'a' && getC() <= 'z') || (getC() >= 'A' && getC() <= 'Z') && getC() != 'r' && getC() != 'R';
	}
	boolean isNumber() {
		return (getC() >= '0' && getC() <= '9');
	}
	
	boolean isR() {
		return (getC() == 'r' || getC() == 'R');
	}
	
	public Token nextToken() {
		while(getC() != EOF) {
			switch(getC()) {
			case ' ':
			case '\t':
			case '\r':
				WS();
				continue;
			case '\n':
				consume();
				return new Token(NWLN, "\n");
			case ',':
				consume();
				return new Token(COMMA, ",");
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
		return new Token(EOF_TYPE, "<EOF>");
	}
	
	private Token IMMEDIATE_VALUE() {
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
		return new Token(IMMD, buf.toString());
	}

	private Token WORD() {
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
				return new Token(INSTRUCTION, buf.toString());
			default:
				throw new Error("Invalid instruction: " + buf.toString());
		}
	}
	
	private Token REGISTER() {
		StringBuilder buf = new StringBuilder();
		do {
			buf.append(getC());
			consume();
		} while (isNumber());
		return new Token(REGISTER, buf.toString());
	}
	
	private void WS() {
		while(getC() == ' ' || getC() == '\t' || getC() == '\r') {
				consume();
		}
	}
}
