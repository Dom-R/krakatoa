package ast;

public class StatementExpr extends Statement {
	public StatementExpr(Expr leftExpr, Expr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
		
	}
	
	@Override
	public void genC(PW pw) {
		pw.printlnIdent("Implementar genKra de StatementExpr");
	}
	
	@Override
	public void genKra(PW pw) {
		pw.printIdent("leftExpr");
		if(rightExpr != null) {
			pw.print(" = ");
			pw.print("rightExpr");
		}
		pw.println(";");
	}

	private Expr leftExpr;
	private Expr rightExpr;
}
