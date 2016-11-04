/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class StatementReturn extends Statement {
	public StatementReturn(Expr expr) {
		this.expr = expr;
	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("return ");
		expr.genC(pw, false);
		pw.println(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("return ");
		expr.genKra(pw, false);
		pw.println(";");
	}

	private Expr expr;
}
