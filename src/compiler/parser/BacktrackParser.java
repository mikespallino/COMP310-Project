package compiler.parser;

import compiler.lexer.Lexer;
import compiler.lexer.ListLexer;

public class BacktrackParser extends Parser {

	public BacktrackParser(Lexer input) {
		super(input);
	}
	
	public void stats() throws MismatchedTokenException {
		while(lookAhead(1) != ListLexer.EOF_TYPE) {
			stat();
		}
		match(ListLexer.EOF_TYPE);
	}
	
	public void stat() throws MismatchedTokenException {
		if(speculateBusControlInstr()) {
			busControlInstr();
		} else if(speculatearithmeticInstr()) {
			arithmeticInstr();
		} else {
			throw new MismatchedTokenException("Expecting Bus Control or arithmetic Instruction; Found " + lookToken(1));
		}
	}
	
	private boolean speculatearithmeticInstr() {
		boolean success = true;
		mark();
		try {
			arithmeticInstr();
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
			if(lookAhead(1) == ListLexer.NWLN) {
				match(ListLexer.NWLN);
			} else if(lookAhead(1) == ListLexer.EOF_TYPE) {
				match(ListLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3));
			}
		} else if(speculateBusControlInstrTwoArg()) {
			instr();
			reg();
			if(lookAhead(1) == ListLexer.NWLN) {
				match(ListLexer.NWLN);
			} else if(lookAhead(1) == ListLexer.EOF_TYPE) {
				match(ListLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3));
			}
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
			if(lookAhead(1) == ListLexer.NWLN) {
				match(ListLexer.NWLN);
			} else if(lookAhead(1) == ListLexer.EOF_TYPE) {
				match(ListLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3) + " " + lookAhead(4));
			}
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
			if(lookAhead(1) == ListLexer.NWLN) {
				match(ListLexer.NWLN);
			} else if(lookAhead(1) == ListLexer.EOF_TYPE) {
				match(ListLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3));
			}
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	public void arithmeticInstr() throws MismatchedTokenException {
		instr();
		reg();
		reg();
		reg();
		if(lookAhead(1) == ListLexer.NWLN) {
			match(ListLexer.NWLN);
		} else if(lookAhead(1) == ListLexer.EOF_TYPE) {
			match(ListLexer.EOF_TYPE);
		} else {
			throw new MismatchedTokenException("Expecting arithmetic Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3) + " " + lookAhead(4));
		}
	}
	
	public void mmm() throws MismatchedTokenException {
		if(lookAhead(1) == ListLexer.IMMD) {
			match(ListLexer.IMMD);
		} else {
			throw new MismatchedTokenException("Expecting immediate value; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	public void reg() throws MismatchedTokenException {
		if(speculateRegisterComma()) {
			match(ListLexer.REGISTER);
			match(ListLexer.COMMA);
		} else if(speculateRegister()) {
			match(ListLexer.REGISTER);
		} else {
			throw new MismatchedTokenException("Expecting Register definition; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	public boolean speculateRegisterComma() {
		boolean success = true;
		mark();
		try {
			match(ListLexer.REGISTER);
			match(ListLexer.COMMA);
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	public boolean speculateRegister() {
		boolean success = true;
		mark();
		try {
			match(ListLexer.REGISTER);
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	public void instr() throws MismatchedTokenException {
		if(lookAhead(1) == ListLexer.INSTRUCTION) {
			match(ListLexer.INSTRUCTION);
		} else {
			throw new MismatchedTokenException("Expecting an instruction; found " + lookToken(1));
		}
	}

}
