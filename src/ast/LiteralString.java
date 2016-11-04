/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
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
		pw.print("\"" + literalString + "\"");
	}

    public Type getType() {
        return Type.stringType;
    }

    public String getName() {
    	return literalString;
    }

    private String literalString;
}
