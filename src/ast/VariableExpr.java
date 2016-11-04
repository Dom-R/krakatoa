/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class VariableExpr extends Expr {

    public VariableExpr( Variable v, boolean isThis ) {
        this.v = v;
        this.isThis = isThis;
    }

    public void genC( PW pw, boolean putParenthesis ) {
    	if (putParenthesis == true)
    		pw.print("(");

        pw.print( v.getName() );

        if (putParenthesis == true)
    		pw.print(")");
    }

    public void genKra( PW pw, boolean putParenthesis ) {

    	if( v != null ) {
    		if (putParenthesis == true)
        		pw.print("(");

    		if (isThis == true)
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
    private boolean isThis;
}
