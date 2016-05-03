package cliteCompiler;

import java.util.ArrayList;

public class Parser {
	
	FileOpener fileOpener = new FileOpener();
	Automaton lexer = new Automaton(fileOpener);
	Token token;


	private String match(Token t)
	{
		String value = token.value;
		if(token.type.equals(t.type)){
			token=lexer.next();
		}
		else
			error(t);		
		return value;
	}
	
	
	private void error(Token t)
	{
		System.err.println("Syntax error  : expecting : "+ t+ "   ; saw: " + token + " " + lexer.line);
		System.exit(1);
	}
	
	private void error(String t)
	{
		System.err.println("Syntax error  : expecting : "+ t+ "   ; saw: " + token + " " + lexer.line);
		System.exit(1);
	}
	
	private Program program()
	{
		//int main(){Declarations Statements}
		match(new Token().mkKey("int"));
		match(new Token().mkKey("main"));
		match(Token.LeftParentheses);
		match(Token.RightParentheses);
		match(new Token().mkSeparator("{"));
				
		
		Declarations decpart = declarations();
		Statements body = statements();
		
		match(new Token().mkSeparator("}"));
		return new Program(decpart,body);
	}
	
	
	private Declarations declarations()
	{
		//Declarations -> {Declaration}
		Declarations dec = new Declarations();
		while(isType())
		{
			dec.addAll(declaration());
		}
		
		return dec;
	}
	
	private ArrayList<Declaration> declaration()
	{
		//Declaration -> Type Identifier[[Integer]]{, Identifier[[Integer]]}
		
		ArrayList<Declaration> list = new ArrayList<Declaration>();
		Type t = type();
		Variable v= new Variable(match(Token.Identifier));
		Declaration d;
		//배열일 경우
		if(isLeftBracket()) 
			d= arrayDecl(t,v);
		else
			d = new VariableDecl(v, t);
		
		list.add(d);
		while(isComma())
		{
			match(Token.Comma);
			Variable v2= new Variable(match(Token.Identifier));
			Declaration d2;
			//배열일 경우
			if(isLeftBracket()) 
				d2= arrayDecl(t,v2);
			else
				d2 = new VariableDecl(v2, t);
			
			list.add(d2);
		}
		
		//문법에는 없는데 있어야될꺼같음★★★
		match(Token.Semicolon);
		return list;
	}
	
	private Type type()
	{
		Type t;
		if(token.type.equals(Token.typeInt.type)) t= new Type(Type.INT);
		else if(token.type.equals(Token.typeBool.type)) t= new Type(Type.BOOL);
		else if(token.type.equals(Token.typeFloat.type)) t= new Type(Type.FLOAT);
		else t= new Type(Type.CHAR);
		
		token=lexer.next();
		
		return t;
	}
	
	private Statements statements()
	{

		Statements statements = new Statements();
		
		while(isStatement())
		{
			statements.addAll(statement());
		}
		return statements;
	}
	
	private Statements statement()
	{
		//Statement -> ; | Block | Assignment | IfStatement | WhileStatement
		Statements s = new Statements();
		if(isSemicolon())
		{
			s.add(new Skip());
			token=lexer.next();
		}
		else if(isLeftBrace())
		{
			s.addAll(block());
		}
		else if(isIdendifier())
		{
			s.add(assignment());
		}
		else if(isIF())
		{
			s.add(ifStatement());
		}
		else if(isWhile())
		{
			s.add(WhileStatement());
		}
		return s;
	}
	private Statements block()
	{
		if(isLeftBrace())
			token = lexer.next();
		else
			error(Token.RightBrace);
		
		
		Statements s = statements();
		
		if(isRightBrace())
			token = lexer.next();
		else
			error(Token.RightBrace);
		
		return s;
	}
	
	private Assignment assignment()
	{
		//Assignment -> Identifier[[Expression]] = Expression;
		VariableRef target;
		String id = match(Token.Identifier);
		
		if(isLeftBracket())
			target = arrayRef(id);
		else
			target = new Variable(id);
		
		match(Token.Assign);
		Expression source = expression();
		match(Token.Semicolon);
		return new Assignment(target,source);
	}
	
