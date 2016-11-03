package ast;

import lexer.*;
import java.util.HashMap;


public class ThisExpr extends Expr {

    public ThisExpr(KraClass classeReferida) {
    	this.classeReferida = classeReferida;
    }
    
    @Override
	public void genC( PW pw, boolean putParenthesis ) {
        if ( putParenthesis )
          pw.print("(");
        
        pw.print("this");
        
        if ( putParenthesis )
          pw.print(")");
    }
    
    @Override
	public Type getType() {
       return classeReferida;
    }
    
	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		if ( putParenthesis )
		      pw.print("(");
		
		pw.print("this");
		
		if ( putParenthesis )
		  pw.print(")");
	}
	
	private KraClass classeReferida;
}
