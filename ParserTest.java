package cop5556sp17;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;


public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}

	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//scanner.show();
		//System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.arg();
	}

	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}


	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	
	
	
	//my tests
	@Test
	public void testfactor() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"12","abc","true","false","screenheight","screenwidth","(abc)"};
		for (int i=0;i<6;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.factor();
		}
		
	}
	
	@Test
	public void TestFactor2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"(3/5)","(1*2)","(true & false)","(abc %3)","(3/5+1/2)","(1*2+1*2)","(true & false|a+b)","(3/5<1*2)","3/5<1*2)","(3/5>1*2)","(3/5<=1*2)","(3/5>=1*2)","(3/5!=1*2)","(true & false==abc %3)"};
		for (int i=0;i<1;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.factor();
		}
	}
	
	@Test
	public void TestFactorError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "()";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//scanner.show();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.factor();
	}
	
	@Test
	public void TestElem() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"3/5","1*2","true & false","abc %3","a","1","true"};
		for (int i=0;i<7;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.elem();
		}
		
	}
	
	@Test
	public void TestElemError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"3+5","1-2","( & )","abc |3"};
		for (int i=0;i<4;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			thrown.expect(Parser.SyntaxException.class);
			parser.elem();
		}
	}
	
	@Test
	public void TestTerm() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"3/5","1*2","true & false","abc %3","3/5+1/2","1*2+1*2","true & false|a+b","abc","1"};
		for (int i=0;i<9;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.term();
		}
	}
	
	@Test 
	public void TestTermError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"{}","if a == b","while (a == b)"};
		for (int i=0;i<3;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			thrown.expect(Parser.SyntaxException.class);
			parser.term();
		}
	}
	
	@Test
	public void TestExpression() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"3/5","1*2","true & false","abc %3","3/5+1/2","1*2+1*2","true & false|a+b","3/5<1*2","3/5<1*2","3/5>1*2","3/5<=1*2","3/5>=1*2","3/5!=1*2","true & false==abc %3","abc","123"};
		for (int i=0;i<14;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.term();
		}
	}
	
	
	
	@Test
	public void TestExpressionError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		//String inputs[] = {"3/5!!1*2","3/5!1*2","3/501*2","3/5<>1*2","3/5$1*2","3/5abc1*2"};
		//for (int i=0;i<2;i++){
			Scanner scanner = new Scanner("3/5!1*2");
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			thrown.expect(Parser.SyntaxException.class);
			parser.program();
		//}
	}
	
	
	@Test 
	public void TestDec() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"integer a","boolean b","boolean c","image d","frame e"};
		for (int i=0;i<5;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.dec();
		}
	}
	@Test
	public void TestDecError() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String inputs[] = {"integer 234","boolean >","boolean 1","image boolean","frame   "};
		for (int i=1;i<5;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			thrown.expect(Parser.SyntaxException.class);
			parser.dec();
		}
	}

	@Test 
	public void TestArg() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"","(3/5,1*2)","(true & false,abc %3)","(3/5+1/2,1*2+1*2,true & false|a+b)","(3/5<1*2)","(3/5<1*2)","(3/5>1*2,3/5<=1*2,3/5>=1*2,3/5!=1*2)","(true & false==abc %3)","(abc)","(123)"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.arg();
		}
	}
	
	@Test
	public void TestChainElem()  throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"abc","blur ","gray (32)","convolve (abc)","show (3/5,1*2)","width(true & false,abc %3)"," move(3/5+1/2,1*2+1*2,true & false|a+b)"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.chainElem();
		}
	}
	
	@Test 
	public void TestChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String inputs[] = {"abc->blur","gray (32) |-> convolve (abc)","show (3/5,1*2) -> width(true & false,abc %3) |-> move(3/5+1/2,1*2+1*2,true & false|a+b)"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.chain();
		}
	}
	@Test public void TestStatement_chain()  throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"abc->blur;","gray (32) |-> convolve (abc);","show (3/5,1*2) -> width(true & false,abc %3) |-> move(3/5+1/2,1*2+1*2,true & false|a+b);"};
		//String inputs[] = {"gray (32) |-> convolve (abc);"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.statement();
		}
	}
	@Test public void TestStatement_assign()  throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"a<-3/5;","b<-1*2;","c<-true & false;","d<-abc %3;","d<-3/5+1/2;","d<-1*2+1*2;","d<-true & false|a+b;","d<-3/5<1*2;"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.statement();
		}
	}
	@Test public void TestStatement_sleep()  throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"sleep 3/5;","sleep 1*2;","sleep true & false;","sleep abc %3;","sleep 3/5+1/2;","sleep 1*2+1*2;","sleep true & false|a+b;","sleep 3/5<1*2;"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.statement();
		}
	}
	
	@Test 
	public void TestStatement_if_while()  throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"while (true & false==abc %3) {a<-3/5;b<-1*2;}"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.statement();
		}
	}
	
	@Test
	public void TestParamDec() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"url abc","file abc","integer bc","boolean abc"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.paramDec();
		}
	}
	@Test 
	public void TestBlock_Dec() throws IllegalCharException, IllegalNumberException, SyntaxException{
		//String inputs[] = {"integer a","boolean b","boolean c","image d","frame e"};
		String inputs[] = {"{integer a}","{boolean b}","{boolean c}","{image d}","{frame e}"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.block();
		}
	}
	@Test
	public void TestBlock_Statement() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs1[] = {"{abc->blur;}","{gray (32) |-> convolve (abc);}","{show (3/5,1*2) -> width(true & false,abc %3) |-> move(3/5+1/2,1*2+1*2,true & false|a+b);}"};
		String inputs2[] = {"{a<-3/5;b<-1*2;}","{c<-true & false;}","{d<-abc %3;}","{d<-3/5+1/2;}","{d<-1*2+1*2;}","{d<-true & false|a+b;}","{d<-3/5<1*2;}"};
		String inputs3[] = {"{sleep 3/5;}","{sleep 1*2;}","{sleep true & false;}","{sleep abc %3;}","{sleep 3/5+1/2;}","{sleep 1*2+1*2;}","{sleep true & false|a+b;}","{sleep 3/5<1*2;}"};
		String inputs4[] = {"{while (true & false==abc %3) {a<-3/5;b<-1*2;}}"};

		for (int i=0;i<inputs1.length;i++){
			Scanner scanner = new Scanner(inputs1[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.block();
		}
		for (int i=0;i<inputs2.length;i++){
			Scanner scanner = new Scanner(inputs2[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.block();
		}
		for (int i=0;i<inputs3.length;i++){
			Scanner scanner = new Scanner(inputs3[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.block();
		}
		for (int i=0;i<inputs4.length;i++){
			Scanner scanner = new Scanner(inputs4[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.block();
		}
	}
	@Test
	public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"p1 {integer a}","p2 {boolean b}","p3 {boolean c}","p4 {image d}","p5 {frame e}"};
		String inputs1[] = {"p1 {abc->blur;}","p1 {gray (32) |-> convolve (abc);}","p1 {show (3/5,1*2) -> width(true & false,abc %3) |-> move(3/5+1/2,1*2+1*2,true & false|a+b);}"};
		String inputs2[] = {"p1 {a<-3/5;b<-1*2;}","p1 {c<-true & false;}","p1 {d<-abc %3;}","p1 {d<-3/5+1/2;}","p1 {d<-1*2+1*2;}","p1 {d<-true & false|a+b;}","p1 {d<-3/5<1*2;}"};
		String inputs3[] = {"p1 {sleep 3/5;}","p1 {sleep 1*2;}","p1 {sleep true & false;}","p1 {sleep abc %3;}","p1 {sleep 3/5+1/2;}","p1 {sleep 1*2+1*2;}","p1 {sleep true & false|a+b;}","p1 {sleep 3/5<1*2;}"};
		String inputs4[] = {"p1 {while (true & false==abc %3) {a<-3/5;b<-1*2;}}"};
		for (int i=0;i<1;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.program();
		}
		for (int i=0;i<inputs1.length;i++){
			Scanner scanner = new Scanner(inputs1[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.program();
		}
		for (int i=0;i<inputs2.length;i++){
			Scanner scanner = new Scanner(inputs2[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.program();
		}
		for (int i=0;i<inputs3.length;i++){
			Scanner scanner = new Scanner(inputs3[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.program();
		}
		for (int i=0;i<inputs4.length;i++){
			Scanner scanner = new Scanner(inputs4[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.program();
		}
	}
	@Test 
	public void TestProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String inputs[] = {"p url abc, file abc{abc->blur;}","p file abc, integer bc{while (true & false==abc %3) {a<-3/5;b<-1*2;}}","p integer bc{d<-abc %3;}","p boolean abc{d<-true & false|a+b;}"};
		for (int i=0;i<inputs.length;i++){
			Scanner scanner = new Scanner(inputs[i]);
			scanner.scan();
			//scanner.show();
			Parser parser = new Parser(scanner);
			parser.program();
		}
	}
	@Test
	public void testProgram3() throws IllegalCharException, IllegalNumberException, SyntaxException{
	String input = "prog1 integer a, url abc123 {}";
	Parser parser = new Parser(new Scanner(input).scan());
	parser.parse();
	}

	@Test
	public void testParamDec() throws IllegalCharException, IllegalNumberException, SyntaxException{
	String inputs[] = {"integer dwadas","file sawaw","url asvas","boolean dwada121"};
	for(String s:inputs){
	Parser parser = new Parser(new Scanner(s).scan());
	parser.paramDec();
	}
	}

	@Test
	public void testBlock() throws IllegalCharException, IllegalNumberException, SyntaxException{
	String input[] ={"{}","{integer adwa while(asas<10){asas<-10;}}"};
	for(String s:input){
	Parser parser = new Parser(new Scanner(s).scan());
	parser.block();
	}

	}

	@Test
	public void testDec() throws IllegalCharException, IllegalNumberException, SyntaxException{
	String inputs[] = {"integer awadaw","boolean sasaw121","image sawa2121","frame a211"};
	for(String s:inputs){
	Parser parser = new Parser(new Scanner(s).scan());
	parser.dec();
	}
	}

	@Test
	public void testStatement() throws IllegalCharException, IllegalNumberException, SyntaxException{
	String inputs[] = {"while(asas<10){asas<-10;}","sleep 1221*12;","if(assa>10){assa<-10;}","dwada->blur->gray->convolve;","avass<-10;"};
	for(String s:inputs){
	Parser parser = new Parser(new Scanner(s).scan());
	parser.statement();
	}
	}
	
}