	private VariableRef arrayRef(String id)
	{
		token=lexer.next();
		Expression index = expression();
		token=lexer.next();
		return new ArrayRef(id,index);
	}
	
	private Conditional ifStatement()
	{
		//IfStatement -> if(Expression) Statement [else Statement]
		Conditional con;
		
		match(Token.IF);
		match(Token.LeftParentheses);
		Expression test = expression();
		match(Token.RightParentheses);
		Statements thenbranch = statement();
		con = new Conditional(test,thenbranch);
		if(isElse())
		{
			token=lexer.next();
			Statements elsebranch = statement();
			con = new Conditional(test,thenbranch,elsebranch);
		}
		return con;
	}
	
	private Loop WhileStatement()
	{
		//WhileStatement - > while(Expression) Statement
		match(Token.WHILE);
		match(Token.LeftParentheses);
		Expression test = expression();
		match(Token.RightParentheses);
		Statements body  = statement();
		return new Loop(test,body);
	}
	
	private Expression expression()
	{
		//Conjunction{ || Conjunction}
		Expression exp = conjunction();
		while(isOrOp())
		{
			match(Token.OR);
			Operator op = new Operator("||");
			Expression exp2 = conjunction();
			exp = new Binary(op,exp,exp2);
		}
		
		return exp;
	}
	
	private Expression conjunction()
	{
		//Equality {&& Equality}
		Expression exp =equality();
		while(isAndOp())
		{
			match(Token.AND);
			Operator op = new Operator("&&");
			Expression exp2 =equality();
			exp= new Binary(op,exp,exp2);
		}
		
		return exp;
	}
	
	private Expression equality()
	{
		//Relation[EquOp Relation]
		Expression exp = relation();
		if(isEqualityOp())
		{
			Operator op = new Operator(token.type);
			match(token);
			Expression exp2 = relation();
			exp = new Binary(op,exp,exp2);
		}
		
		return exp;
	}
	
	private Expression relation()
	{
		//Addition[RelOp Addition]
		Expression exp = addition();
		if(isRelationalOp())
		{
			Operator op = new Operator(token.type);
			match(token);
			Expression exp2 = addition();
			exp = new Binary(op,exp,exp2);
		}
		return exp;
	}
	
	private Expression addition()
	{
		//Term{AddOp Term}
		Expression exp =term();
		while(isAddOp())
		{
			Operator op = new Operator(token.type);
			match(token);
			Expression exp2 =term();
			exp= new Binary(op,exp,exp2);
		}
		
		return exp;
	}
	
	private Expression term()
	{
		//Factor {MulOp Factor}
		Expression exp =factor();
		while(isMultiplyOp())
		{
			Operator op = new Operator(token.type);
			match(token);
			Expression exp2 =term();
			exp= new Binary(op,exp,exp2);
		}
		return exp;
	}
	
	private Expression factor()
	{
		//[Unary]Primary
		if(isUnaryOp())
		{
			UnaryOp op = new UnaryOp(token.type);
			match(token);
			Expression term = primary();
			return new Unary(op,term);
		}
		else
			return primary();
	}
	
	private Expression primary()
	{
		//Identifier[[Expression]] | Literal | (Expression) | Type(Expression)
		
		Expression e = null;
		if(isIdendifier())
		{
			String id = match(Token.Identifier);
			if(isLeftBracket())
			{
				match(Token.LeftBraket);
				Expression index = expression();
				match(Token.Rightbraket);
				e = new ArrayRef(id,index);				
			}
			else
			{
				e=new Variable(id);
			}
		}
		else if(isLiteral())
		{
			e= literal();
		}
		else if(isLeftParentheses())
		{
			match(Token.LeftParentheses);
			e= expression();
			match(Token.RightParentheses);
		}
		else if(isType())
		{
			UnaryOp op = new UnaryOp(Token.typeInt.type);
			match(token);
			match(Token.LeftParentheses);
			e= expression();
			match(Token.RightParentheses);
			e= new Unary(op,e);
		}
		else
			error("Identifier | Literal | (Expression) | Type(Expression)");
		
		return e;
	}
	
