package ast;

public class StatementWhile extends Statement {
	public StatementWhile(Expr expr, Statement statement) {
		this.expr = expr;
		this.statement = statement;
	}
	
	@Override
	public void genC(PW pw) {
		pw.printIdent("while ( ");
		expr.genC(pw, false);
		pw.println(" )");
		pw.add();
		statement.genC(pw);
		pw.sub();
	}

	private Expr expr;
	private Statement statement;
}
