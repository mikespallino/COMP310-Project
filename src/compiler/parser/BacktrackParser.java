package compiler.parser;

import compiler.lexer.Lexer;
import compiler.lexer.ListLexer;

public class BacktrackParser extends Parser {

	public BacktrackParser(Lexer input) {
		super(input);
	}
	
//	public void parse() throws Exception {
//		while(lookToken(1).type != ListLexer.EOF_TYPE) {
//			instruction();
//		}
//	}
//	
//	public void instruction() throws Exception {
//		if(isArithmeticInstruction() && lookAhead(2) == ListLexer.REGISTER && lookAhead(3) == ListLexer.COMMA && lookAhead(4) == ListLexer.REGISTER && lookAhead(5) == ListLexer.COMMA && lookAhead(6) == ListLexer.REGISTER) {
//			match(lookAhead(1));
//			match(ListLexer.REGISTER);
//			match(ListLexer.COMMA);
//			match(ListLexer.REGISTER);
//			match(ListLexer.COMMA);
//			match(ListLexer.REGISTER);
//		} else {
//			throw new Exception("No suitable instruction match. Found: " + lookAhead(1));
//		}
//		if (isBusControlInstruction() && lookAhead(2) == ListLexer.REGISTER && lookAhead(3) == ListLexer.COMMA && lookAhead(4) == ListLexer.IMMD) {
//			match(lookAhead(1));
//			match(ListLexer.REGISTER);
//			match(ListLexer.COMMA);
//			match(ListLexer.IMMD);
//		} else if(isBusControlInstruction() && lookAhead(2) == ListLexer.REGISTER) {
//			match(lookAhead(1));
//			match(ListLexer.REGISTER);
//		} else {
//			throw new Exception("No suitable instruction match. Found: " + lookAhead(1));
//		}
//		
//		if(lookAhead(1) == ListLexer.NWLN) {
//			match(ListLexer.NWLN);
//		} else {
//			throw new Exception("No suitable instruction match. Found: " + lookAhead(1));
//		}
//	}
//	
//	private boolean isArithmeticInstruction() {
//		switch(lookAhead(1)) {
//			case ListLexer.ADD:
//			case ListLexer.SUB:
//			case ListLexer.MUL:
//			case ListLexer.DIV:
//			case ListLexer.AND:
//			case ListLexer.OR:
//			case ListLexer.XNOR:
//				return true;
//			default:
//				return false;
//		}
//	}
//	
//	private boolean isBusControlInstruction() {
//		switch(lookAhead(1)) {
//			case ListLexer.MOV:
//			case ListLexer.JZE:
//			case ListLexer.JZN:
//			case ListLexer.JZG:
//			case ListLexer.JZL:
//			case ListLexer.LOAD:
//			case ListLexer.STOR:
//				return true;
//			default:
//				return false;
//		}
//	}
	
	public void stats() throws MismatchedTokenException {
		while(lookAhead(1) != ListLexer.EOF_TYPE) {
			stat();
		}
		match(ListLexer.EOF_TYPE);
	}
	
	public void stat() throws MismatchedTokenException {
		if(speculateBusControlInstr()) {
			busControlInstr();
		} else if(speculateArithmaticInstr()) {
			arithmaticInstr();
		} else {
			throw new MismatchedTokenException("Expecting Bus Control or Arithmatic Instruction; Found " + lookToken(1));
		}
	}
	
	private boolean speculateArithmaticInstr() {
		boolean success = true;
		mark();
		try {
			arithmaticInstr();
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}

	private boolean speculateBusControlInstr() {
		boolean success = true;
		mark();
		try {
			busControlInstr();
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}

	public void busControlInstr() throws MismatchedTokenException {
		if(speculateBusControlInstrThreeArg()) {
			instr();
			reg();
			mmm();
		} else if(speculateBusControlInstrTwoArg()) {
			instr();
			reg();
		} else {
			throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	private boolean speculateBusControlInstrThreeArg() {
		boolean success = true;
		mark();
		try {
			instr();
			reg();
			mmm();
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	private boolean speculateBusControlInstrTwoArg() {
		boolean success = true;
		mark();
		try {
			instr();
			reg();
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	public void arithmaticInstr() throws MismatchedTokenException {
		instr();
		reg();
		reg();
		reg();
	}
	
	public void mmm() throws MismatchedTokenException {
		if(lookAhead(1) == ListLexer.IMMD && lookAhead(2) == ListLexer.NWLN) {
			match(ListLexer.IMMD);
			match(ListLexer.NWLN);
		} else {
			throw new MismatchedTokenException("Expecting immediate value; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	public void reg() throws MismatchedTokenException {
		if(lookAhead(1) == ListLexer.REGISTER && lookAhead(2) == ListLexer.COMMA) {
			match(ListLexer.REGISTER);
			match(ListLexer.COMMA);
		} else if(lookAhead(1) == ListLexer.REGISTER && lookAhead(2) == ListLexer.NWLN) {
			match(ListLexer.REGISTER);
			match(ListLexer.NWLN);
		} else {
			throw new MismatchedTokenException("Expecting Register definition; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	public void instr() throws MismatchedTokenException {
		if(lookAhead(1) == ListLexer.INSTRUCTION) {
			match(ListLexer.INSTRUCTION);
		} else {
			throw new MismatchedTokenException("Expecting an instruction; found " + lookToken(1));
		}
	}

}
