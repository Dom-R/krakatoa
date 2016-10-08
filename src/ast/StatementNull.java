package ast;

public class StatementNull extends Statement {
	public StatementNull() {
	}
	
	@Override
	public void genC(PW pw) {
		pw.printIdent(";");
	}
	
	@Override
	public void genKra(PW pw) {
		pw.printlnIdent(";");
	}
}
