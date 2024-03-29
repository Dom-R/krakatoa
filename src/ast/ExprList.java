/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.*;

public class ExprList {

    public ExprList() {
        exprList = new ArrayList<Expr>();
    }

    public void addElement( Expr expr ) {
        exprList.add(expr);
    }

    public Iterator<Expr> getExprListIterator() {
    	return exprList.iterator();
    }

    public void genC( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	e.genC(pw, false);
            if ( --size > 0 )
                pw.print(", ");
        }
    }

    public void genKra( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	e.genKra(pw, false); //Remover coment�rio depois
            if ( --size > 0 )
                pw.print(", ");
        }
    }

    private ArrayList<Expr> exprList;

}
