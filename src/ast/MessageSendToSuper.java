package ast;

public class MessageSendToSuper extends MessageSend { 

    public Type getType() { 
        return method.getType();
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
    	pw.print("super." + method.getName() + "(" );
    	if(parameterList != null) parameterList.genKra(pw);
		pw.print(")");
	}
    
    public MessageSendToSuper(Method method, ExprList parameterList) {
    	this.method = method;
    	this.parameterList = parameterList;
    }
    
    private ExprList parameterList;
    private Method method;
}