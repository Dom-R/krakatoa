/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.Iterator;

public class StatementDoWhile extends Statement {
	public StatementDoWhile(Expr expr, StatementList statementList) {
		this.expr = expr;
		this.statementList = statementList;
	}

	@Override
	public void genC(PW pw) {
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("do {");
		pw.add();

		Iterator<Statement> s = statementList.elements();
		while(s.hasNext()) {
			/*
			* Debug pois alguns statements ainda retornam null
			*/
			Statement stmt = s.next();
			if(stmt != null)
				stmt.genKra(pw);
			else
				System.out.println("Debug: Statement Null!");
			/*
			* Fim Debug pois alguns statements ainda retornam null
			*/

			//s.next().genKra(pw);
		}

		pw.sub();
		pw.printIdent("}	while(");
		expr.genKra(pw, false);
		pw.println(");");
	}

	private Expr expr;
	private StatementList statementList;
}
