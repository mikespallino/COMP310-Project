package compiler.codegen;

import java.util.List;

import compiler.lexer.ASMLexer;
import compiler.lexer.Token;

/**
 * ASMCodeGenerator will handle the conversion of 
 * Assembly instructions to their proper hexadecimal
 * equivalents in machine code.
 * @author Mike
 * @author Matt
 */
public class ASMCodeGenerator implements CodeGenerator {

	/**
	 * generate(List<Token> tokens) will handle the
	 * conversion of tokens to machine code. It will
	 * go through each token and convert it to it's
	 * proper hex value.
	 * @param List<Token> tokens - the tokens that have 
	 * 							   been parsed.
	 * @return output - A string of the machine code.
	 * @throws InvalidTokenException 
	 */
	@Override
	public String generate(List<Token> tokens) throws InvalidTokenException {
		String output = "";
		for(Token t : tokens) {
			// All tokens have a type. Switch on the type and 
			// then perform the appropriate action from there.
			switch(t.type) {
				// The first type would be instruction. If this
				// is an instruction token, we need to switch on
				// the text associated with that token as well
				// to figure out which insruction it is.
				case ASMLexer.INSTRUCTION:
					switch(t.text) {
						case "ADD":
							// Since this instruction token is an ADD instruction
							// The hexadecimal conversion for that is 0. Append 
							// that to the output and we're done!
							output += "0";
							break;
						case "SUB":
							// Implement the rest of these! the hex conversions can
							// be derived from the CPU ISA reference in the README.
							break;
						case "MUL":
							break;
						case "DIV":
							break;
						case "AND":
							break;
						case "OR":
							break;
						case "XNOR":
							break;
						case "MOV":
							break;
						case "JZE":
							break;
						case "JZN":
							break;
						case "JZG":
							break;
						case "JZL":
							break;
						case "LOAD":
							break;
						case "STOR":
							break;
						default:
							// We shouldn't ever get here, but if we do
							// that means that the token that was created
							// isn't a valid instruction token that can 
							// be converted so throw this exception. 
							throw new InvalidTokenException("Can't convert and invalid token into Machine Code!" + t);
					}
					break;
				case ASMLexer.REGISTER:
					// Do the same this for a register token.
					// Here all you need is the number at the
					// end. EX: R0 -> 0.
					break;
				case ASMLexer.IMMD:
					// Basically just append the text of this
					// to the output. We can check this later.
					break;
				default:
					// We shouldn't ever get here, but if we do
					// that means that the token that was created
					// isn't a valid token that can be converted
					// so throw this exception.
					throw new InvalidTokenException("Can't convert and invalid token into Machine Code!" + t);
			}
		}
		return output;
	}

}
