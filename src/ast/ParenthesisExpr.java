package ast;

public class ParenthesisExpr extends Expr {
    
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("(");
        expr.genC(pw, false);
        pw.print(")");
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
    	pw.print("(");
        expr.genKra(pw, false);
        pw.print(")");
	}
    
    public String getName() {
 	   return "(" + expr.getName() + ")";
    }
    
    private Expr expr;
}