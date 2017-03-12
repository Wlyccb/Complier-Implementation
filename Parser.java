package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;
import cop5556sp17.Scanner.Token;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}
	
	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
	@SuppressWarnings("serial")	
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}
	
	static final Kind[] DEC_F = { KW_INTEGER,KW_BOOLEAN,KW_IMAGE,KW_FRAME};
	static final Kind[] STATEMENT_F = {OP_SLEEP,KW_WHILE,KW_IF,IDENT,OP_BLUR,OP_GRAY,OP_CONVOLVE, KW_SHOW,KW_HIDE,KW_MOVE,KW_XLOC,KW_YLOC,OP_WIDTH,OP_HEIGHT,KW_SCALE};
	static final Kind[] FILTER_OP = {OP_BLUR,OP_GRAY,OP_CONVOLVE};
	static final Kind[] FRAME_OP = {KW_SHOW,KW_HIDE,KW_MOVE,KW_XLOC,KW_YLOC};
	static final Kind[] IMAGE_OP = {OP_WIDTH,OP_HEIGHT,KW_SCALE};
	static final Kind[] REL_OP = {LT,LE,GT,GE,EQUAL,NOTEQUAL};
	static final Kind[] WEAK_OP = {PLUS,MINUS,OR};
	static final Kind[] STRONG_OP = {TIMES,DIV,AND,MOD};
	
	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	void parse() throws SyntaxException {
		program();
		matchEOF();
		return;
	}

	void expression() throws SyntaxException {
		//TODO
		term();
		while (isKind(REL_OP)){
			consume();
			term();
		}
	}

	void term() throws SyntaxException {
		//TODO
		elem();
		while (isKind(WEAK_OP)){
			consume();
			elem();
		}
		//throw new UnimplementedFeatureException();
	}

	void elem() throws SyntaxException {
		//TODO
		factor();
		while (isKind(STRONG_OP)){
			consume();
			factor();
		}
		//throw new UnimplementedFeatureException();
		
	}

	void factor() throws SyntaxException {
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			consume();
		}
			break;
		case INT_LIT: {
			consume();
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			consume();
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			consume();
		}
			break;
		case LPAREN: {
			consume();
			expression();
			match(RPAREN);
		}
			break;
		default:
			//you will want to provide a more useful error message
			throw new SyntaxException("illegal factor");
		}
	}

	void block() throws SyntaxException {
		//TODO
		match(LBRACE);
		while (isKind(DEC_F) || isKind(STATEMENT_F)){
			if (isKind(DEC_F)) dec();
			else if (isKind(STATEMENT_F))  statement();
		}
		match(RBRACE);
		
		//throw new UnimplementedFeatureException();
	}

	void program() throws SyntaxException {
		//TODO
		match(IDENT);
		if (!isKind(LBRACE)) {
			paramDec();
			while (isKind(COMMA)){
				consume();
				paramDec();
			}
		}
		block();
		if (!isKind(EOF)) System.out.println("NOT END");
		//throw new UnimplementedFeatureException();
	}

	void paramDec() throws SyntaxException {
		//TODO
		Kind kind = t.kind;
		switch(kind){
		case KW_URL:
			consume();
			break;
		case KW_FILE:
			consume();
			break;
		case KW_INTEGER:
			consume();
			break;
		case KW_BOOLEAN:
			consume();
			break;
		default:
			//you will want to provide a more useful error message
			System.out.println(t.kind);
			throw new SyntaxException("illegal factor");
		}
		if (t.kind == IDENT) consume();
		else throw new SyntaxException("illegal factor");
		//throw new UnimplementedFeatureException();
	}

	void dec() throws SyntaxException {
		//TODO
		match(DEC_F);
		match(IDENT);
		//throw new UnimplementedFeatureException();
	}

	void statement() throws SyntaxException {
		//TODO
		Kind kind = t.kind;
		if (isKind(OP_SLEEP)){
			consume();
			expression();
			match(SEMI);
		}
		else if (isKind(KW_WHILE)){
			match(KW_WHILE);
			match(LPAREN);
			expression();
			match(RPAREN);
			block();
		}
		else if (isKind(KW_IF)){
			match(KW_IF);
			match(LPAREN);
			expression();
			match(RPAREN);
			block();
		}
		else if (isKind(IDENT) && scanner.peek().kind == ASSIGN){
				consume();
				consume();
				expression();
				match(SEMI);
			}
		else {
			chain();
			match(SEMI);
		}

		//throw new UnimplementedFeatureException();
	}

	void chain() throws SyntaxException {
		//TODO
		chainElem();
		if (isKind( ARROW)) consume();
		else if (isKind(BARARROW)) consume();
		else throw new SyntaxException("SyntaxException");
		chainElem();
		while (isKind(ARROW) || isKind(BARARROW)){
			consume();
			chainElem();
		}
		
		//throw new UnimplementedFeatureException();
	}

	void chainElem() throws SyntaxException {
		//TODO
		if (isKind(IDENT)) consume();
		else if (isKind(FILTER_OP)){
			consume();
			arg();
		}
		else if (isKind(FRAME_OP)){
			consume();
			arg();
		}
		else if (isKind(IMAGE_OP)){
			consume();
			arg();
		}
		
		//throw new UnimplementedFeatureException();
	}

	void arg() throws SyntaxException {
		//TODO
		if (isKind(LPAREN)){
			consume();
			expression();
			while (isKind(COMMA)) {
				match(COMMA);
				expression();
			}
			//System.out.println(t.kind);
			match(RPAREN);
		}
		//throw new UnimplementedFeatureException();
	}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (isKind(EOF)) {
			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (isKind(kind)) {
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		if (isKind(kinds))  return consume();
		else {
			throw new SyntaxException("SyntaxException");
		}
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}
	
	
	private boolean isKind (Kind k){
		return k==t.kind;
	}
	private boolean isKind(Kind... kinds) {
		// TODO. Optional but handy
		for (Kind kind:kinds){
			if (t.kind == kind) return true;
		}
		return false;
	}
	

}
