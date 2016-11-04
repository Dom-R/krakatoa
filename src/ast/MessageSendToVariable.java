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
