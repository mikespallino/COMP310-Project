package compiler.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.lexer.Token;
import compiler.lexer.ZMMLexer;

public class ZMMCodeGenerator implements CodeGenerator {

	private String output;
	private int[] registers;
	private int regPointer; // pointer to keep track of register decls.
	private List<Integer> outputPointer;
	private Map<String, Integer> fields;
	ZMMLexer lexer;
	
	public ZMMCodeGenerator(ZMMLexer lexer) {
		setOutput("");
		registers = new int[16];
		registers[0] = 0; // Zero by CPU
		registers[1] = 1; // Save for value storage
		registers[2] = 1; // Save for comparison subtraction storage
		registers[3] = 1;
		regPointer = 3;
		outputPointer = new ArrayList<Integer>();
		fields = new HashMap<String, Integer>();
		this.lexer = lexer;
	}
	
	public int generateArithmetic(char op, Token val1, Token val2, Token val3) throws InvalidTokenException {
		int r1 = -1, r2 = -1, r3 = -1;
		int returnAmt = 4;
		if(fields.containsKey(val1.text)) {
			r1 = fields.get(val1.text);
		} else {
			r1 = Integer.parseInt(val1.text);
		}
		if(fields.containsKey(val2.text)) {
			r2 = fields.get(val2.text);
		} else {
			r2 = Integer.parseInt(val2.text);
			registers[1] = r2;
			if(r2 <= 0x0F) {
				output += "MOV R1, 0" + Integer.toHexString(r2) + "\n";
			} else {
				output += "MOV R1, " + Integer.toHexString(r2) + "\n";
			}
			returnAmt += 4;
		}
		if(fields.containsKey(val3.text)) {
			r3 = fields.get(val3.text);
		} else {
			r3 = Integer.parseInt(val3.text);
			registers[1] = r3;
			if(r2 <= 0x0F) {
				output += "MOV R1, 0" + Integer.toHexString(r3) + "\n";
			} else {
				output += "MOV R1, " + Integer.toHexString(r3) + "\n";
			}
			returnAmt += 4;
		}
		switch(op) {
			case '+':
				if(registers[1] == r2) {
					output += "ADD R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "ADD R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "ADD R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
			case '-':
				if(registers[1] == r2) {
					output += "SUB R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "SUB R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "SUB R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
			case '*':
				if(registers[1] == r2) {
					output += "MUL R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "MUL R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "MUL R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
			case '/':
				if(registers[1] == r2) {
					output += "DIV R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "DIV R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "DIV R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
			case '&':
				if(registers[1] == r2) {
					output += "AND R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "AND R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "AND R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
			case '^':
				if(registers[1] == r2) {
					output += "OR R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "OR R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "OR R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
			case '#':
				if(registers[1] == r2) {
					output += "XNOR R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "XNOR R" + r1 + ", R" + r2 + ", R1\n";
				} else {
					output += "XNOR R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				}
				break;
		}
		return returnAmt;
	}
	
	public int generateDecl(Token name, Token value) {
		if(fields.containsKey(name.text)) {
			int originalRegister = fields.get(name.text);
			if(fields.containsKey(value.text)) {
				int reg = fields.get(value.text);
				fields.put(name.text, reg);
				if(registers[reg] <= 0x0F) {
					output += "MOV R"+ originalRegister + ", 0" + Integer.toHexString(new Integer(registers[reg])) + "\n";
				} else {
					output += "MOV R"+ originalRegister + ", " + Integer.toHexString(new Integer(registers[reg])) + "\n";
				}
				return 4;
			} else {
				int val = Integer.parseInt(value.text);
				if(val <= 0x0F) {
					output += "MOV R"+ originalRegister + ", 0" + Integer.toHexString(val) + "\n";
				} else {
					output += "MOV R"+ originalRegister + ", " + Integer.toHexString(val) + "\n";
				}
				return 4;
			}
		}
		if(fields.containsKey(value.text)) {
			int reg = fields.get(value.text);
			fields.put(name.text, reg);
			if(registers[reg] <= 0x0F) {
				output += "MOV R"+ reg + ", 0" + Integer.toHexString(new Integer(registers[reg])) + "\n";
			} else {
				output += "MOV R"+ reg + ", " + Integer.toHexString(new Integer(registers[reg])) + "\n";
			}
			return 4;
		} else {
			if(regPointer == registers.length) {
				throw new Error("No more register space!");
			} else {
				int val;
				try {
					val = Integer.parseInt(value.text);
					fields.put(name.text, regPointer);
					registers[regPointer] = val;
					if(registers[regPointer] <= 0x0F) {
						output += "MOV R"+ regPointer + ", 0" + Integer.toHexString(new Integer(val)) + "\n";
					} else {
						output += "MOV R"+ regPointer + ", " + Integer.toHexString(new Integer(val)) + "\n";
					}
					regPointer++;
					return 4;
				} catch(NumberFormatException e) {
					throw new Error("Variable not declared! variable: " + value.text);
				}
			}
		}
	}
	
	public int generateWhile(Context c, int stats, int inlineStats) {
		String before = output.substring(0, outputPointer.get(outputPointer.size() - 1));
		String after = output.substring(outputPointer.get(outputPointer.size() - 1));
		String insert = "";
		String unconditionalJumpBack = "JZE R0, ";
		if(inlineStats <= 0x0F) {
			unconditionalJumpBack += "0" + Integer.toHexString(new Integer(inlineStats / 4)) + "\n";
		} else {
			unconditionalJumpBack += Integer.toHexString(new Integer(inlineStats / 4)) + "\n";
		}
		String tokenOneRegister = Integer.toHexString(new Integer(fields.get(c.getTokOne().text)));
		String tokenTwoRegister;
		if(fields.get(c.getTokTwo().text) != null && fields.get(c.getTokTwo().text) <= 0x0F) {
			tokenTwoRegister = "0" + Integer.toHexString(new Integer(fields.get(c.getTokTwo().text)));
		} else if(fields.get(c.getTokTwo().text) != null) {
			tokenTwoRegister = Integer.toHexString(new Integer(fields.get(c.getTokTwo().text)));
		} else {
			tokenTwoRegister = null; //Should never happen
		}
		switch(c.getType()) {
		case 'E':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					insert = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					insert = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				insert += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZE R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nJZE R2, " + tmp;
				output = before + insert + after + unconditionalJumpBack;
				return 20;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				insert = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZE R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  		"\nJZE R2, ";
				output = before + insert + after + unconditionalJumpBack;
				return 12;
			}
			break;
		case 'N':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					insert = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					insert = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				insert += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZN R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nJZE R2, " + tmp;
				output = before + insert + after + unconditionalJumpBack;
				return 20;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				insert = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZN R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  		"\nJZE R2, ";
				output = before + insert + after + unconditionalJumpBack;
				return 12;
			}
			break;
		case 'G':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					insert = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					insert = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				insert += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZG R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nJZE R2, " + tmp;
				output = before + insert + after + unconditionalJumpBack;
				return 20;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				insert = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZG R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nJZE R2, " + tmp;
				output = before + insert + after + unconditionalJumpBack;
				return 12;
			}
			break;
		case 'L':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					insert = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					insert = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				insert += "\nSUB R2, R" + tokenOneRegister + ", R1" +
						  "\nJZL R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nJZE R2, " + tmp;
				output = before + insert + after + unconditionalJumpBack;
				return 20;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				insert = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZL R2, ";
				String tmp = "";
				if((stats + inlineStats  + inlineStats + 12)/4 <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + inlineStats  + inlineStats + 12)/4));
				}
				insert += tmp + "\nJZE R2, " + tmp;
				output = before + insert + after + unconditionalJumpBack;
				return 12;
			}
			break;
		}
		return 0;
	}
	
