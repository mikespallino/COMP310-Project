package compiler.parser;

import java.util.List;

import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.ZMMLexer;

/**
 * ZMM Parser is an implementation of a backtracking parser.
 * that takes the input from the lexer and parses the tokens.
 * It speculates about different paths in the parse tree to take
 * and goes the way that doesn't throw an exception.
 * @author Mike
 * @author Zach
 */
public class ZMMParser extends Parser {

	public ZMMParser(Lexer input) {
		super(input);
	}
	
	/**
	 * stats() is the entry point into the grammar.
	 * It matches statements until the end of file.
	 * @return a list of all the parsed tokens
	 * @throws MismatchedTokenException
	 * @author Mike
	 */
	public List<Token> stats() throws MismatchedTokenException {
		while(lookAhead(1) != ZMMLexer.EOF_TYPE) {
			stat();
		}
		match(ZMMLexer.EOF_TYPE);
		return parsedTokens;
	}
	
	/**
	 * stat() is the generic statement in the grammar.
	 * It matches assignment, comparision, while, for,
	 * and if statements.
	 * @throws MismatchedTokenException
	 * @author Mike
	 */
	public void stat() throws MismatchedTokenException {
		if(speculateAssign()) {
			assign();
		} else if(speculateComp()) {
			comp();
		} else if(speculateWhileS()) {
			whileS();
		} else if(speculateForS()) {
			forS();
		} else if(speculateIfS()) {
			ifS();
		} else {
			throw new MismatchedTokenException("Expecting a statment; Found " + lookToken(1));
		}
	}
	
	private boolean speculateElseS() {
		// TODO: Zach implement this!
		return false;
	}

	private boolean speculateIfS() {
		// TODO: Zach implement this!
		return false;
	}

	private boolean speculateForS() {
		// TODO: Zach implement this!
		return false;
	}

	/**
	 * Speculate if this is a while statement
	 * @return if the token stream represents a while statement.
	 * @author Mike
	 */
	private boolean speculateWhileS() {
		boolean success = true;
		mark();
		try {
			whileS();
		} catch(MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
	}

	private boolean speculateComp() {
		// TODO: Zach implement this!
		return false;
	}

	private boolean speculateAssign() {
		// TODO: Zach implement this!
		return false;
	}

	/**
	 * assign() is the assignment section of the grammar. It
	 * handles statements of assignment with and without an
	 * operator.
	 * @throws MismatchedTokenException 
	 * @author Mike
	 */
	public void assign() throws MismatchedTokenException {
		if(speculateRegularAssign()) {
			match(ZMMLexer.INT);
			match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} else if(speculateOperatorAssign()) {
			match(ZMMLexer.INT);
			match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.OP);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} else {
			throw new MismatchedTokenException("Expecting an assignment; Found " + lookToken(1));
		}
	}
	
	/**
	 * Speculates an operator assignment statement. (int x = y + 5;)
	 * @return if this token stream is an operator assignment statement.
	 * @author Mike
	 */
	private boolean speculateOperatorAssign() {
		boolean success = true;
		mark();
		try {
			match(ZMMLexer.INT);
			match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		return success;
	}

	/**
	 * Speculates an assignment statement. (int x = 5;)
	 * @return if this token stream is an assignment statement.
	 * @author Mike
	 */
	private boolean speculateRegularAssign() {
		boolean success = true;
		mark();
		try {
			match(ZMMLexer.INT);
			match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.OP);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		return success;
	}

	/**
	 * comp() is the comparison section of the grammar. It 
	 * handles statements of equality and value comparisons
	 * for less than or greater than.
	 * @throws MismatchedTokenException 
	 * @author Mike
	 */
	public void comp() throws MismatchedTokenException {
		if(speculateEquality()) {
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} else if (speculateValDif()) {
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.LESS, ZMMLexer.GREATER);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} else if(speculateValDifEq()) {
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.LESS, ZMMLexer.GREATER);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} else {
			throw new MismatchedTokenException("Expecting a comparison; Found " + lookToken(1));
		}
	}
	
	/**
	 * Speculates an equality statement. (==)
	 * @return if this token stream is an equality comparison statement.
	 * @author Mike
	 */
	private boolean speculateEquality() {
		boolean success = true;
		mark();
		try {
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		return success;
	}
	
	/**
	 * Speculates an value difference statement. (< or >)
	 * @return if this token stream is an value difference comparison statement.
	 * @author Mike
	 */
	private boolean speculateValDif() {
		boolean success = true;
		mark();
		try {
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.LESS, ZMMLexer.GREATER);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		return success;
	}
	
	/**
	 * Speculates an value difference equality statement. (<= or >=)
	 * @return if this token stream is an value difference equality comparison statement.
	 * @author Mike
	 */
	private boolean speculateValDifEq() {
		boolean success = true;
		mark();
		try {
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.LESS, ZMMLexer.GREATER);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		return success;
	}

	/**
	 * whileS() matches the tokens required for a while statement
	 * @throws MismatchedTokenException
	 * @author Mike
	 */
	public void whileS() throws MismatchedTokenException {
		match(ZMMLexer.WHILE);
		match(ZMMLexer.OPAREN);
		stat();
		match(ZMMLexer.CPAREN);
		match(ZMMLexer.OBRACK);
		while(lookAhead(1) != ZMMLexer.CBRACK) {
			stat();
		}
		match(ZMMLexer.CBRACK);
	}
	
	public void forS() {
		// TODO: Zach implement this!
	}
	
	public void ifS() {
		// TODO: Zach implement this!
	}
	
	public void elseS() {
		// TODO: Zach implement this!
	}
	
}
