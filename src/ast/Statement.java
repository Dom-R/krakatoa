/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

abstract public class Statement {

	abstract public void genC(PW pw);

	abstract public void genKra(PW pw);

}