	private Value literal()
	{
		//IntValue | BoolValue | FloatValue | CharValue
		Value v=null;
		if(token.type.equals(Token.IntLiteral.type))
		{
			v = new IntValue(Integer.parseInt(match(token)));
		}
		else if(token.type.equals(Token.FloatLiteral.type))
		{
			v = new FloatValue(Float.parseFloat(match(token)));
		}
		else if(token.type.equals(Token.CharLiteral.type))
		{
			v= new CharValue(match(token).charAt(0));
		}
		else if(token.type.equals(Token.True.type))
		{
			v= new BoolValue(true);
			token=lexer.next();
		}
		else if(token.type.equals(Token.False.type))
		{
			v= new BoolValue(false);
			token=lexer.next();
		}
		else
			error("IntValue | BoolValue | FloatValue | CharValue");

		return v;
	}
	
	private Declaration arrayDecl(Type t, Variable v)
	{
		token=lexer.next(); //왼쪽 [ 제거
		int size = Integer.parseInt(match(Token.IntLiteral));
		if(!isRightBracket())
			error(Token.Rightbraket);
		else
			token=lexer.next();
		return new ArrayDecl(v,t,size);
	}
	
	private boolean isLeftBracket()
	{
		return token.type.equals(token.LeftBraket.type);
	}
	private boolean isRightBracket()
	{
		return token.type.equals(token.Rightbraket.type);
	}
	
	private boolean isSemicolon()
	{
		return token.type.equals(Token.Semicolon.type);
	}
	
	private boolean isIdendifier()
	{
		return token.type.equals(Token.Identifier.type);
	}
	
	private boolean isIF()
	{
		return token.type.equals(Token.IF.type);
	}
	
	private boolean isElse()
	{
		return token.type.equals(Token.ELSE.type);
	}
	private boolean isWhile()
	{
		return token.type.equals(Token.WHILE.type);
	}
	
	private boolean isOrOp()
	{
		return token.type.equals(Token.OR.type);
	}
	
	private boolean isAndOp()
	{
		return token.type.equals(Token.AND.type);
	}
	
	private boolean isAddOp()
	{
		return token.type.equals(Token.ADDOP.type) || token.type.equals(Token.MINUSOP.type);
	}
	
	private boolean isMultiplyOp()
	{
		return token.type.equals(Token.MULOP.type) || token.type.equals(Token.REMAINOP.type) || token.type.equals(Token.DIVOP.type);
	}
	
	private boolean isUnaryOp()
	{
		return token.type.equals(Token.MINUSOP.type)|| token.type.equals(Token.NOTOP.type);
	}
	
	private boolean isEqualityOp()
	{
		return token.type.equals(Token.Equal.type) || token.type.equals(Token.NotEqual.type);
	}
	
	private boolean isRelationalOp()
	{
		return token.type.equals(Token.Greater.type) || token.type.equals(Token.Egrater.type) ||
				token.type.equals(Token.Less.type) || token.type.equals(Token.ELess.type);
	}
	
	private boolean isType()
	{
		return token.type.equals(Token.typeBool.type)
				||token.type.equals(Token.typeChar.type)
				||token.type.equals(Token.typeFloat.type)
				||token.type.equals(Token.typeInt.type);
	}
	
	private boolean isLiteral()
	{
		return token.type.equals(Token.IntLiteral.type)||
				token.type.equals(Token.FloatLiteral.type)||
				token.type.equals(Token.CharLiteral.type) ||
				isBoolLiteral();
	}
	
	
	private boolean isBoolLiteral()
	{
		return token.type.equals(Token.False.type) || token.type.equals(Token.True.type);
	}
	private boolean isComma()
	{
		return token.type.equals(token.Comma.type);
	}
	
	
	
	private boolean isLeftParentheses()
	{
		return token.type.equals(Token.LeftParentheses.type);
	}
	
	private boolean isRightParentheses()
	{
		return token.type.equals(Token.RightParentheses.type);
	}
	
	private boolean isLeftBrace()
	{
		return token.type.equals(Token.LeftBrace.type);
	}
	
	private boolean isRightBrace()
	{
		return token.type.equals(Token.RightBrace.type);
	}
	
	private boolean isStatement()
	{
		return isSemicolon() || isLeftBrace() || isIF() || isWhile() || isIdendifier();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Parser parser = new Parser();
		parser.token=parser.lexer.next();
		Program p = parser.program();		
		
		p.print();

		
	}
	
}
