package ast;

public class StatementWrite extends Statement {
	public StatementWrite(ExprList exprList, boolean flagln) {
		this.exprList = exprList;
		this.flagln = flagln;
	}
	
	public StatementWrite(ExprList exprList) {
		this.exprList = exprList;
		this.flagln = false;
	}
	
	@Override
	public void genC(PW pw) {
		if (flagln)
			pw.printIdent("System.out.println( ");
		else
			pw.printIdent("System.out.print( ");
		
		exprList.genC (pw);
		pw.println(" );");
	}
	
	@Override
	public void genKra(PW pw) {
		pw.printIdent("write( ");
		exprList.genKra (pw);
		pw.println(" );");
		
		if (flagln)
			pw.printlnIdent("write(\"\n\");");
	}

	private ExprList exprList;
	private boolean flagln;
}
