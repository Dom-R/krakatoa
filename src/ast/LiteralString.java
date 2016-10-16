package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }
    
    @Override
	public void genKra(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	}
    
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
