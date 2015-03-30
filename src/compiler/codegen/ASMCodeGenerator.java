package compiler.codegen;

import java.util.List;

import compiler.lexer.ASMLexer;
import compiler.lexer.Token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
							output += "1";
							break;
						case "MUL":
							output += "2";
							break;
						case "DIV":
							output += "3";
							break;
						case "AND":
							output += "4";
							break;
						case "OR":
							output += "5";
							break;
						case "XNOR":
							output += "6";
							break;
						case "MOV":
							output += "7";
							break;
						case "JZE":
							output += "8";
							break;
						case "JZN":
							output += "9";
							break;
						case "JZG":
							output += "A";
							break;
						case "JZL":
							output += "B";
							break;
						case "LOAD":
							output += "C";
							break;
						case "STOR":
							output += "E";
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
					// Do the same thing for a register token.
					// Here all you need is the number at the
					// end. EX: R0 -> 0.
				{
					switch(t.text)
					{
					case "R0":
						output+="0";
						break;
					case "R1":
						output+="1";
						break;
					case "R2":
						output+="2";
						break;
					case "R3":
						output+="3";
						break;
					case "R4":
						output+="4";
						break;
					case "R5":
						output+="5";
						break;
					case "R6":
						output+="6";
						break;
					case "R7":
						output+="7";
						break;
					case "R8":
						output+="8";
						break;
					case "R9":
						output+="9";
						break;
					case "RA":
						output+="A";
						break;
					case "RB":
						output+="B";
						break;
					case "RC":
						output+="C";
						break;
					case "RD":
						output+="D";
						break;
					case "RE":
						output+="E";
						break;
					case "RF":
						output+="F";
						break;
					}
				}
					
					break;
				case ASMLexer.IMMD:
					// Basically just append the text of this
					// to the output. We can check this later.
					Pattern p = Pattern.compile("[0-f]{1,2}");
					Matcher m = p.matcher(t.text);
					if(m.matches())
					{
						output+=t.text;
					}
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
