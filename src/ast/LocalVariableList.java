/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.*;

public class LocalVariableList extends Statement {

    public LocalVariableList() {
       localList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       localList.add(v);
    }

    public Iterator<Variable> elements() {
        return localList.iterator();
    }

    public int getSize() {
        return localList.size();
    }

    private ArrayList<Variable> localList;

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent(localList.get(0).getType().getKraname() + " ");
		// TODO Auto-generated method stub
		Iterator<Variable> iter = elements();
		while(iter.hasNext()) {
			Variable v = iter.next();
			pw.print(v.getName());
			if(iter.hasNext()) {
				pw.print(", ");
			}
		}
		pw.println(";");
	}

}
