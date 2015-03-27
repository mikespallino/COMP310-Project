package test;

import org.junit.Test;

import compiler.lexer.ZMMLexer;
import compiler.parser.MismatchedTokenException;
import compiler.parser.ZMMParser;

public class ZMMParserTest {

	@Test
	public void testStats() {
		ZMMLexer lexer = new ZMMLexer("int x = 1; while(x == 5) [\n x = x + 1; ]");
		ZMMParser parser = new ZMMParser(lexer);
		try {
			parser.stats();
		} catch (MismatchedTokenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
