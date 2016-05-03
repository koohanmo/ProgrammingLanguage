package cliteCompiler;

import java.util.ArrayList;

class Program
{
	//Program = Declarations decpart; Statements body;
	Declarations decpart;
	Statements body;
	
	Program(Declarations d, Statements b)
	{
		this.decpart=d;
		this.body=b;
	}
	
	public void print()
	{
		decpart.print(0);
		body.print(0);
	}
}

class Declarations extends ArrayList<Declaration>
{
	//Declaration*
	public void print(int depth)
	{
		for(Declaration d : this)
		{
			d.print(depth);
		}
	}
}


abstract class Declaration 
{
	//VariableDecl | ArrayDecl
	abstract void print(int depth);
}

class VariableDecl extends Declaration
{
	//Variable v; Type t
	Variable v;
	Type t;
	VariableDecl(Variable v, Type t)
	{
		this.v=v;
		this.t=t;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		
		System.out.println("VariableDecl");
		t.print(depth+1);
		System.out.println();
		v.print(depth+1);
		System.out.println();
				
	}
	

}

class ArrayDecl extends Declaration
{
	//Variable v; Type t; Integer size
	Variable v;
	Type t;
	int size;
	ArrayDecl(Variable v, Type t, int size)
	{
		this.v=v;
		this.t=t;
		this.size=size;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("ArrayDecl");
		t.print(depth+1);
		System.out.println();
		v.print(depth+1);
		System.out.println();
		for(int i=0;i<depth+1;i++)
		{
			if(i+1==depth+1) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("Size" + size);
				
	}
}

class Type
{
	//int | bool | float | char
	public static String INT="int";
	public static String BOOL="bool";
	public static String FLOAT="float";
	public static String CHAR="char";
	
	public String id;
	
	public Type(String id)
	{
		this.id=id;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.print("Type  "+id);
	}
}

class Statements extends ArrayList<Statement>
{
	//Statement*
	public void print(int depth)
	{
		for(Statement s : this)
		{
			s.print(depth);
		}
	}
}

abstract class Statement
{
	//Skip | Block | Assignment | Conditional | Loop
	abstract void print(int depth);
}

class Skip extends Statement
{
	public void print(int depth)
	{
		
	}
}

class Block extends Statement
{
	//Block = Statements
	Statements s;
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		s.print(depth);
	}
}
class Conditional extends Statement
{
	//Expression test; Statement thenbranch, elsebranch
	Expression test; 
	Statements thenbranch, elsebranch;

	public Conditional(Expression test, Statements thenbranch)
	{
		this.test=test;
		this.thenbranch=thenbranch;
	}
	
	public Conditional(Expression test, Statements thenbranch, Statements elsebranch)
	{
		this.test=test;
		this.thenbranch=thenbranch;
		this.elsebranch=elsebranch;
	}

	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("Conditional");
		test.print(depth+1);
		thenbranch.print(depth+1);
		if(elsebranch!=null)
			{
			for(int i=0;i<depth+1;i++)
			{
				if(i+1==depth+1) System.out.print("    戌式式式式式式式式式式  ");
				else System.out.print("\t\t");
			}
			System.out.println("Else");
				elsebranch.print(depth+2);
			}
	}
	
	
}

class Loop extends Statement
{
	//Expression test; Statement body
	Expression test;
	Statements body;
	
	public Loop(Expression test, Statements body)
	{
		this.test=test;
		this.body=body;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.print("Loop");
		System.out.println();
		test.print(depth+1);
		System.out.println();
		
		body.print(depth+1);
		System.out.println();
	}
}

class Assignment extends Statement
{
	//VariableRef target; Expression source
	VariableRef target;
	Expression source;
	
	public Assignment(VariableRef target, Expression source)
	{
		this.target=target;
		this.source = source;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("Assignment");
		
		
		target.print(depth+1);
		System.out.println();
		
		
		source.print(depth+1);
		System.out.println();
	}
	
	
}

abstract class Expression{
	//VariableRef | Value | Binary | Unary
	abstract void print(int depth);
}

abstract class VariableRef extends Expression
{
	//Variable | ArrayRef
	abstract void print(int depth);
}
class Variable extends VariableRef
{
	String id;
	public Variable(String id)
	{
		this.id=id;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.print("Variable   "+id+"  ");
	}

	
}
class ArrayRef extends VariableRef
{
	String id;
	Expression index;
	
	public ArrayRef(String id, Expression index)
	{
		this.id=id;
		this.index = index;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("ArrayRef");
		for(int i=0;i<depth+1;i++)
		{
			if(i+1==depth+1) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("Variable   "+id);
		index.print(depth+1);
	}

}

class Binary extends Expression
{
	Operator op;
	Expression term1, term2;
	
	public Binary(Operator op, Expression term1, Expression term2)
	{
		this.op=op;
		this.term1=term1;
		this.term2=term2;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.print("Binary");
		System.out.println();
		op.print(depth+1);
		System.out.println();
		term1.print(depth+1);
		System.out.println();
		term2.print(depth+1);
		System.out.println();
	}
	
}

class Unary extends Expression
{
	UnaryOp op;
	Expression term;
	
	public Unary(UnaryOp op, Expression term)
	{
		this.op= op;
		this.term = term;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("Unary");
		op.print(depth+1);
		System.out.println();
		term.print(depth+1);
		System.out.println();
	}
}

class Operator
{
	//BooleanOp | RelationalOp | ArithmeticOp
	String op;
	public Operator(String op)
	{
		this.op=op;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.print("Operator  "+op);
	}
}

class BooleanOp extends Operator
{
	public static final String andOp="&&";
	public static final String orOp="||";
	
	public BooleanOp(String op)
	{
		super(op);
	}
}

class RelationalOp extends Operator
{
	public static final String equalOp = "=";
	public static final String notEqualOp = "!=";
	public static final String greaterOp = "<";
	public static final String greaterAndlOp = "<=";
	public static final String lessOp = ">";
	public static final String lessAndOp = ">=";
	
	public RelationalOp(String op)
	{
		super(op);
	}
}

class ArithmeticOp extends Operator
{
	public static final String addOp="+";
	public static final String minusOp="-";
	public static final String mulOp="*";
	public static final String divOp="/";
	
	public ArithmeticOp(String op)
	{
		super(op);
	}
}

class UnaryOp
{
	public static final String not = "!";
	public static final String minus="-";
	
	String op;
	public UnaryOp(String op)
	{
		this.op=op;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.print(op);
	}
}

abstract class Value extends Expression
{
	//IntValue | Bool Value | FloatValue | CharValue
	abstract void print(int depth);
}

class IntValue extends Value
{
	int intValue;
	
	public IntValue(int v)
	{
		this.intValue=v;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("IntValue   " + intValue);
	}
}

class FloatValue extends Value
{
	float floatValue;
	
	public FloatValue(float v)
	{
		this.floatValue=v;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("floatValue   " + floatValue);
	}
}

class BoolValue extends Value
{
	Boolean boolValue;
	
	public BoolValue(Boolean b)
	{
		boolValue=b;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
			
		}
		System.out.println("BoolValue   " + boolValue);
	}
}

class CharValue extends Value
{
	char charValue;
	
	public CharValue(char c)
	{
		this.charValue=c;
	}
	
	public void print(int depth)
	{
		for(int i=0;i<depth;i++)
		{
			if(i+1==depth) System.out.print("    戌式式式式式式式式式式  ");
			else System.out.print("\t\t");
		}
		System.out.println("CharValue   " + charValue);
	}
}