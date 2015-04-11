package compiler.main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import compiler.codegen.ASMCodeGenerator;
import compiler.lexer.ASMLexer;
import compiler.lexer.ZMMLexer;
import compiler.parser.ASMBacktrackParser;
import compiler.parser.MismatchedTokenException;
import compiler.parser.ZMMParser;
import compiler.preprocessor.Preprocessor;

public class ZMMCompiler {

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Specify Input file, output file, and bundle size.");
			System.exit(-1);
		} else {
			String file = args[0];
			String outputFile = args[1];
			String bundle = args[2];

			
			Scanner scan;
			String input = "";
			try {
				scan = new Scanner(new File(file));
				while (scan.hasNextLine()) {
					input += scan.nextLine();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			ZMMLexer lexer = new ZMMLexer(input);
			ZMMParser parser = new ZMMParser(lexer);
			try {
				ASMCodeGenerator.BUNDLE_SIZE= Integer.parseInt(bundle);
				parser.stats();
				String asm = parser.getCodeGenerator().getOutput();
				System.out.println(asm + "\n");
				Preprocessor pre = new Preprocessor(parser.getLookahead(),
						lexer);
				parser.setLookahead(pre.process());

				ASMBacktrackParser asmParser = new ASMBacktrackParser(
						new ASMLexer(asm));
				String output = asmParser.stats();
				try {
					PrintWriter writer = new PrintWriter("res/outputFile", "UTF-8");
					writer.println(output);
				} catch (FileNotFoundException e) {
					System.out.println("File could not be written, File not found exception.");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					System.out.println("Unsupported Encoding Exception.");
					e.printStackTrace();
				}
				System.out.println(output);
			} catch (MismatchedTokenException e) {
				e.printStackTrace();
			}

		}
	}
}

