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

	public static int BUNDLE_SIZE = 4;
	private int memorylocation = 0;
	private String output = "v2.0 raw\n";
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
							memorylocation++;
							break;
						case "SUB":
							// Implement the rest of these! the hex conversions can
							// be derived from the CPU ISA reference in the README.
							output += "1";
							memorylocation++;
							break;
						case "MUL":
							output += "2";
							memorylocation++;
							break;
						case "DIV":
							output += "3";
							memorylocation++;
							break;
						case "AND":
							output += "4";
							memorylocation++;
							break;
						case "OR":
							output += "5";
							memorylocation++;
							break;
						case "XNOR":
							output += "6";
							memorylocation++;
							break;
						case "MOV":
							output += "7";
							memorylocation++;
							break;
						case "JZE":
							output += "8";
							memorylocation++;
							break;
						case "JZN":
							output += "9";
							memorylocation++;
							break;
						case "JZG":
							output += "A";
							memorylocation++;
							break;
						case "JZL":
							output += "B";
							memorylocation++;
							break;
						case "LOAD":
							output += "C";
							memorylocation++;
							break;
						case "STOR":
							output += "E";
							memorylocation++;
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
						memorylocation++;
						break;
					case "R1":
						output+="1";
						memorylocation++;
						break;
					case "R2":
						output+="2";
						memorylocation++;
						break;
					case "R3":
						output+="3";
						memorylocation++;
						break;
					case "R4":
						output+="4";
						memorylocation++;
						break;
					case "R5":
						output+="5";
						memorylocation++;
						break;
					case "R6":
						output+="6";
						memorylocation++;
						break;
					case "R7":
						output+="7";
						memorylocation++;
						break;
					case "R8":
						output+="8";
						memorylocation++;
						break;
					case "R9":
						output+="9";
						memorylocation++;
						break;
					case "RA":
						output+="A";
						memorylocation++;
						break;
					case "RB":
						output+="B";
						memorylocation++;
						break;
					case "RC":
						output+="C";
						memorylocation++;
						break;
					case "RD":
						output+="D";
						memorylocation++;
						break;
					case "RE":
						output+="E";
						memorylocation++;
						break;
					case "RF":
						output+="F";
						memorylocation++;
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
						if(t.text.length() < 2) {
							output += "0" + t.text;
						} else {
							output+=t.text;
						}
						memorylocation += 2;
					}
					break;
				case ASMLexer.COMMA:
				case ASMLexer.EOF_TYPE:
				case ASMLexer.NWLN:
					break;
				default:
					// We shouldn't ever get here, but if we do
					// that means that the token that was created
					// isn't a valid token that can be converted
					// so throw this exception.
					throw new InvalidTokenException("Can't convert and invalid token into Machine Code!" + t);
			}
			if(memorylocation % BUNDLE_SIZE == 0 && memorylocation % 16 != 0) {
				output += " ";
			}
			if(memorylocation != 0 && memorylocation % 16 == 0) {
				output += "\n";
				memorylocation = 0;
			}
		}
		return output;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

}
