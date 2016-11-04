/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class StatementBreak extends Statement {

	public StatementBreak() {
	}

	@Override
	public void genC(PW pw) {
		pw.printlnIdent("break;");
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("break;");
	}
}
