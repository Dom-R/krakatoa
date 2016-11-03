
package ast;

import lexer.*;

public class UnaryExpr extends Expr {

	public UnaryExpr(Expr expr, Symbol op) {
		this.expr = expr;
		this.op = op;
	}

	@Override
	public void genC(PW pw, boolean putParenthesis) {
		switch (op) {
		case PLUS:
			pw.print("+");
			break;
		case MINUS:
			pw.print("-");
			break;
		case NOT:
			pw.print("!");
			break;
		default:
			pw.print(" internal error at UnaryExpr::genC");

		}
		expr.genC(pw, false);
	}
	
	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		switch (op) {
		case PLUS:
			pw.print("+");
			break;
		case MINUS:
			pw.print("-");
			break;
		case NOT:
			pw.print("!");
			break;
		default:
			pw.print(" internal error at UnaryExpr::genKra");

		}
		expr.genKra(pw, false);
	}

	@Override
	public Type getType() {
		return expr.getType();
	}
	
	public String getName() {
		String nome = "";
		switch (op) {
		case PLUS:
			nome = "+";
			break;
		case MINUS:
			nome = "-";
			break;
		case NOT:
			nome = "!";
			break;
		}
		nome += expr.getName();
		return nome;
	}

	private Expr	expr;
	private Symbol	op;
}
