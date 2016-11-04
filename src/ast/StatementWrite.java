/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
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
		pw.printIdent("printf( "); //Arrumar aqui para pegar os tipos das vari�veis
		exprList.genC (pw);
		pw.println(" );");
		if (flagln)
			pw.println("printf(\"\n\" );");
	}

	@Override
	public void genKra(PW pw) {
		if (flagln)
			pw.printIdent("writeln( ");
		else
			pw.printIdent("write( ");
		exprList.genKra (pw);
		pw.println(" );");
	}

	private ExprList exprList;
	private boolean flagln;
}
