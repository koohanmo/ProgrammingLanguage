package cliteCompiler;
public class Automaton {

	private final String letters = "abcdefghijklmnopqrstuvwxyz"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String digits = "0123456789";
	public int line=1;
	
	FileOpener source;
	char current;
	
	Automaton(FileOpener source)
	{
		this.source=source;
		current=source.getChar();
	}
	
	public Token next()
	{
		Token token = new Token();
		do{
			if(isLetter(current)) //¼ýÀÚ¸é
			{
				String total = conCat(letters+digits);
				return token.keyword(total);
			}
			else if(isDigit(current))
			{
				String number= conCat(digits);
				if(current!='.') return token.mkInt(number);
				//float
				number += conCat(digits);
				return token.mkFloat(number);
			}
			else switch(current)
			{
			case ' ': case '\t' : case '\r' :
				current=source.getChar();
				if(current==(char)-1) return Token.EOF;
				continue;
			case '\n':
				line++;
				current=source.getChar();
				if(current==(char)-1) return Token.EOF;
				continue;
			case  '+': case '-': case '*': case ';': 
				token =token.mkOp(String.valueOf(current));
				current=source.getChar();
				return token;
			case '[' : case ']' :case '(' :case ')' :case '{' :case '}' : case ',':
				token=token.mkSeparator(String.valueOf(current));
				current=source.getChar();
				return token;
			case '/':
				current=source.getChar();
				if(current!='/') return token.mkOp("/"); 
				else 
					{ 	do
						{
							current=source.getChar();
						}while(current!='\n');
					
					}
				continue;
			case '>':
				current=source.getChar();
				if(current=='=')
					{
						current=source.getChar();
						return token.mkOp(">=");
					}
				else return token.mkOp(">");
			case '<':
				current=source.getChar();
				if(current=='=')
					{
						current=source.getChar();
						return token.mkOp("<=");
					}
				else return token.mkOp("<");
			case '=':
				current=source.getChar();
				if(current=='=')
					{
						current=source.getChar();
						return token.mkOp("==");
					}
				else return token.mkOp("=");

			case '|':
				current=source.getChar();
				if(current=='|')
					{
						current=source.getChar();
						return token.mkOp("||");
					}
				else return token.mkOp("|");
				
			case '&':
				current=source.getChar();
				if(current=='&')
					{
						current=source.getChar();
						return token.mkOp("&&");
					}
				else return token.mkOp("&");
				
			case '!':
				current=source.getChar();
				if(current=='=')
					{
						current=source.getChar();
						return token.mkOp("!=");
					}
				else return token.mkOp("!");
				
				default :   token.mkError(String.valueOf(current));
							current=source.getChar();
							return token;
				
			}
				
		}while(true);
		
	}
	
	
	public boolean isEnd()
	{
		return source.isEnd();
	}
	
	private boolean isLetter(char ch)
	{
		return ch>='a' && ch<='z' || ch>='A' && ch<='Z';
	}
	
	private boolean isDigit(char ch)
	{
		return ch>='0' && ch<='9';
	}
	
	private String conCat(String set)
	{
		StringBuffer buf = new StringBuffer("");
		do{
			buf.append(current);
			current=source.getChar();
			
		}while(set.indexOf(current)>=0);
		return buf.toString();
	}
	
	
}
