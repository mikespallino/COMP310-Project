package compiler.parser;

import java.util.List;

import compiler.lexer.Lexer;
import compiler.lexer.ASMLexer;
import compiler.lexer.Token;

/**
 * ASMBacktrackParser is an implementation of a backtracking parser 
 * that takes the input from the lexer and parses the tokens.
 * It speculates about different paths in the parse tree to take
 * and goes the way that doesn't throw an exception.
 * @author Mike
 *
 */
public class ASMBacktrackParser extends Parser {

	public ASMBacktrackParser(Lexer input) {
		super(input);
	}
	
	/**
	 * stats() is the entry point to parsing an arbitrary number of statements until the end of file
	 * @return a list of all the parsed tokens.
	 * @throws MismatchedTokenException
	 */
	public List<Token> stats() throws MismatchedTokenException {
		while(lookAhead(1) != ASMLexer.EOF_TYPE) {
			stat();
		}
		match(ASMLexer.EOF_TYPE);
		return parsedTokens;
	}
	
	/**
	 * stat() is the generic statement rule in the grammar.
	 * It speculates about being wither a bus control instruction 
	 * or an arithmetic instruction.
	 * @throws MismatchedTokenException
	 */
	public void stat() throws MismatchedTokenException {
		if(speculateBusControlInstr()) {
			busControlInstr();
		} else if(speculateArithmeticInstr()) {
			arithmeticInstr();
		} else {
			throw new MismatchedTokenException("Expecting Bus Control or arithmetic Instruction; Found " + lookToken(1));
		}
	}
	
	/**
	 * speculateArithmeticInstr() will try to match tokens specific
	 * to and arithmetic instruction. If this fails, the pattern is not
	 * and arithmetic statement.
	 * @return if the token pattern is an arithmetic instruction
	 */
	private boolean speculateArithmeticInstr() {
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

	/**
	 * speculateBusControlInstr() will try to match tokens specific
	 * to and bus control instruction. If this fails, the pattern is not
	 * and bus control instruction.
	 * @return if the token pattern is an bus control instruction
	 */
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

	/**
	 * busControlInstr() will speculate about the different types of busControlInstructions
	 * in the grammar. If it is either of the types it will parse accordingly.
	 */
	public void busControlInstr() throws MismatchedTokenException {
		if(speculateBusControlInstrThreeArg()) {
			instr();
			reg();
			mmm();
			if(lookAhead(1) == ASMLexer.NWLN) {
				match(ASMLexer.NWLN);
			} else if(lookAhead(1) == ASMLexer.EOF_TYPE) {
				match(ASMLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3));
			}
		} else if(speculateBusControlInstrTwoArg()) {
			instr();
			reg();
			if(lookAhead(1) == ASMLexer.NWLN) {
				match(ASMLexer.NWLN);
			} else if(lookAhead(1) == ASMLexer.EOF_TYPE) {
				match(ASMLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3));
			}
		} else {
			throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	/**
	 * speculateBusControlInstrThreeArg() speculates whether or not the token
	 * stream matches a bus control instruction with three arguments.
	 * @return if this is a three argument bus control instruction
	 */
	private boolean speculateBusControlInstrThreeArg() {
		boolean success = true;
		mark();
		try {
			instr();
			reg();
			mmm();
			if(lookAhead(1) == ASMLexer.NWLN) {
				match(ASMLexer.NWLN);
			} else if(lookAhead(1) == ASMLexer.EOF_TYPE) {
				match(ASMLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3) + " " + lookAhead(4));
			}
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	/**
	 * speculateBusControlInstrTwoArg() speculates whether or not the token
	 * stream matches a bus control instruction with two arguments.
	 * @return if this is a two argument bus control instruction
	 */
	private boolean speculateBusControlInstrTwoArg() {
		boolean success = true;
		mark();
		try {
			instr();
			reg();
			if(lookAhead(1) == ASMLexer.NWLN) {
				match(ASMLexer.NWLN);
			} else if(lookAhead(1) == ASMLexer.EOF_TYPE) {
				match(ASMLexer.EOF_TYPE);
			} else {
				throw new MismatchedTokenException("Expecting Bus Control Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3));
			}
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	/**
	 * arithmeticInstr() will match the tokens required for an
	 * arithmetic instruction.
	 * @throws MismatchedTokenException
	 */
	public void arithmeticInstr() throws MismatchedTokenException {
		instr();
		reg();
		reg();
		reg();
		if(lookAhead(1) == ASMLexer.NWLN) {
			match(ASMLexer.NWLN);
		} else if(lookAhead(1) == ASMLexer.EOF_TYPE) {
			match(ASMLexer.EOF_TYPE);
		} else {
			throw new MismatchedTokenException("Expecting arithmetic Instruction; Found " + lookToken(1) + " " + lookToken(2) + " " + lookToken(3) + " " + lookAhead(4));
		}
	}
	
	/**
	 * mmm() will match the tokens required for an immediate
	 * value token.
	 * @throws MismatchedTokenException
	 */
	public void mmm() throws MismatchedTokenException {
		if(lookAhead(1) == ASMLexer.IMMD) {
			match(ASMLexer.IMMD);
		} else {
			throw new MismatchedTokenException("Expecting immediate value; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	/**
	 * reg() will speculate about the two ways to declare a register 
	 * according to the grammar.
	 * @throws MismatchedTokenException
	 */
	public void reg() throws MismatchedTokenException {
		if(speculateRegisterComma()) {
			match(ASMLexer.REGISTER);
			match(ASMLexer.COMMA);
		} else if(speculateRegister()) {
			match(ASMLexer.REGISTER);
		} else {
			throw new MismatchedTokenException("Expecting Register definition; Found " + lookToken(1) + " " + lookToken(2));
		}
	}
	
	/**
	 * speculateRegisterComma() will try to match both a register
	 * token and a comma token.
	 * @return if this token stream is a register and comma token
	 */
	public boolean speculateRegisterComma() {
		boolean success = true;
		mark();
		try {
			match(ASMLexer.REGISTER);
			match(ASMLexer.COMMA);
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	/**
	 * speculateRegister() will try to match a register token.
	 * @return if this token stream is a register
	 */
	public boolean speculateRegister() {
		boolean success = true;
		mark();
		try {
			match(ASMLexer.REGISTER);
		} catch (MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}
	
	/**
	 * instr() will match an instruction token.
	 * @throws MismatchedTokenException
	 */
	public void instr() throws MismatchedTokenException {
		if(lookAhead(1) == ASMLexer.INSTRUCTION) {
			match(ASMLexer.INSTRUCTION);
		} else {
			throw new MismatchedTokenException("Expecting an instruction; found " + lookToken(1));
		}
	}

}
