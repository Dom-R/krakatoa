package ast;


abstract public class MessageSend  extends Expr  {
	abstract public void genKra( PW pw, boolean putParenthesis );
	
	abstract public Type getType();
	abstract public String getName();
}

