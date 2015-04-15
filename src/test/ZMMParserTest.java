package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import compiler.lexer.ASMLexer;
import compiler.lexer.ZMMLexer;
import compiler.parser.ASMBacktrackParser;
import compiler.parser.MismatchedTokenException;
import compiler.parser.ZMMParser;

public class ZMMParserTest {

	@Test
	public void testStats() {
		Scanner scan;
		String input = "";
		try {
			scan = new Scanner(new File("res/test.zmm"));
			while(scan.hasNextLine()) {
				input += scan.nextLine();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		ZMMLexer lexer = new ZMMLexer(input);
		ZMMParser parser = new ZMMParser(lexer);
		try {
			parser.stats();
			String asm = parser.getCodeGenerator().getOutput();
			System.out.println(asm +"\n");
			
			ASMBacktrackParser asmParser = new ASMBacktrackParser(new ASMLexer(asm));
			String output = asmParser.stats();
			//Write this to a file!
			System.out.println(output);
		} catch (MismatchedTokenException e) {
			e.printStackTrace();
		}
	}

}
