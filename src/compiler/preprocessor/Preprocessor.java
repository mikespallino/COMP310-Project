package compiler.preprocessor;

import java.util.ArrayList;
import java.util.List;

import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.ZMMLexer;

public class Preprocessor {
	
	private List<Token> input;
	private Lexer lexerType;
	
	public Preprocessor(List<Token> input, Lexer lexerType) {
		this.input = input;
		this.lexerType = lexerType;
	}
	
	public List<Token> process() {
		for(int i = 0; i < input.size(); i++) {
			if(lookAhead(i).type == ZMMLexer.NAME && lookAhead(i+1).type == ZMMLexer.OP
					&& lookAhead(i+2).type == ZMMLexer.OP && lookAhead(i+1).text.equals("+")
					&& lookAhead(i+2).text.equals("+")) {
				input.set(i+1, new Token(ZMMLexer.EQUALS, "=", lexerType));
				input.set(i+2, new Token(ZMMLexer.NAME, lookAhead(i).text, lexerType));
				input.add(i+2, new Token(ZMMLexer.OP, "+", lexerType));
				input.add(i+3, new Token(ZMMLexer.VALUE, "1", lexerType));
			}
		}
		return null;
	}
	
	public Token lookAhead(int i) {
		return input.get(i);
	}

}
