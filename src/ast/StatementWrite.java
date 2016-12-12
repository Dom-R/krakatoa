/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.Iterator;

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
		Iterator<Expr> iter = exprList.getExprListIterator();
		while(iter.hasNext()) {
			Expr expr = iter.next();
			if(expr.getType() == Type.stringType) {
				pw.printIdent("puts( ");
			} else {
				pw.printIdent("printf(\"%");
				switch(expr.getType().getName()) {
					case "int":
						pw.print("d");
						break;
					default:
						pw.print("-> ERROR ON TYPE <-");
				}
				pw.print(" \", ");
			}
			expr.genC(pw, false);
			pw.println(");");
		}
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
