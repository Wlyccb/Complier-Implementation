package cop5556sp17;

import cop5556sp17.AST.*;
import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.util.ArrayList;

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
	ASTNode parse() throws SyntaxException {
		ASTNode ast = program();
		matchEOF();
		return ast;
	}

	Expression expression() throws SyntaxException {
		//TODO
		Token first = t;
		Expression e1 = null;
		Expression e2 = null;
		e1 = term();
		while (t.isKind(REL_OP)){
			Token op = consume();
			e2 = term();
			e1 = new BinaryExpression(first,e1,op,e2);
		}
		return e1;
	}

	Expression term() throws SyntaxException {
		//TODO
		Token first = t;
		Expression e1 = null;
		Expression e2 = null;
		e1 = elem();
		while (t.isKind(WEAK_OP)){
			Token op = consume();
			e2 = elem();
			e1 = new BinaryExpression(first,e1,op,e2);
		}
		return e1;
		//throw new UnimplementedFeatureException();
	}

	Expression elem() throws SyntaxException {
		//TODO
		Token first = t;
		Expression e1 = null;
		Expression e2 = null;
		e1 = factor();
		while (t.isKind(STRONG_OP)){
			Token op = consume();
			e2 = factor();
			e1 = new BinaryExpression(first,e1,op,e2);
		}
		return e1;
		//throw new UnimplementedFeatureException();
		
	}

	Expression factor() throws SyntaxException {
		Token first = t;
		Expression e1 = null;
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			e1 = new IdentExpression(consume());
		}
			break;
		case INT_LIT: {
			e1 = new IntLitExpression(consume());
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			e1 = new BooleanLitExpression(consume());
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			e1 = new ConstantExpression(consume());
		}
			break;
		case LPAREN: {
			consume();
			e1 = expression();
			match(RPAREN);
		}
			break;
		default:
			//you will want to provide a more useful error message
			throw new SyntaxException("illegal factor");
		}
		return e1;
	}

	Block block() throws SyntaxException {
		//TODO
		Token first = t;
		Block e = null;
		ArrayList<Dec> e1 = new ArrayList<Dec>();
		ArrayList<Statement> e2 = new ArrayList<Statement>();
		match(LBRACE);
		while (t.isKind(DEC_F) || t.isKind(STATEMENT_F)){
			if (t.isKind(DEC_F)) e1.add(dec());
			else if (t.isKind(STATEMENT_F))  e2.add(statement());
		}
		e = new Block(first,e1,e2);
		match(RBRACE);
		return e;
		//throw new UnimplementedFeatureException();
	}

	Program program() throws SyntaxException {
		//TODO
		Token first = t;
		Program e = null;
		ArrayList<ParamDec> paramList = new ArrayList<ParamDec>();
		Block b = null;
		match(IDENT);
		if (!t.isKind(LBRACE)) {
			paramList.add(paramDec());
			while (t.isKind(COMMA)){
				consume();
				paramList.add(paramDec());
			}
		}
		b = block();
		e = new Program(first,paramList,b);
		return e;
		//throw new UnimplementedFeatureException();
	}

	ParamDec paramDec() throws SyntaxException {
		//TODO
		Token first = t;
		ParamDec e = null;
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
		if (t.kind == IDENT) e = new ParamDec(first,consume());
		else throw new SyntaxException("illegal factor");
		return e;
		//throw new UnimplementedFeatureException();
	}

	Dec dec() throws SyntaxException {
		//TODO
		Token first = t;
		Dec e = null;
		match(DEC_F);
		Token e1 = t;
		match(IDENT);
		e = new Dec(first,e1);
		//throw new UnimplementedFeatureException();
		return e;
	}

	Statement statement() throws SyntaxException {
		//TODO
		Token first = t;
		Statement e = null;
		Kind kind = t.kind;
		if (t.isKind(OP_SLEEP)){
			consume();
			Expression e1 = expression();
			e = new SleepStatement(first,e1);
			match(SEMI);
		}
		else if (t.isKind(KW_WHILE)){
			match(KW_WHILE);
			match(LPAREN);
			Expression e1 = expression();
			match(RPAREN);
			Block e2 = block();
			e = new WhileStatement(first,e1,e2);
		}
		else if (t.isKind(KW_IF)){
			match(KW_IF);
			match(LPAREN);
			Expression e1 = expression();
			match(RPAREN);
			Block e2 = block();
			e = new IfStatement(first,e1,e2);
		}
		else if (t.isKind(IDENT) && scanner.peek().kind == ASSIGN){
			IdentLValue e1 = new IdentLValue(first);
			consume();
			consume();
			Expression e2 = expression();
			match(SEMI);
			e = new AssignmentStatement(first,e1,e2);
		}
		else {
			e = chain();
			match(SEMI);
		}
		return e;
		//throw new UnimplementedFeatureException();
	}

	Chain chain() throws SyntaxException {
		//TODO
		Token first = t;
		Chain e1 = chainElem();
		ChainElem e2 = null;
		if (t.isKind(ARROW) || t.isKind(BARARROW)) {
			Token op = consume();
			e2 = chainElem();
			e1 = new BinaryChain(first,e1,op,e2);
			//consume();
		}
		else throw new SyntaxException("SyntaxException");
		while (t.isKind(ARROW) || t.isKind(BARARROW)){
			Token op = consume();
			e2 = chainElem();
			e1 = new BinaryChain(first,e1,op,e2);
			//consume();
		}
		return e1;
		//throw new UnimplementedFeatureException();
	}

	ChainElem chainElem() throws SyntaxException {
		//TODO
		Token first = t;
		ChainElem e = null;
		if (t.isKind(IDENT)) e = new IdentChain(consume());
		else if (t.isKind(FILTER_OP)){
			Token op = consume();
			Tuple tuple = arg();
			e = new FilterOpChain(op,tuple);
		}
		else if (t.isKind(FRAME_OP)){
			Token op = consume();
			Tuple tuple = arg();
			e = new FrameOpChain(op,tuple);
		}
		else if (t.isKind(IMAGE_OP)){
			Token op = consume();
			Tuple tuple = arg();
			e = new ImageOpChain(op,tuple);
		}
		else throw new SyntaxException("expected chain element");
		return e;
		
		//throw new UnimplementedFeatureException();
	}

	Tuple arg() throws SyntaxException {
		//TODO
		Token first = t;
		ArrayList<Expression> expressions = new ArrayList<Expression>();
		Tuple tuple = null;
		if (t.isKind(LPAREN)){
			consume();
			expressions.add(expression());
			while (t.isKind(COMMA)) {
				match(COMMA);
				expressions.add(expression());
			}
			//System.out.println(t.kind);
			match(RPAREN);
		}
		tuple = new Tuple(first,expressions);
		return tuple;
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
		if (t.isKind(EOF)) {
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
		if (t.isKind(kind)) {
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
		if (t.isKind(kinds))  return consume();
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
	
	
	
	

}
