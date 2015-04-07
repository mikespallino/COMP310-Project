package compiler.codegen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.lexer.Token;
import compiler.lexer.ZMMLexer;

public class ZMMCodeGenerator implements CodeGenerator {

	private String output;
	private int[] registers;
	private int regPointer; // pointer to keep track of register decls.
	private int memPointer; // possible use for loops
	private Map<String, Integer> fields;
	ZMMLexer lexer;
	
	public ZMMCodeGenerator(ZMMLexer lexer) {
		setOutput("");
		registers = new int[16];
		registers[0] = 0;
		registers[1] = 1;
		registers[2] = 1;
		regPointer = 2;
		memPointer = 0;
		fields = new HashMap<String, Integer>();
		this.lexer = lexer;
	}
	
	public void generateArithmetic(char op, Token val1, Token val2, Token val3) throws InvalidTokenException {
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
		}
		if(fields.containsKey(val3.text)) {
			r3 = fields.get(val3.text);
		} else {
			r3 = Integer.parseInt(val3.text);
		}
		switch(op) {
			case '+':
				output += "ADD R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
			case '-':
				output += "SUB R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
			case '*':
				output += "MUL R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
			case '/':
				output += "DIV R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
			case '&':
				output += "AND R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
			case '^':
				output += "OR R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
			case '#':
				output += "XNOR R" + r1 + ", R" + r2 + ", R" + r3 + "\n";
				break;
		}
	}
	
	public void generateLoop(char conditionType, int numOfStats) {
		switch(conditionType) {
		case 'E':
			setOutput(getOutput() + "JZE R0, " + memPointer + numOfStats + "\n");
			break;
		case 'G':
			setOutput(getOutput() + "JZG R0, " + memPointer + numOfStats + "\n");
			break;
		case 'L':
			setOutput(getOutput() + "JZL R0, " + memPointer + numOfStats + "\n");
			break;
		}
	}
	
	public void generateDecl(Token name, Token value) {
		if(fields.containsKey(value)) {
			int reg = fields.get(value);
			fields.put(name.text, reg);
			output += "MOV R"+ reg + ", " + registers[reg] + "\n";
		} else {
			int val;
			try {
				val = Integer.parseInt(value.text);
				fields.put(name.text, regPointer);
				registers[regPointer] = val;
				output += "MOV R" + regPointer + ", " + val + "\n";
			} catch(NumberFormatException e) {
				throw new Error("Variable not declared! variable: " + value.text);
			}
		}
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

	
	
}
