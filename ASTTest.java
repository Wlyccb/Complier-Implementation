package cop5556sp17;

import static cop5556sp17.Scanner.Kind.*;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.*;


public class ASTTest {

	static final boolean doPrint = true;
	static void show(Object s){
		if(doPrint){System.out.println(s);}
	}
	

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}



	@Test
	public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}
	
	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(3,5)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.arg();
		assertEquals(Tuple.class, ast.getClass());
		Tuple tuple = (Tuple) ast;
		List<Expression> list = tuple.getExprList();
		for (int i=0;i<list.size();i++){
			assertEquals(IntLitExpression.class, list.get(i).getClass());
		}
	}
	
	@Test
	public void testProgram0 () throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 file f1 {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.program();
		assertEquals(Program.class, ast.getClass());
		Program program = (Program)ast;
		List<ParamDec> list = program.getParams();
		for (int i=0;i<list.size();i++){
			assertEquals(ParamDec.class, list.get(i).getClass());
		}
	}
	@Test
	public void testfactor2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "true";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BooleanLitExpression.class, ast.getClass());
	}
	
	@Test
	public void testfactor3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "screenheight";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(ConstantExpression.class, ast.getClass());
	}
	
	@Test
	public void testfactor4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(abc)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}
	
	@Test
	public void testfactor5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "true & false+1";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
		BinaryExpression b = (BinaryExpression) be.getE0();
		assertEquals(BooleanLitExpression.class, b.getE0().getClass());
		assertEquals(BooleanLitExpression.class, b.getE1().getClass());
		assertEquals(AND, b.getOp().kind);
	}
	
	@Test
	public void testfactor6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(true & false+1)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
		BinaryExpression b = (BinaryExpression) be.getE0();
		assertEquals(BooleanLitExpression.class, b.getE0().getClass());
		assertEquals(BooleanLitExpression.class, b.getE1().getClass());
		assertEquals(AND, b.getOp().kind);
	}
	
	@Test
	public void testSleep() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sleep 3/5;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(SleepStatement.class, ast.getClass());
		SleepStatement sleep = (SleepStatement)ast;
		assertEquals(BinaryExpression.class, sleep.getE().getClass());
		BinaryExpression b = (BinaryExpression) sleep.getE();
		assertEquals(IntLitExpression.class, b.getE0().getClass());
		assertEquals(IntLitExpression.class, b.getE1().getClass());
		assertEquals(DIV, b.getOp().kind);
	}
	@Test
	public void testWhile() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while (true) {a<-3/5;b<-1*2;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(WhileStatement.class, ast.getClass());
		WhileStatement w = (WhileStatement)ast;
		assertEquals(BooleanLitExpression.class, w.getE().getClass());
		assertEquals(Block.class, w.getB().getClass());
		Block b = (Block)w.getB();
		List<Dec> listdec= b.getDecs();
		List<Statement> listst= b.getStatements();
		assertEquals(listdec.size(),0);
		assertEquals(listst.size(),2);
	}
	
	@Test
	public void testIf() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if (true) {a<-3/5;b<-1*2;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(IfStatement.class, ast.getClass());
		IfStatement w = (IfStatement)ast;
		assertEquals(BooleanLitExpression.class, w.getE().getClass());
		assertEquals(Block.class, w.getB().getClass());
		Block b = (Block)w.getB();
		List<Dec> listdec= b.getDecs();
		List<Statement> listst= b.getStatements();
		assertEquals(listdec.size(),0);
		assertEquals(listst.size(),2);
	}
	
	@Test
	public void testDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "integer awadaw";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.dec();
		assertEquals(Dec.class, ast.getClass());
		Dec dec = (Dec)ast;
		Token ident = dec.getIdent();
		assertEquals(ident.getText(),"awadaw");
	}
	
	@Test
	public void testIdentChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "awadaw";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(IdentChain.class, ast.getClass());
	}
	
	@Test
	public void testFilterOpChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur a/3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(FilterOpChain.class, ast.getClass());
		FilterOpChain filter= (FilterOpChain)ast;
		Tuple be= filter.getArg();
		assertEquals(Tuple.class, be.getClass());
		List<Expression> list = be.getExprList();
		for (int i=0;i<list.size();i++){
			assertEquals(BinaryExpression.class, list.get(i).getClass());
		}
	}
	@Test
	public void testFrameOpChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "show a/3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(FrameOpChain.class, ast.getClass());
		FrameOpChain filter= (FrameOpChain)ast;
		Tuple be= filter.getArg();
		assertEquals(Tuple.class, be.getClass());
		List<Expression> list = be.getExprList();
		for (int i=0;i<list.size();i++){
			assertEquals(BinaryExpression.class, list.get(i).getClass());
		}
	}
	
	@Test
	public void testImageOpChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "width 3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.chainElem();
		assertEquals(ImageOpChain.class, ast.getClass());
		ImageOpChain filter= (ImageOpChain)ast;
		Tuple be= filter.getArg();
		assertEquals(Tuple.class, be.getClass());
		List<Expression> list = be.getExprList();
		for (int i=0;i<list.size();i++){
			assertEquals(IntLitExpression.class, list.get(i).getClass());
		}
	}
	
	@Test
	public void testAssign() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "a<-3/5;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(AssignmentStatement.class, ast.getClass());
		AssignmentStatement as = (AssignmentStatement)ast;
		IdentLValue lv= as.getVar();
		Expression e = as.getE();
		assertEquals(IdentLValue.class, lv.getClass());
		assertEquals(BinaryExpression.class, e.getClass());
		assertEquals(lv.getText(), "a");
	}
	
	@Test
	public void testchain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "show (3/5,1*2) -> width(true & false,abc %3) |-> move(3/5+1/2,1*2+1*2,true & false|a+b);";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.statement();
		assertEquals(BinaryChain.class, ast.getClass());
		BinaryChain bc = (BinaryChain) ast;
		Chain chain= bc.getE0();
		ChainElem ce = bc.getE1();
		assertEquals(BinaryChain.class, chain.getClass());
		assertEquals(FrameOpChain.class, ce.getClass());
	}

	
}



