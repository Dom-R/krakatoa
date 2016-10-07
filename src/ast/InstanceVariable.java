package ast;

public class InstanceVariable extends Variable {

    public InstanceVariable( String name, Type type ) {
        super(name, type);
    }
    
    public void genKra(PW pw) {
    	pw.println("private " + getType().getCname() + " " + getName() + ";");
    }

}