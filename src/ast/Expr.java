/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

abstract public class Expr {
    abstract public void genC( PW pw, boolean putParenthesis );

    abstract public void genKra( PW pw, boolean putParenthesis );

    abstract public String getName();

      // new method: the type of the expression
    abstract public Type getType();
}
