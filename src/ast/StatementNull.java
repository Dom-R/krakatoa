package ast;

public class StatementNull extends Statement {
	public StatementNull() {
	}
	
	@Override
	public void genC(PW pw) {
		pw.printIdent(";");
	}
}
