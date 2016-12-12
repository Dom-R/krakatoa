/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class StatementExpr extends Statement {
	public StatementExpr(Expr leftExpr, Expr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;

	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("");
		leftExpr.genC(pw, false);
		if(rightExpr != null) {
			pw.print(" = ");
			rightExpr.genC(pw, false);
		}
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("");
		leftExpr.genKra(pw, false);
		if(rightExpr != null) {
			pw.print(" = ");
			rightExpr.genKra(pw, false);
		}
		pw.println(";");
	}

	private Expr leftExpr;
	private Expr rightExpr;
}
