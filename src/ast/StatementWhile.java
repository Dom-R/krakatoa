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
	
	@Override
	public void genKra(PW pw) {
		pw.printIdent("while ( ");
		expr.genKra(pw, false);
		pw.print(" )");
		//pw.add();
		if ( ! (statement instanceof StatementComposite) ) {
			pw.print("\n");
			pw.add();
		}
			
		statement.genKra(pw);
		
		//pw.sub();
		if ( ! (statement instanceof StatementComposite) )
			pw.sub();
	}

	private Expr expr;
	private Statement statement;
}
