/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

import java.util.Iterator;
import java.util.ArrayList;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {

	//
	// Classe e metodos de KraClass que guarda metodos da tabela para o genC
	//
   public class MethodTable {
	   
	   public MethodTable() {
		   this.methodNameList = new ArrayList<String>();
		   this.methodNameClass = new ArrayList<String>();
	   }
	   
	   public void put(String methodName, String methodClassName) {
		   
		   int index = indexOf(methodName);
		   if(index == -1) {
			   methodNameList.add(methodName);
			   methodNameClass.add(methodClassName);
		   } else {
			   methodNameList.set(index, methodName);
			   methodNameClass.set(index, methodClassName);
		   }
	   }
	   
	   public int indexOf(String methodName) {
		   return methodNameList.indexOf(methodName);
	   }
	   
	   public int getSize() {
		   return methodNameList.size();
	   }
	   
	   public String getMethodName(int index) {
		   return methodNameList.get(index);
	   }
	   
	   public String getClassName(int index) {
		   return methodNameClass.get(index);
	   }
	   
	   private ArrayList<String> methodNameList;
	   private ArrayList<String> methodNameClass;
   }
   
   public MethodTable getMethodTable() {
	   return methodTable;
   }
   //
   //
   //
	
   public KraClass( String name ) {
      super(name);
      superclass = null;
      instanceVariableList = new InstanceVariableList();
      publicMethodList = new MethodList();
      privateMethodList = new MethodList();
      methodTable = new MethodTable();
   }

   /*public void addSuperClass(KraClass superclass) {
	   this.superclass = superclass;
   }*/

   public String getCname() {
      return "_class_" + getName() + " *";
   }

   public String getKraname() {
	   return getName();
   }

   /*
    * Metodos Superclass
    */
   public void setSuperclass(KraClass superclass) {
	   this.superclass = superclass;
   }

   public KraClass getSuperclass() {
	   return superclass;
   }

   /*
    * Metodos InstanceVariableList
    */
   public InstanceVariableList getInstanceVariableList() {
	   return instanceVariableList;
   }

   public void addInstanceVariable(InstanceVariable v) {
	   instanceVariableList.addElement(v);
   }

   /*
    * Metodos PublicMethodList
    */
   public MethodList getPublicMethodList() {
	   return publicMethodList;
   }

   public void addPublicMethod(Method m) {
	   publicMethodList.addElement(m);
   }

   /*
    * Metodos PrivateMethodList
    */
   public MethodList getPrivateMethodList() {
	   return privateMethodList;
   }

   public void addPrivateMethod(Method m) {
	   privateMethodList.addElement(m);
   }

   public void genC(PW pw) {
	   //
	   // Clona methodTable se a classe tem superclass da superclasse
	   //
	   if(superclass != null) {
		   methodTable = superclass.getMethodTable();
	   }
	   //
	   //
	   //
	   
	   //
	   // gera struct
	   //
	   pw.printlnIdent("typedef");
	   pw.add();
	   pw.printlnIdent("struct _St_" + getName() + " {");
	   pw.add();
	   pw.printlnIdent("Func *vt;");
	   
	   // Insere variaveis de instancia da superclasse
	   if(superclass != null) {
		   superclass.genCInstanceVariable(pw);
	   }
	   
	   // Insere variaveis de instancia da classe
	   Iterator<InstanceVariable> e = instanceVariableList.elements();
	   while(e.hasNext()) {
		   e.next().genC(pw, this);
	   }
	   
	   pw.printlnIdent("} _class_" + getName() + ";");
	   pw.sub();
	   pw.sub();
	   //
	   //
	   //
	   
	   pw.println();
	   pw.printlnIdent( getCname() + "new_" + getName() + "(void);");
	   
	   //
	   // Gera metodos publicos
	   //
	   Iterator<Method> m = publicMethodList.elements();
	   while(m.hasNext()) {
		   pw.println();
		   Method method = m.next();
		   methodTable.put(method.getName(), this.getName());
		   method.genC(pw, this);
	   }
	   //
	   //
	   //
	   
	   //
	   // Gera metodos privados
	   //
	   m = privateMethodList.elements();
	   while(m.hasNext()) {
		   pw.println();
		   m.next().genC(pw, this);
	   }
	   //
	   //
	   //
	   
	   pw.println();
	   
	   //
	   // Gera tabela de metodos publicos(Falta fazer geracao com heranca)
	   //
	   pw.println("Func VTclass_" + getName() + "[] = {");
	   pw.add();
	   
	   // Somente insere metodos publicos
	   /*m = publicMethodList.elements();
	   while(m.hasNext()) {
		   Method metodo = m.next();
		   pw.printIdent("( void (*)() ) _" + getName() + "_" + metodo.getName());
		   if(m.hasNext()) pw.print(",");
		   pw.println();
	   
	   }*/
	   
	   int size = methodTable.getSize();
	   for(int i = 0; i < size; i++) {
		   pw.printIdent("( void (*)() ) _" + methodTable.getClassName(i) + "_" + methodTable.getMethodName(i));
		   if(i < (size - 1)) pw.print(",");
		   pw.println();
	   }
	   
	   pw.printlnIdent("};");
	   pw.sub();
	   //
	   //
	   //
	   
	   pw.println();
	   
	   //
	   // gera funcao de alocacao de memoria para objeto da classe
	   //
	   pw.println( getCname() + "new_" + getName() + "()");
	   pw.println("{");
	   pw.add();
	   pw.printlnIdent( getCname() + "t;");
	   pw.println();
	   pw.printlnIdent("if ( (t = malloc(sizeof(_class_" + getName() + "))) != NULL )");
	   pw.add();
	   pw.printlnIdent("t->vt = VTclass_" + getName() + ";");
	   pw.sub();
	   pw.printlnIdent("return t;");
	   pw.sub();
	   pw.println("}");
   }
   
   public void genKra(PW pw) {
	   pw.print("class " + getName());
	   if(superclass != null) pw.print(" extends " + superclass.getName());
	   pw.println(" {");
	   pw.add();

	   // printa variaveis de instancia
	   Iterator<InstanceVariable> e = instanceVariableList.elements();
	   while(e.hasNext()) {
		   e.next().genKra(pw);
	   }

	   // printa metodos publicos
	   Iterator<Method> m = publicMethodList.elements();
	   while(m.hasNext()) {
		   m.next().genKra(pw);
	   }

	// printa metodos privados
	   m = privateMethodList.elements();
	   while(m.hasNext()) {
		   m.next().genKra(pw);
	   }

	   pw.sub();
	   pw.println("}");
   }
   
   // Metodo para printar variaveis de instancia de superclasses
   private void genCInstanceVariable(PW pw) {
	   if(superclass != null) {
		   superclass.genCInstanceVariable(pw);
	   }
	   Iterator<InstanceVariable> e = instanceVariableList.elements();
	   while(e.hasNext()) {
		   e.next().genC(pw, this);
	   }
   }

   //private String name;
   private KraClass superclass;
   private InstanceVariableList instanceVariableList;
   private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
   
   // variavel que guarda a tabela de metodos para o genC
   private MethodTable methodTable;
   
}
