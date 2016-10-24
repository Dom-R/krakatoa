package ast;


public class MessageSendToVariable extends MessageSend { 

    public Type getType() { 
        return method.getType();
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
    	pw.print( variable.getName() + "." + method.getName() + "(" );
		if(parameterList != null) parameterList.genKra(pw);
		pw.print(")");
	}
    
    public MessageSendToVariable(Variable variable, Method method, ExprList parameterList) {
    	this.variable = variable;
    	this.method = method;
    	this.parameterList = parameterList;
    }
    
    private ExprList parameterList;
    private Method method;
    private Variable variable;
}