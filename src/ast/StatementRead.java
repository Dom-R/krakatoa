/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class StatementRead extends Statement {
	public StatementRead(ArrayList<VariableExpr> variableExprList) {
		this.variableExprList = variableExprList;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		Iterator<VariableExpr> iter = variableExprList.iterator();
		while(iter.hasNext()) {
			VariableExpr var = iter.next();
			
			pw.printlnIdent("{");
			pw.add();
			pw.printlnIdent("char __s[512];");
			pw.printlnIdent("gets(__s);");
			
			// Diferencia entre int e string 
			if(var.getType() == Type.intType) {
				pw.printIdent("sscanf(__s, \"%d\", &");
				
				if(var.getThisClass() != null) {
					pw.print("this->_" + var.getThisClass().getName());
				}
				
				var.genC(pw, false);
				pw.println(");");
			} else {
				var.genC(pw, false);
				pw.printlnIdent(" = malloc(strlen(__s) + 1);");
				pw.printIdent("strcpy(");
				var.genC(pw, false);
				pw.println(", __s);");
			}
			
			pw.sub();
			pw.printlnIdent("}");
		}
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
