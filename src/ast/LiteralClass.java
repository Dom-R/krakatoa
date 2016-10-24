package ast;

public class LiteralClass extends Expr {
    
    public LiteralClass( KraClass value ) { 
        this.value = value;
    }
    
    public KraClass getValue() {
        return value;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.printIdent("" + value.getCname());
    }
    
    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
    	pw.print("new " + value.getKraname() + "()");
	}
    
    public Type getType() {
        return value;
    }
    
    private KraClass value;
}
