package cop5556sp17;

import java.util.ArrayList;

import cop5556sp17.Scanner.Kind;

public class Scanner {
	/**
	 * Kind enum
	 */
//	comment ::=  /*  NOT( */ )*  */
//	token ::= ident | keyword   | frame_op_keyword   | filter_op_keyword   | image_op_keyword   | boolean_literal | int_literal   |  separator   | operator
//	ident ::= ident_start ident_part* (but not reserved)
//	ident_start ::=  A  ..  Z  |  a  ..  z  |  $  |  _
//	ident_part ::= ident_start | (  0  ..  9  )
//	int_literal ::=  0  | ( 1 .. 9 ) ( 0 .. 9 )*
//	keyword ::=  integer  |  boolean  |  image  |  url  |  file  |  frame  |  while  |  if  |  sleep  |  screenheight  |  screenwidth 
//	filter_op_keyword  ∷ =  gray  |  convolve  |  blur  |  scale
//	image_op_keyword  ∷ =  width  |  height
//	frame_op_keyword  ∷ =  xloc  |  yloc  |  hide  |  show  |  move
//	boolean_literal ::=  true  |  false
//	separator ::=  ;  |  ,  | (  |  )  |  {  |  }
//	operator ::=  |  |  & | ==  |  !=  |  <  |  >  |  <=  |  >=  |  +  |  -   |  *   |  /   |  %  |  !  |  ->  |  |->  |  <-	
	
	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}
