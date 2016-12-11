/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class VariableExpr extends Expr {

    public VariableExpr( Variable v, KraClass thisClass ) {
        this.v = v;
        this.thisClass = thisClass;
    }

    public void genC( PW pw, boolean putParenthesis ) {
    	if( v != null ) {
    		if (putParenthesis == true)
        		pw.print("(");

    		if (thisClass != null)
        		pw.print("this->_" + thisClass.getName());

    		pw.print("_" + v.getName() );

    		if (putParenthesis == true)
        		pw.print(")");
    	}
    }

    public void genKra( PW pw, boolean putParenthesis ) {

    	if( v != null ) {
    		if (putParenthesis == true)
        		pw.print("(");

    		if (thisClass != null)
        		pw.print("this.");

    		pw.print( v.getName() );

    		if (putParenthesis == true)
        		pw.print(")");
    	}
    }

    public Type getType() {
        return v.getType();
    }

    public String getName() {
    	return v.getName();
    }

    private Variable v;
    private KraClass thisClass;
}
