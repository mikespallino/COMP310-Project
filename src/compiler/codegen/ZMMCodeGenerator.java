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
		}
		if(fields.containsKey(val3.text)) {
			r3 = fields.get(val3.text);
		} else {
			r3 = Integer.parseInt(val3.text);
			registers[1] = r3;
		}
		switch(op) {
			case '+':
				if(registers[1] == r2) {
					output += "ADD R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "ADD R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
			case '-':
				if(registers[1] == r2) {
					output += "SUB R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "SUB R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
			case '*':
				if(registers[1] == r2) {
					output += "MUL R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "MUL R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
			case '/':
				if(registers[1] == r2) {
					output += "DIV R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "DIV R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
			case '&':
				if(registers[1] == r2) {
					output += "AND R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "AND R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
			case '^':
				if(registers[1] == r2) {
					output += "OR R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "OR R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
			case '#':
				if(registers[1] == r2) {
					output += "XNOR R" + r1 + ", R1, R" + r3 + "\n";
				} else if(registers[1] == r3) {
					output += "XNOR R" + r1 + ", R" + r2 + ", R1\n";
				}
				break;
		}
		return 4;
	}
	
	public int generateDecl(Token name, Token value) {
		if(fields.containsKey(value)) {
			int reg = fields.get(value);
			fields.put(name.text, reg);
			if(registers[reg] < 0x0A) {
				output += "MOV R"+ reg + ", 0" + registers[reg] + "\n";
			} else {
				output += "MOV R"+ reg + ", " + registers[reg] + "\n";
			}
			return 4;
		} else {
			int val;
			try {
				val = Integer.parseInt(value.text);
				fields.put(name.text, regPointer);
				registers[regPointer] = val;
				output += "MOV R" + regPointer + ", " + val + "\n";
				regPointer++;
				return 4;
			} catch(NumberFormatException e) {
				throw new Error("Variable not declared! variable: " + value.text);
			}
		}
	}
	
	public int generateWhile(Token comp, Token comp2, char conditionType, int stats) {
		switch(conditionType) {
		case 'E':
			if(comp2.type == ZMMLexer.VALUE) {
				output += "MOV R1, " + comp2.text +
						"\nSUB R2, R" + fields.get(comp) + ", R" + comp2.text + 
						"\nJZE R2, " + stats + "\n";
				return 10;
			} else if(comp2.type == ZMMLexer.NAME) {
				output += "\nSUB R2, R" + fields.get(comp) + ", R" + fields.get(comp2) + 
						  "\nJZE R2, " + stats + "\n";
				return 7;
			}
			break;
		case 'G':
			if(comp2.type == ZMMLexer.VALUE) {
				output += "MOV R1, " + comp2.text +
						"\nSUB R2, R" + fields.get(comp) + ", R" + comp2.text + 
						"\nJZG R2, " + stats + "\n";
				return 10;
			} else if(comp2.type == ZMMLexer.NAME) {
				output += "\nSUB R2, R" + fields.get(comp) + ", R" + fields.get(comp2) + 
						  "\nJZG R2, " + stats + "\n";
				return 7;
			}
			break;
		case 'L':
			if(comp2.type == ZMMLexer.VALUE) {
				output += "MOV R1, " + comp2.text +
						"\nSUB R2, R" + fields.get(comp) + ", R" + comp2.text + 
						"\nJZL R2, " + stats + "\n";
				return 10;
			} else if(comp2.type == ZMMLexer.NAME) {
				output += "\nSUB R2, R" + fields.get(comp) + ", R" + fields.get(comp2) + 
						  "\nJZL R2, " + stats + "\n";
				return 7;
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
