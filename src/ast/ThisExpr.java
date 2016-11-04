/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class ThisExpr extends Expr {

    public ThisExpr(KraClass classeReferida) {
    	this.classeReferida = classeReferida;
    }

    @Override
	public void genC( PW pw, boolean putParenthesis ) {
        if ( putParenthesis )
          pw.print("(");

        pw.print("this");

        if ( putParenthesis )
          pw.print(")");
    }

    @Override
	public Type getType() {
       return classeReferida;
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		if ( putParenthesis )
		      pw.print("(");

		pw.print("this");

		if ( putParenthesis )
		  pw.print(")");
	}

	public String getName() {
		return "this";
	}

	private KraClass classeReferida;
}
