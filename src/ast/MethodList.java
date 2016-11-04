/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.*;

public class MethodList {

    public MethodList() {
       methodList = new ArrayList<Method>();
    }

    public void genKra(PW pw) {
    	for( Method m : methodList ) {
    		//pw.genKra(m);
    	}

    }

    public void addElement(Method m) {
       methodList.add(m);
    }

    public Iterator<Method> elements() {
        return methodList.iterator();
    }

    public int getSize() {
        return methodList.size();
    }

    private ArrayList<Method> methodList;

}
