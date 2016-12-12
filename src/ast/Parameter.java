/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;


public class Parameter extends Variable {

    public Parameter( String name, Type type ) {
        super(name, type);
    }

    public void genKra(PW pw) {
    	pw.print(getType().getKraname() + " " + getName());
    }
    
    public void genC(PW pw) {
    	pw.print(getType().getCname() + " " + getCname());
    }

}
