/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
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

    public String getName() {
		return value.getName();
	}

    private KraClass value;

}
