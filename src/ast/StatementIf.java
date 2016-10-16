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
		pw.println(" )");
		pw.add();
		statementThen.genKra(pw);
		pw.sub();
		if( statementElse != null ) {
			pw.println("else");
			pw.add();
			statementElse.genKra(pw);
			pw.sub();
		}
	}

	private Expr expr;
	private Statement statementThen;
	private Statement statementElse;
}
