package cliteCompiler;

public class Token {
	
	public static final Token IntLiteral = new Token("IntLiteral","");
	public static final Token FloatLiteral = new Token("FloatLiteral","");
	public static final Token CharLiteral = new Token("CharLiteral","");
	public static final Token BooleanLiteral = new Token("BooleanLiteral","");
	
	
	public static final Token Identifier = new Token("Identifier","");
	public static final Token Comma = new Token(",","");
	public static final Token Semicolon = new Token(";","");
	
	public static final Token LeftParentheses = new Token("(",""); 
	public static final Token RightParentheses = new Token(")","");
	
	public static final Token LeftBrace = new Token("{",""); 
	public static final Token RightBrace = new Token("}","");
	public static final Token LeftBraket = new Token("[",""); 
	public static final Token Rightbraket = new Token("]","");
	
	public static final Token typeInt = new Token("int","");
	public static final Token typeBool = new Token("bool","");
	public static final Token typeFloat = new Token("float","");
	public static final Token typeChar = new Token("char","");
	public static final Token IF = new Token("if","");
	public static final Token ELSE = new Token("else","");
	public static final Token WHILE = new Token("while","");
	public static final Token Assign = new Token("=","");
	
	public static final Token OR = new Token("||","");
	public static final Token AND = new Token("&&","");
	
	public static final Token Equal = new Token("==","");
	public static final Token NotEqual = new Token("!=","");
	
	public static final Token Greater = new Token("<","");
	public static final Token Egrater = new Token("<=","");
	public static final Token Less = new Token(">","");
	public static final Token ELess = new Token(">=","");
	
	public static final Token ADDOP = new Token("+","");
	public static final Token MINUSOP = new Token("-","");
	public static final Token NOTOP = new Token("!","");
	
	public static final Token MULOP = new Token("*","");
	public static final Token DIVOP = new Token("/","");
	public static final Token REMAINOP = new Token("%","");
	
	public static final Token True = new Token("true","");
	public static final Token False = new Token("false","");
	
	
	public static final Token EOF = new Token("EOF","");

	public String type="";
	public String value="";
	
	public Token()
	{
		
	}
	
	public Token(String type, String value)
	{
		this.type=type;
		this.value=value;
	}
	
	public Token keyword(String s)
	{
		char ch= s.charAt(0);
		if(ch>='A' && ch<='Z') return mkID(s);
		else
		{
			for(String t: TokenType.keyword)
			{
				if(s.equals(t))
				{
					this.type=t;
					return this;
				}
			}
		}
		return mkID(s);
	}
	
	public Token mkLine(String s)
	{
		type="line";
		value=s;
		return this;
	}
	
	public Token mkError(String s)
	{
		type="ERROR";
		value=s;
		return this;
	}
	
	public Token mkID(String s)
	{
		type="Identifier";
		value =s;
		return this;
	}
	
	public Token mkInt(String s)
	{
		type="IntLiteral";
		value=s;
		return this;
	}
	
	public Token mkFloat(String s)
	{
		type="FloatLiteral";
		value=s;
		return this;
	}
	public Token mkChar(String s)
	{
		type="CharLiteral";
		value=s;
		return this;
	}
	
	public Token mkOp(String s)
	{
		type=s;
		value=null;
		return this;
	}
	
	public Token mkSeparator(String s)
	{
		type=s;
		value=null;
		return this;
	}
	
	public Token mkKey(String s)
	{
		type=s;
		value=null;
		return this;
	}
	
	
	public Token mkScript()
	{
		type="sc";
		return this;
	}
	
	public String toString()
	{
		if(type=="line") return type+value;
		if(value==null) return "   "+type;
		else return "   "+type+"  "+value;
		
	}
	
	
	
}