	public int generateIf(Context c, int stats, int ifStatCount, int stats2, int elseStatCount) {
		String start = output.substring(0, outputPointer.get(outputPointer.size() - 2));
		String middle = output.substring(outputPointer.get(outputPointer.size() - 2), outputPointer.get(outputPointer.size() - 1));
		String end = output.substring(outputPointer.get(outputPointer.size() - 1));
		
		String tokenOneRegister = Integer.toHexString(new Integer(fields.get(c.getTokOne().text)));
		String tokenTwoRegister;
		if(fields.get(c.getTokTwo().text) != null && fields.get(c.getTokTwo().text) <= 0x0F) {
			tokenTwoRegister = "0" + Integer.toHexString(new Integer(fields.get(c.getTokTwo().text)));
		} else if(fields.get(c.getTokTwo().text) != null) {
			tokenTwoRegister = Integer.toHexString(new Integer(fields.get(c.getTokTwo().text)));
		} else {
			tokenTwoRegister = null; //Should never happen
		}
		
		String ifStatStart = "";
		String unconditionalJumpOver = "JZE R0, ";
		if((stats + ifStatCount) / 4 <= 0x0F) {
			unconditionalJumpOver +=  "0" + Integer.toHexString((stats + ifStatCount) / 4);
		} else {
			unconditionalJumpOver += Integer.toHexString((stats + ifStatCount) / 4);
		}
		
		switch(c.getType()) {
		case 'E':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					ifStatStart = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					ifStatStart = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				ifStatStart += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZE R2, ";
				if((stats + ifStatCount + stats2 + 4) /4 <= 0x0F) {
					ifStatStart += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				} else {
					ifStatStart += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				}
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 16;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				ifStatStart = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZE R2, ";
				if((stats + ifStatCount + stats2 + 4) /4  <= 0x0F) {
					ifStatStart += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				} else {
					ifStatStart += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				}
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 8;
			}
			break;
		case 'N':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					ifStatStart = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					ifStatStart = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				ifStatStart += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZN R2, ";
				if((stats + ifStatCount + stats2 + 4) /4  <= 0x0F) {
					ifStatStart += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				} else {
					ifStatStart += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				}
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 16;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				ifStatStart = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZN R2, ";
				if((stats + ifStatCount + stats2 + 4) /4  <= 0x0F) {
					ifStatStart += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				} else {
					ifStatStart += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 4) /4));
				}
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 8;
			}
			break;
		case 'L':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					ifStatStart = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					ifStatStart = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				ifStatStart += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZL R2, ";
				String tmp = "";
				if((stats + ifStatCount + stats2 + 8) /4  <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				}
				ifStatStart += tmp + "\nJZE R2, " + tmp;
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 13;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				ifStatStart = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZL R2, ";
				String tmp = "";
				if((stats + ifStatCount + stats2 + 8) /4  <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				}
				ifStatStart += tmp + "\nJZE R2, " + tmp;
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 10;
			}
			break;
		case 'G':
			if(c.getTokTwo().type == ZMMLexer.VALUE) {
				if(new Integer(c.getTokTwo().text) <= 0x0F) {
					ifStatStart = "\nMOV R1, 0" + Integer.toHexString(new Integer(c.getTokTwo().text));
				} else {
					ifStatStart = "\nMOV R1, " + Integer.toHexString(new Integer(c.getTokTwo().text));
				}
				ifStatStart += "\nSUB R2, R" + tokenOneRegister + ", R1" + 
						  "\nJZG R2, ";
				String tmp = "";
				if((stats + ifStatCount + stats2 + 8) /4  <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				}
				ifStatStart += tmp + "\nJZE R2, " + tmp;
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 13;
			} else if(c.getTokTwo().type == ZMMLexer.NAME) {
				ifStatStart = "\nSUB R2, R" + tokenOneRegister + ", R" + tokenTwoRegister + 
						  "\nJZG R2, ";
				String tmp = "";
				if((stats + ifStatCount + stats2 + 8) /4  <= 0x0F) {
					tmp += "0" + Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				} else {
					tmp += Integer.toHexString(new Integer((stats + ifStatCount + stats2 + 8) /4));
				}
				ifStatStart += tmp + "\nJZE R2, " + tmp;
				output = start + ifStatStart + middle + "\n" + unconditionalJumpOver + end;
				return 10;
			}
			break;
		}
		return 0;
	}

	@Override
	public String generate(List<Token> tokens) throws InvalidTokenException {
		return null;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void mark() {
		outputPointer.add(output.length() - 1);
	}
	
	public void release() {
		outputPointer.remove(outputPointer.size() - 1);
	}
	
}