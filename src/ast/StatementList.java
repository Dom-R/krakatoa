/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.*;

public class StatementList {

    public StatementList() {
    	statementList = new ArrayList<Statement>();
    }

    public void addElement(Statement statement) {
    	statementList.add( statement );
    }

    public Iterator<Statement> elements() {
    	return this.statementList.iterator();
    }

    public int getSize() {
        return statementList.size();
    }

    public void genKra(PW pw) {
    	for ( Statement stmt : statementList ) {
    		stmt.genKra(pw);
    	}
	}

    private ArrayList<Statement> statementList;

}
