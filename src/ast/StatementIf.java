/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class StatementIf extends Statement {
	public StatementIf(Expr expr, Statement statementThen, Statement statementElse) {
		this.expr = expr;
		this.statementThen = statementThen;
		this.statementElse = statementElse;
	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("if ( ");
		expr.genC(pw, false);
		pw.println(" )");
		pw.add();
		statementThen.genC(pw);
		pw.sub();
		if( statementElse != null ) {
			pw.println("else");
			pw.add();
			statementElse.genC(pw);
			pw.sub();
		}
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("if ( ");
		expr.genKra(pw, false);
		pw.print(" )");

		//pw.add();
		if ( ! (statementThen instanceof StatementComposite) ) {
			pw.print("\n");
			pw.add();
		}

		statementThen.genKra(pw);

		//pw.sub();
		if ( ! (statementThen instanceof StatementComposite) )
			pw.sub();

		if( statementElse != null ) {
			pw.printIdent("else");

			if ( ! (statementElse instanceof StatementComposite) ) {
				pw.print("\n");
				pw.add();
			}
			//pw.add();

			statementElse.genKra(pw);

			//pw.sub();
			if ( ! (statementElse instanceof StatementComposite) )
				pw.sub();

		}
	}

	private Expr expr;
	private Statement statementThen;
	private Statement statementElse;
}
