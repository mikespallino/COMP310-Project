package compiler.parser;

import java.util.ArrayList;
import java.util.List;

import compiler.lexer.Lexer;
import compiler.lexer.Token;

/**
 * Generic Parser implementation. This provides base
 * methods for a backtracking parser.
 * @author Mike
 */
public class Parser {

	Lexer input;
	List<Integer> markers;
	protected List<Token> lookahead;
	int p = 0;
	
	public Parser(Lexer input) {
		this.input = input;
		markers = new ArrayList<Integer>();
		lookahead = new ArrayList<Token>();
	}
	
	/**
	 * lookToken(int i) will get the token at i in the buffer.
	 * @param i - position
	 * @return Token in the lookahead buffer at p+i-1
	 */
	public Token lookToken(int i) {
		sync(i);
		return getLookahead().get(p+i-1);
	}
	
	/**
	 * lookAhead(int i) will get a token in the lookahead buffer
	 * @param i - position
	 * @return The token type of the Token in the lookahead buffer at i.
	 */
	public int lookAhead(int i) {
		return lookToken(i).type;
	}
	
	/**
	 * match(int target) will try to match a specific token with
	 * the next in the stream.
	 * @param target - Token type you're trying to match
	 * @throws MismatchedTokenException
	 */
	public Token match(int target) throws MismatchedTokenException {
		Token temp;
		if(lookAhead(1) == target) {
			temp = lookToken(1);
			consume();
			return temp;
		} else {
			throw new MismatchedTokenException("Expecting " + input.getTokenName(target) + "; Found " + lookToken(1));
		}
	}
	
	/**
	 * match(int target1, int target2) will try to match either of the
	 * specified token with the next in the stream.
	 * @param target1 - a possible token type you're trying to match
	 * @param target2 - another possible token type you're trying to match
	 * @throws MismatchedTokenException
	 */
	public Token match(int target1, int target2) throws MismatchedTokenException {
		Token temp;
		if(lookAhead(1) == target1) {
			temp = lookToken(1);
			consume();
			return temp;
		} else if(lookAhead(1) == target2) {
			temp = lookToken(1);
			consume();
			return temp;
		} else {
			throw new MismatchedTokenException("Expecting either " + input.getTokenName(target1) + " or " + input.getTokenName(target2) + "; Found " + lookToken(1));
		}
	}
	
	/**
	 * sync(int i) increases the lookahead buffer size.
	 * @param i
	 */
	public void sync(int i) {
		if(p+i-1 > getLookahead().size()-1) {
			int n = (p+i-1) - (getLookahead().size() - 1);
			fill(n);
		}
	}
	
	/**
	 * fill(int n) fills the lookahead buffer with n tokens from the lexer.
	 * @param n - number of tokens to fill.
	 */
	public void fill(int n) {
		for(int i = 1; i <= n; i++) {
			getLookahead().add(input.nextToken());
		}
	}
	
	/**
	 * consume() increments the pointer of where you are looking
	 * in the stream.
	 */
	public void consume() {
		p++;
		if(p == getLookahead().size() && !isSpeculating()) {
			p = 0;
			getLookahead().clear();
		}
		sync(1);
	}
	
	/**
	 * mark() added an index marker for where in the stream
	 * to jump back to should the speculation fail.
	 * @return index
	 */
	public int mark() {
		markers.add(p);
		return p;
	}
	
	/**
	 * release() sets the position back to the previous marker.
	 */
	public void release() {
		int marker = markers.get(markers.size()-1);
		markers.remove(markers.size()-1);
		seek(marker);
	}
	
	/**
	 * seek(int index) will reset the pointer to the old marked
	 * marked index in the stream.
	 * @param index
	 */
	public void seek(int index) {
		p = index;
	}
	
	/**
	 * isSpeculating() returns whether or not the parser is speculating
	 * about something (is any markers have been set).
	 * @return is the parser speculating.
	 */
	public boolean isSpeculating() {
		return markers.size() > 0;
	}

	public List<Token> getLookahead() {
		return lookahead;
	}

	public void setLookahead(List<Token> lookahead) {
		this.lookahead = lookahead;
	}
	
}
