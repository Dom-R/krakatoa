/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class Variable {

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public void genKra(PW pw) {

    }

    public String getName() { return name; }

    public Type getType() {
        return type;
    }

    private String name;
    private Type type;
}
