package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import compiler.lexer.ZMMLexer;
import compiler.parser.MismatchedTokenException;
import compiler.parser.ZMMParser;

public class ZMMParserTest {

	@Test
	public void testStats() {
		Scanner scan;
		String input = "";
		try {
			scan = new Scanner(new File("res/test.zmm"));
			input = "";
			while(scan.hasNext()) {
				input += scan.next();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		ZMMLexer lexer = new ZMMLexer(input);
		ZMMParser parser = new ZMMParser(lexer);
		try {
			parser.stats();
		} catch (MismatchedTokenException e) {
			e.printStackTrace();
		}
	}

}
