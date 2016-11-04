/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class StatementNull extends Statement {
	public StatementNull() {
	}

	@Override
	public void genC(PW pw) {
		pw.printlnIdent(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent(";");
	}
}
