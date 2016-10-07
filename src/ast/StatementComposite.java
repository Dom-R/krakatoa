package ast;

public class StatementComposite extends Statement {
	public StatementComposite(StatementList statementList) {
		this.statementList = statementList;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		pw.println("{");
		pw.add();
		//statementList.genC(pw);
		pw.sub();
		pw.println("}");
	}

	private StatementList statementList;
}
