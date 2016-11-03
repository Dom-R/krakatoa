package ast;

import java.util.*;

public class MessageSendToSelf extends MessageSend {
    
    public Type getType() { 
        return method.getType();
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
    }
    
    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
    	if(variable == null) {
    		pw.print("this." + method.getName() + "(" );
    	} else {
    		pw.print("this." + variable.getName() + "." + method.getName() + "(" );
    	}
    		
		if(parameterList != null) parameterList.genKra(pw);
		pw.print(")");
	}
    
    public MessageSendToSelf(Method method, ExprList parameterList) {
    	this.variable = null;
    	this.method = method;
    	this.parameterList = parameterList;
    }
    
    public MessageSendToSelf(Variable variable, Method method, ExprList parameterList) {
    	this.variable = variable;
    	this.method = method;
    	this.parameterList = parameterList;
    }
    
    public String getName() {
    	String nome = "this.";
    	if(variable == null) {
    		nome += method.getName() + "(";
    	} else {
    		nome += variable.getName() + "." + method.getName() + "(";
    	}
    	
    	if(parameterList != null) {
    		Iterator<Expr> iter = parameterList.getExprListIterator();
    		while(iter.hasNext()) {
    			Expr e = iter.next();
    			nome += e.getName();
    			if(iter.hasNext()) {
    				nome += ",";
    			}
    		}
    	}
    	
    	nome += ")";
    	
    	return nome;
    	
    }
    
    private Variable variable;
    private ExprList parameterList;
    private Method method;
    
}