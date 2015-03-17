package compiler.lexer;

import compiler.parser.BacktrackParser;

public class Test {

	public static void main(String[] args) {
		String input = "ADD R2, R3, R4 \n"
					 + "STOR R5, 563763 \n"
					 + "JZE R4, 3";
		ListLexer lexer = new ListLexer(input);
		BacktrackParser parser = new BacktrackParser(lexer);
		try {
			parser.stats();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
