/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class InstanceVariable extends Variable {

    public InstanceVariable( String name, Type type ) {
        super(name, type);
    }

    public void genKra(PW pw) {
    	pw.printlnIdent("private " + getType().getKraname() + " " + getName() + ";");
    }
    
    public void genC(PW pw, KraClass currentClass) {
    	pw.printlnIdent(getType().getCname() + " _" + currentClass.getName() + "" + getCname() + ";");
    }

}
