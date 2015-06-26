package compiler.parser;

import java.util.List;

import compiler.ast.AST;
import compiler.codegen.Context;
import compiler.codegen.InvalidTokenException;
import compiler.codegen.ZMMCodeGenerator;
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

	private ZMMCodeGenerator asmCodeGenerator;
	private AST.Program programNode;
	private AST.Visit visitor = new AST.Visit();
	private int count;
	
	public ZMMParser(Lexer input) {
		super(input);
		asmCodeGenerator = new ZMMCodeGenerator((ZMMLexer) input);
	}
	
	/**
	 * stats() is the entry point into the grammar.
	 * It matches statements until the end of file.
	 * @return a list of all the parsed tokens
	 * @throws MismatchedTokenException
	 * @author Mike	
	 */
	public List<Token> stats() throws MismatchedTokenException {
		programNode = new AST.Program();
		while(lookAhead(1) != ZMMLexer.EOF_TYPE) {
			stat();
		}
		match(ZMMLexer.EOF_TYPE);
		visitor.visit(programNode);
		return lookahead;
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
			//assign();
		} else if(speculateWhileS()) {
			//whileS();
		}else if(speculateIfS()) {
			//ifS();
		} else {
			throw new MismatchedTokenException("Expecting a statment; Found " + lookToken(1) + " " + lookToken(2));
		}
	}

	private boolean speculateNotEqual(){
		boolean success = true;
		mark();
		try{
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.OP);
			match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
		}
		catch(MismatchedTokenException e){
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
		boolean success = true;
        mark();
        try {
            ifS();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        //release();
        return success;
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
		//release();
		return success;
	}

    /**
     * Speculates assign
     * if try works success is returned as true
     * @return
     * @author Zach
     */
	private boolean speculateAssign() {
        boolean success = true;
        mark();
        try {
            assign();
        } catch(MismatchedTokenException e) {
            success = false;
        }
        //release();
        return success;
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
			String id = match(ZMMLexer.NAME).text;
			match(ZMMLexer.EQUALS);
			String value = match(ZMMLexer.NAME, ZMMLexer.VALUE).text;
			match(ZMMLexer.SEMI);
			programNode.addChild(new AST.Decl(new AST.Var(id, value), new AST.Var(value,value)));
		} else if(speculateVarAssign()) {
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
		} else if(speculateVarOperatorAssign()) {
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
	 * Speculates an assignment statement. (int x = 5;)
	 * @return if this token stream is an assignment statement.
	 * @author Mike
	 */
	private boolean speculateRegularAssign() {
		boolean success = true;
		Token name = null, val = null;
		mark();
		try {
			match(ZMMLexer.INT);
			name = match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			val = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		if(success && name != null && val != null) {
			count += asmCodeGenerator.generateDecl(name, val);
		}
		release();
		return success;
	}
	
	/**
	 * Speculates a variable assignment statement. (x = 6;)
	 * @return if this token stream is a variable assignment statement.
	 * @author Mike
	 */
	private boolean speculateVarAssign() {
		boolean success = true;
		Token name = null, val = null;
		mark();
		try {
			name = match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			val = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		if(success && name != null && val != null) {
			count += asmCodeGenerator.generateDecl(name, val);
		}
		release();
		return success;
	}

	/**
	 * Speculates an operator assignment statement. (int x = y + 5;)
	 * @return if this token stream is an operator assignment statement.
	 * @author Mike
	 */
	private boolean speculateOperatorAssign() {
		boolean success = true;
		Token val1 = null, val2 = null, val3 = null, op = null;
		mark();
		try {
			match(ZMMLexer.INT);
			val1 = match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			val2 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			op = match(ZMMLexer.OP);
			val3 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		if(success && val1 != null && val2 != null && val3 != null && op != null) {
			if(val2.type == ZMMLexer.VALUE && val3.type == ZMMLexer.NAME) {
				count += asmCodeGenerator.generateDecl(val3, val2);
			} else if(val2.type == ZMMLexer.NAME && val3.type == ZMMLexer.VALUE) {
				count += asmCodeGenerator.generateDecl(val2, val3);
			}
			try {
				count += asmCodeGenerator.generateArithmetic(op.text.toCharArray()[0], val1, val2, val3);
			} catch (InvalidTokenException e) {
				throw new Error("Couldn't generate assembly instruction.");
			}
		}
		release();
		return success;
	}
	
	/**
	 * Speculates a variable operator assignment statement. (x = x + 6;)
	 * @return if this token stream is a variable operator assignment statement.
	 * @author Mike
	 */
	private boolean speculateVarOperatorAssign() {
		boolean success = true;
		Token val1 = null, val2 = null, val3 = null, op = null;
		mark();
		try {
			val1 = match(ZMMLexer.NAME);
			match(ZMMLexer.EQUALS);
			val2 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			op = match(ZMMLexer.OP);
			val3 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		if(success && val1 != null && val2 != null && val3 != null && op != null) {
			try {
				count += asmCodeGenerator.generateArithmetic(op.text.toCharArray()[0], val1, val2, val3);
			} catch (InvalidTokenException e) {
				throw new Error("Couldn't generate assembly instruction.");
			}
		}
		release();
		return success;
	}

	/**
	 * comp() is the comparison section of the grammar. It 
	 * handles statements of equality and value comparisons
	 * for less than or greater than.
	 * 
	 * When we generate this code we want to do exactly the
	 * opposite of what the user wrote.
	 * 
	 * @throws MismatchedTokenException 
	 * @author Mike
	 */
	public Context comp() throws MismatchedTokenException {
		if(speculateEquality()) {
			Token t1 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.EQUALS);
			Token t2 = match(ZMMLexer.EQUALS);
			match(ZMMLexer.NAME, ZMMLexer.VALUE);
			if(speculateSemi())
				match(ZMMLexer.SEMI);
			return new Context(t1, t2, 'N', count);
		} else if (speculateValDif()) {
			Token t1 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			Token type = match(ZMMLexer.LESS, ZMMLexer.GREATER);
			Token t2 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			if(speculateSemi())
				match(ZMMLexer.SEMI);
			if(type.text == "<") {
				return new Context(t1, t2, 'G', count);
			} else if(type.text == ">") {
				return new Context(t1, t2, 'L', count);
			}
		} else if(speculateNotEqual()){
			Token t1 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			match(ZMMLexer.OP);
			match(ZMMLexer.EQUALS);
			Token t2 = match(ZMMLexer.NAME, ZMMLexer.VALUE);
			return new Context(t1, t2, 'E', count);
		} else {
			throw new MismatchedTokenException("Expecting a comparison; Found " + lookToken(1));
		}
		return null;
	}
	
	/**
	 * Speculates the next token is a semi colon token.
	 * @return if the next token in the stream is a semi colon
	 * @author Mike
	 */
	private boolean speculateSemi() {
		boolean success = true;
		mark();
		try {
			match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		release();
		return success;
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
			if(speculateSemi())
				match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		release();
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
			if(speculateSemi())
				match(ZMMLexer.SEMI);
		} catch(MismatchedTokenException e) {
			success = false;
		}
		release();
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
		Context c = comp();
		int countStart = count;
		match(ZMMLexer.CPAREN);
		match(ZMMLexer.OBRACK);
		asmCodeGenerator.mark();
		while(lookAhead(1) != ZMMLexer.CBRACK) {
			stat();
		}
		count += asmCodeGenerator.generateWhile(c, countStart, c.getCount());
		asmCodeGenerator.release();
		match(ZMMLexer.CBRACK);
	}

    /**
     * if statement
     * checks one AND ONLY one condition
     * everything in if statement is executed with while loop
     * @throws MismatchedTokenException
     * @author Zach
     */
	public void ifS() throws MismatchedTokenException {
        match(ZMMLexer.IF);
        match(ZMMLexer.OPAREN);
        Context c = comp();
        match(ZMMLexer.CPAREN);
        match(ZMMLexer.OBRACK);
        asmCodeGenerator.mark();
        int countStart = count;
        while(lookAhead(1) != ZMMLexer.CBRACK) {
            stat();
        }
        int countStart2 = count;
        match(ZMMLexer.CBRACK);
        asmCodeGenerator.mark();
        elseS();
        count += asmCodeGenerator.generateIf(c, countStart, c.getCount(), countStart2, count);
        asmCodeGenerator.release();
        asmCodeGenerator.release();
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
	
	public ZMMCodeGenerator getCodeGenerator() {
		return asmCodeGenerator;
	}
}
