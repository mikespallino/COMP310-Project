package compiler.lexer;

import compiler.parser.BacktrackParser;

public class Test {

	public static void main(String[] args) {
		//Works!
		String input = "ADD R0, R1, R2 \n"
				 + "STOR R3, 563763 \n"
				 + "JZE R4, 563763 \n";
		//Doesn't work, duh!
		String input2 = "ADD R0, R1, R2 \n"
					 + "STOR R3, 563763 \n"
					 + "JZE R4, 563763 \n"
					 + "OR";
		//Doesn't work, duh (x2)!
		String input3 = "ADD R0, R1, R2, R7 \n"
					 + "STOR R3, 563763 \n"
					 + "JZE R4, 563763 \n";
		ListLexer lexer = new ListLexer(input3);
		BacktrackParser parser = new BacktrackParser(lexer);
		try {
			parser.stats();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
