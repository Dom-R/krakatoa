/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<KraClass> classList, ArrayList<MetaobjectCall> metaobjectCallList,
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}


	public void genKra(PW pw) {
		for( KraClass c : classList ) {
			c.genKra(pw);
		}
	}

	public void genC(PW pw) {
		//
		// Gera cabecalho
		//
		pw.printlnIdent("#include <malloc.h>");
		pw.printlnIdent("#include <stdlib.h>");
		pw.printlnIdent("#include <stdio.h>");
		pw.println();
		pw.printlnIdent("typedef int boolean;");
		pw.printlnIdent("#define true 1");
		pw.printlnIdent("#define false 0");
		pw.println();
		pw.printlnIdent("typedef");
		pw.add();
		pw.printlnIdent("void (*Func)();");
		pw.sub();
		//
		//
		//
		
		//
		// geracao de codigo para cada classe
		//
		int programRunIndex = -1;
		for( KraClass c : classList ) {
			pw.println();
			c.genC(pw);
			
			// Recupera indice do metodo run na classe Program
			if(c.getName().equals("Program")) programRunIndex = c.getMethodTable().indexOf("run");
		}
		//
		//
		//
		
		pw.println();
		
		//
		// gera main
		//
		pw.printlnIdent("int main() {");
		pw.add();
		pw.printlnIdent("_class_Program *program;");
		pw.println();
		pw.printlnIdent("program = new_Program();");
		pw.printlnIdent("( ( void (*)(_class_Program *) ) program->vt[" + programRunIndex + "] )(program);");
		pw.printlnIdent("return 0;");
		pw.sub();
		pw.printlnIdent("}");
	}

	public ArrayList<KraClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectCall> getMetaobjectCallList() {
		return metaobjectCallList;
	}


	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}


	private ArrayList<KraClass> classList;
	private ArrayList<MetaobjectCall> metaobjectCallList;

	ArrayList<CompilationError> compilationErrorList;


}
