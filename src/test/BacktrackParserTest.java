package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import compiler.lexer.Lexer;
import compiler.lexer.ASMLexer;
import compiler.parser.ASMBacktrackParser;
import compiler.parser.MismatchedTokenException;
import compiler.parser.Parser;

public class BacktrackParserTest {
	
	@Test
	public void testStats() {
		Lexer lexer1, lexer2, lexer3, lexer4;
		Parser parser;
		
		// Works!
		String input1 = "ADD R0, R1, R2 \n"
					 + "STOR R3, 563763 \n"
					 + "JZE R4, 563763 \n";
		// Doesn't work, duh!
		// Can't have Arithmetic Instruction with no parameters!
		String input2 = "ADD R0, R1, R2 \n"
					 + "STOR R3, 563763 \n"
					 + "JZE R4, 563763 \n"
					 + "OR";
		// Doesn't work, duh (x2)!
		// Can't have more than 3 parameters to an Arithmetic Intruction!
		String input3 = "ADD R0, R1, R2, R7 \n"
					  + "STOR R3, 563763 \n"
					  + "JZE R4, 563763 \n";
		// Works!
		String input4 = "ADD R0, R1, R2 \n"
						+ "STOR R3 \n"
						+ "JZE R4, 563763 \n"; 
		
		lexer1 = new ASMLexer(input1);
		lexer2 = new ASMLexer(input2);
		lexer3 = new ASMLexer(input3);
		lexer4 = new ASMLexer(input4);
		
		parser = new ASMBacktrackParser(lexer1);
		try {
			((ASMBacktrackParser) parser).stats();
		} catch (MismatchedTokenException e) {
			assertTrue(e == null);
		}
		
		parser = new ASMBacktrackParser(lexer2);
		try {
			((ASMBacktrackParser) parser).stats();
		} catch (MismatchedTokenException e) {
			assertTrue(e != null);
		}
		
		parser = new ASMBacktrackParser(lexer3);
		try {
			((ASMBacktrackParser) parser).stats();
		} catch (MismatchedTokenException e) {
			assertTrue(e != null);
		}
		
		parser = new ASMBacktrackParser(lexer4);
		try {
			((ASMBacktrackParser) parser).stats();
		} catch (MismatchedTokenException e) {
			assertTrue(e == null);
		}
	}

}
