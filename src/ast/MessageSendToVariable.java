/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.Iterator;

public class MessageSendToVariable extends MessageSend {

    public Type getType() {
        return method.getType();
    }

    public void genC( PW pw, boolean putParenthesis ) {
    	if(method.isPublic()) {
    		pw.print("( (" + method.getType().getCname() + " (*)(" + variable.getType().getCname());
    		
    		// Cast dos parametros adicionais
    		if(parameterList != null) {
	    		Iterator<Expr> iter = parameterList.getExprListIterator();
	    		while(iter.hasNext()) {
	    			pw.print(", ");
	    			pw.print("" + iter.next().getType().getCname());
	    		}
    		}
    		
    		pw.print(") ) ");
    		
    		pw.print("_" + variable.getName() + "->vt[");
    		pw.print("" + method.getMethodClass().getMethodTable().indexOf(method.getName()));
    		// creio que podemos substituir a linha acima para pw.print("" + variable.getType().getMethodTable().indexOf(method.getName()));
    		pw.print("] )(_" + variable.getName());
    	} else {
    		pw.print("_" + method.getMethodClass().getName() + "_" + method.getName() + "(_" +  variable.getName() );
    	}
    	 
    	if(parameterList != null) {
    		pw.print(", ");
    		parameterList.genC(pw);
    	}
    	pw.print(" )");
    	
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

    public String getName() {
    	String nome = variable.getName() + "." + method.getName() + "(";

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

    private ExprList parameterList;
    private Method method;
    private Variable variable;
}
