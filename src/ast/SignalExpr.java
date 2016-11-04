/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import lexer.*;

public class SignalExpr extends Expr {

    public SignalExpr( Symbol oper, Expr expr ) {
       this.oper = oper;
       this.expr = expr;
    }

    @Override
	public void genC( PW pw, boolean putParenthesis ) {
       if ( putParenthesis )
          pw.print("(");
       pw.print( oper == Symbol.PLUS ? "+" : "-" );
       expr.genC(pw, true);
       if ( putParenthesis )
          pw.print(")");
    }

    @Override
	public Type getType() {
       return expr.getType();
    }

    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
    	if ( putParenthesis )
            pw.print("(");
         pw.print( oper == Symbol.PLUS ? "+" : "-" );
         expr.genKra(pw, true);
         if ( putParenthesis )
            pw.print(")");
	}

    public String getName() {
 	   String nome = (oper == Symbol.PLUS ? "+" : "-");
 	   nome += expr.getName();
 	   return nome;
    }

    private Expr expr;
    private Symbol oper;
}
