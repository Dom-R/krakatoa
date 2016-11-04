/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.ArrayList;

public class StatementRead extends Statement {
	public StatementRead(ArrayList<VariableExpr> variableExprList) {
		this.variableExprList = variableExprList;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("read( ");
		int size = variableExprList.size();
		for(VariableExpr ve : variableExprList) {
			if(ve.getType() instanceof KraClass) {
				pw.print("this.");
			}
			ve.genKra(pw, false);
			if ( --size > 0 )
                pw.print(", ");
		}
		pw.println(" );");
	}

	private ArrayList<VariableExpr> variableExprList;
}
