package ast;

public class NullExpr extends Expr {
    
   public void genC( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
   public Type getType() {
	  KraClass nullClass = new KraClass("Null");
      return nullClass;
   }
   
   @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
	   	if (putParenthesis)
	   		pw.print("(");
		pw.print("null");
		if (putParenthesis)
	   		pw.print(")");
	}
}