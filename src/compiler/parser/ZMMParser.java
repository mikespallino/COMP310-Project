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
	
	public List<Token> stats() throws MismatchedTokenException {
		while(lookAhead(1) != ZMMLexer.EOF_TYPE) {
			stat();
		}
		match(ZMMLexer.EOF_TYPE);
		return parsedTokens;
	}
	
	public void stat() throws MismatchedTokenException {
		if(speculateAssign()) {
			
		} else if(speculateComp()) {
			
		} else if(speculateWhileS()) {
			whileS();
		} else if(speculateForS()) {
			forS();
		} else if(speculateIfS()) {
			ifS();
		} else if(speculateElseS()) {
			elseS();
		} else {
			throw new MismatchedTokenException("Expecting a statment; Found " + lookToken(1));
		}
	}

    /**
     * Speculates elseS
     * if try works success is returned as true
     * @return
     * @author Zach
     */
	private boolean speculateElseS() {
		// TODO: Zach implement this!
        boolean success = true;
        mark();
        try {
            elseS();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        release();
        return success;
	}

    /**
     * Speculates ifS
     * if try works success is returned as true
     * @return
     * @author Zach
     */
	private boolean speculateIfS() {
		// TODO: Zach implement this!
        boolean success = true;
        mark();
        try {
            ifS();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        release();
        return success;
	}

    /**
     * Speculates forS
     * if try works success is returned as true
     * @return
     * @author Zach
     */
	private boolean speculateForS() {
		// TODO: Zach implement this!
        boolean success = true;
        mark();
        try {
            forS();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        release();
        return success;
	}

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

    /**
     * Speculates comp
     * if try works success is returned as true
     * @return
     * @author Zach
     */
	private boolean speculateComp() {
		// TODO: Zach implement this!
        boolean success = true;
        mark();
        try {
            comp();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        release();
        return success;
	}

    /**
     * Speculates assign
     * if try works success is returned as true
     * @return
     * @author Zach
     */
	private boolean speculateAssign() {
		// TODO: Zach implement this!boolean success = true;
        boolean success = true;
        mark();
        try {
            assign();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        release();
        return success;
	}

	/**
	 * Let Mike do this
	 * @throws MismatchedTokenException 
	 */
	public void assign() throws MismatchedTokenException {
		if(speculateRegularAssign()) {
			
		} else if(speculateOperatorAssign()) {
			
		} else {
			throw new MismatchedTokenException("Expecting an assignment; Found " + lookToken(1));
		}
	}
	
	private boolean speculateOperatorAssign() {
		// TODO: Let Mike do this
		return false;
	}

	private boolean speculateRegularAssign() {
		// TODO: Let Mike do this
		return false;
	}

	/**
	 * Let Mike do this
	 * @throws MismatchedTokenException 
	 */
	public void comp() throws MismatchedTokenException {
		if(speculateEquality()) {
			
		} else if(speculateGreater()) {
			
		} else if(speculateLess()) {
			
		} else {
			throw new MismatchedTokenException("Expecting a comparison; Found " + lookToken(1));
		}
	}
	
	private boolean speculateLess() {
		// TODO: Let Mike do this
		return false;
	}

	private boolean speculateGreater() {
		// TODO: Let Mike do this
		return false;
	}

	private boolean speculateEquality() {
		// TODO: Let Mike do this
		return false;
	}

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

    /**
     * for loop
     * creates variable, checks condition, and modifies variable
     * everything within for loop is executed via java while loop
     * @throws MismatchedTokenException
     * @author Zach
     */
	public void forS() throws MismatchedTokenException{
        match(ZMMLexer.FOR);
        match(ZMMLexer.OPAREN);
        stat();
        comp();
        stat();
        match(ZMMLexer.CPAREN);
        match(ZMMLexer.OBRACK);
        while(lookAhead(1) != ZMMLexer.CBRACK) {
            stat();
        }
        match(ZMMLexer.CBRACK);
	}

    /**
     * if statement
     * checks one AND ONLY one condition
     * everything in if statement is executed with while loop
     * @throws MismatchedTokenException
     * @author Zach
     */
	public void ifS() throws MismatchedTokenException{
        match(ZMMLexer.IF);
        match(ZMMLexer.OPAREN);
        comp();
        match(ZMMLexer.CPAREN);
        match(ZMMLexer.OBRACK);
        while(lookAhead(1) != ZMMLexer.CBRACK) {
            stat();
        }
        match(ZMMLexer.CBRACK);
	}

    /**
     * else statement
     * everything in else statement is executed through a while loop.
     * @throws MismatchedTokenException
     * @author Zach
     */
	public void elseS() throws MismatchedTokenException{
        match(ZMMLexer.ELSE);
        match(ZMMLexer.OBRACK);
        while(lookAhead(1) != ZMMLexer.CBRACK) {
            stat();
        }
        match(ZMMLexer.CBRACK);
	}
}
