/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class StatementComposite extends Statement {
	public StatementComposite(StatementList statementList) {
		this.statementList = statementList;
	}

	@Override
	public void genC(PW pw) {
		pw.println(" {");
		pw.add();
		statementList.genC(pw);
		pw.sub();
		pw.printlnIdent("}");
	}

	@Override
	public void genKra(PW pw) {
		pw.println(" {");
		pw.add();
		statementList.genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
	}

	private StatementList statementList;
}
