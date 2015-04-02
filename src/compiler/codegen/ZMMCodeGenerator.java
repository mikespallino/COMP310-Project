package compiler.codegen;

import java.util.List;

import compiler.lexer.Token;
import compiler.lexer.ZMMLexer;

public class ZMMCodeGenerator implements CodeGenerator {

	@Override
	public String generate(List<Token> tokens) throws InvalidTokenException {
		for(Token t : tokens) {
			switch(t.type) {
			}
		}
		return null;
	}

	
	
}