/**
 * Thrown by Scanner when an illegal character is encountered
 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}
	
	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
	public IllegalNumberException(String message){
		super(message);
		}
	}
	

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;
		
		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}
		

	public class Token {
		public final Kind kind;
		public final int pos;  //position in input array
		public final int length;
		//public final string text;

		//returns the text of this Token
		public String getText() {
			//TODO IMPLEMENT THIS
			return chars.substring(pos,pos+length);
		}
		
		@Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }

		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }

		 

		  private Scanner getOuterType() {
		   return Scanner.this;
		  }
		
		
		
		
		
		
		
		//returns a LinePos object representing the line and column of this Token
		LinePos getLinePos(){
			//TODO IMPLEMENT THIS
			int line = 0;
			int preline = 0;
			int posinline = 0;
			for (int i=0;i<ns.size();i++){
				if (ns.get(i) < pos) line++;
				else {
					if (i != 0) preline = ns.get(i-1)+1;
					break;
				}
			}
			if (line == ns.size()) posinline = pos - ns.get(ns.size()-1)-1;
			else posinline = pos-preline;
			LinePos lp = new LinePos(line,posinline);
			return lp;
			
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}
		

		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException{
			//TODO IMPLEMENT THIS
			String num = chars.substring(pos,pos+length);
			return Integer.parseInt(num);
		}
		public boolean isKind (Kind k){
			return k==this.kind;
		}
		public boolean isKind(Kind... kinds) {
			// TODO. Optional but handy
			for (Kind kind:kinds){
				if (this.kind == kind) return true;
			}
			return false;
		}
		
	}

	 


	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
		ns = new ArrayList<Integer>();
		tokenNum = 0;

	}
	
	
	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		//int pos = 0; 
		//TODO IMPLEMENT THIS!!!!
		int p = 0;
		while (p!=chars.length()){
			char c = chars.charAt(p);
			switch(c){
			//separator
			case ' ':
				p++;
				break;
			case ';':
				tokens.add(new Token(Kind.SEMI,p++,1));
				break;
			case ',':
				tokens.add(new Token(Kind.COMMA,p++,1));
				break;
			case '(':
				tokens.add(new Token(Kind.LPAREN,p++,1));
				break;
			case ')':
				tokens.add(new Token(Kind.RPAREN,p++,1));
				break;
			case '{':
				tokens.add(new Token(Kind.LBRACE,p++,1));
				break;
			case '}':
				tokens.add(new Token(Kind.RBRACE,p++,1));
				break;
			//operator
			case '|':
				if (p+2 < chars.length() && chars.charAt(p+1) == '-' && chars.charAt(p+2) == '>'){
						tokens.add(new Token(Kind.BARARROW,p,3));
						p += 3; 
				}
				else {
					tokens.add(new Token(Kind.OR,p++,1));
				}
				break;
			case '&':
				tokens.add(new Token(Kind.AND,p++,1));
				break;
			case '=':
				if (p+1<chars.length() && chars.charAt(p+1) == '='){
					tokens.add(new Token(Kind.EQUAL,p,2));
					p += 2;
				}
				else {
					//wrong token
					throw new IllegalCharException("IllegalCharException");
				}
				break;
			case '!':
				if (p+1<chars.length() && chars.charAt(p+1) == '=') {
					tokens.add(new Token(Kind.NOTEQUAL,p,2));
					p += 2;
					break;
				}
				else {
					tokens.add(new Token(Kind.NOT,p++,1));
					break;
				}
			case '<':
				if (p+1<chars.length() && chars.charAt(p+1) == '='){
					tokens.add(new Token(Kind.LE,p,2));
					p += 2;
				}
				else if (p+1<chars.length() && chars.charAt(p+1) == '-'){
					tokens.add(new Token(Kind.ASSIGN,p,2));
				    p += 2;
				}
				else {
					tokens.add(new Token(Kind.LT,p++,1));
				}
				break;
		   case '>':
			   if (p+1<chars.length() && chars.charAt(p+1) == '='){
				   tokens.add(new Token(Kind.GE,p,2));
				   p += 2;
			   }
			   else if (p+1<chars.length() && chars.charAt(p+1) == '-'){
				   tokens.add(new Token(Kind.ARROW,p,2));
				   p += 2;
			   }
			   else {
				   tokens.add(new Token(Kind.GT,p++,1));
			   }
			   break;
		   case '+':
			   tokens.add(new Token(Kind.PLUS,p++,1));
			   break;
		   case '-':
			   if (p+1<chars.length() && chars.charAt(p+1) == '>'){
				   tokens.add(new Token(Kind.ARROW,p,2));
				   p += 2;
			   }
			   else tokens.add(new Token(Kind.MINUS,p++,1));
			   break;
		   case '*':
			   tokens.add(new Token(Kind.TIMES,p++,1));
			   break;
		   case '%':
			   tokens.add(new Token(Kind.MOD,p++,1));
			   break;
		   case '/':
			   if (p+1 < chars.length() && chars.charAt(p+1) == '*'){
				   //comment
				   int head = p+2;
				   while (head < chars.length() && !(chars.charAt(head) == '*' && chars.charAt(head+1) == '/')) {
					   if (chars.charAt(head) == '\n') ns.add(head);
					   head++;
				   }
				   if (head == chars.length()) throw new IllegalCharException("IllegalCharException");
				   else p= head+2;
			   }
			   else {
				   tokens.add(new Token(Kind.DIV,p++,1));
			   }
			   break;
		   case '0':
			   tokens.add(new Token(Kind.INT_LIT,p++,1));
			   break;
		   case '\n':
			   ns.add(p++);
			   break;
		   case '\r':
			   p++;
			   break;
		   default:
			   //numbers
			   if (Character.isDigit(c)){
				   int h = p;
				   while (h<chars.length() && Character.isDigit(chars.charAt(h))) h++;	
				   String num = chars.substring(p, h);
				   try {
					   int n = Integer.parseInt(num);
					   tokens.add(new Token(Kind.INT_LIT,p,num.length()));
					   p += h-p;
					   p = h;
				   }catch(NumberFormatException e){
					   throw new IllegalNumberException("IllegalNumberException");
				   }
				
			   }
			   //letters
			   else if (Character.isLetter(c) || c == '$' || c == '_'){
				   int h2 = p;
				   while (h2<chars.length()){
					   char cc = chars.charAt(h2);
					   if (!(Character.isDigit(cc) || Character.isLetter(cc) || cc == '_' || cc == '$')){
						   break;
					   }
					   else h2++;
				   }
				   String ident = chars.substring(p,h2);
				   switch(ident){
				   case "integer":
					   tokens.add(new Token(Kind.KW_INTEGER,p,7));
					   break;
				   case "boolean":
					   tokens.add(new Token(Kind.KW_BOOLEAN,p,7));
					   break;
				   case "image":
					   tokens.add(new Token(Kind.KW_IMAGE,p,5));
					   break;
				   case "url":
					   tokens.add(new Token(Kind.KW_URL,p,3));
					   break;
				   case "file":
					   tokens.add(new Token(Kind.KW_FILE,p,4));
					   break;
				   case "frame":
					   tokens.add(new Token(Kind.KW_FRAME,p,5));
					   break;
				   case "while":
					   tokens.add(new Token(Kind.KW_WHILE,p,5));
					   break;
				   case "if":
					   tokens.add(new Token(Kind.KW_IF,p,2));
					   break;
				   case "true":
					   tokens.add(new Token(Kind.KW_TRUE,p,4));
					   break;
				   case "false":
					   tokens.add(new Token(Kind.KW_FALSE,p,5));
					   break;
				   case "sleep":
					   tokens.add(new Token(Kind.OP_SLEEP,p,5));
					   break;
				   case "screenheight":
					   tokens.add(new Token(Kind.KW_SCREENHEIGHT,p,12));
					   break;
				   case "screenwidth":
					   tokens.add(new Token(Kind.KW_SCREENWIDTH,p,11));
					   break;
				   case "gray":
					   tokens.add(new Token(Kind.OP_GRAY,p,4));
					   break;
				   case "convolve":
					   tokens.add(new Token(Kind.OP_CONVOLVE,p,8));
					   break;
				   case "blur":
					   tokens.add(new Token(Kind.OP_BLUR,p,4));
					   break;
				   case "scale":
					   tokens.add(new Token(Kind.KW_SCALE,p,5));
					   break;
				   case "width":
					   tokens.add(new Token(Kind.OP_WIDTH,p,5));
					   break;
				   case "height":
					   tokens.add(new Token(Kind.OP_HEIGHT,p,5));
					   break;
				   case "xloc":
					   tokens.add(new Token(Kind.KW_XLOC,p,4));
					   break;
				   case "yloc":
					   tokens.add(new Token(Kind.KW_YLOC,p,4));
					   break;
				   case "hide":
					   tokens.add(new Token(Kind.KW_HIDE,p,4));
					   break;
				   case "show":
					   tokens.add(new Token(Kind.KW_SHOW,p,4));
					   break;
				   case "move":
					   tokens.add(new Token(Kind.KW_MOVE,p,4));
					   break;
				   default:
					   tokens.add(new Token(Kind.IDENT,p,h2-p));  
				   }
				   p = h2;
			   }
			   else {
				   throw new IllegalCharException("IllegalCharException");
			   }
			   //digits or letters
			}
	    	}
		tokens.add(new Token(Kind.EOF,p,0));
		return this;  
	}

	final ArrayList<Token> tokens;
	final String chars;
	final ArrayList<Integer> ns;
	int tokenNum;

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}
	
	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);		
	}

	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		//TODO IMPLEMENT THIS
		return t.getLinePos();
	}
	
	
	//test functions
	public void show(){
		for (int i=0;i<tokens.size();i++){
			System.out.println(tokens.get(i).getText());
		}
	}
	
	public void showns(){
		for (int i=0;i<ns.size();i++){
			System.out.println(ns.get(i));
		}
	}


}
