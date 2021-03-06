package compiler.codegen;

import java.util.List;

import compiler.lexer.Token;

public interface CodeGenerator {
	
	public String generate(List<Token> tokens) throws InvalidTokenException;
	public static final int BUNDLE_SIZE = 4;
	
}
