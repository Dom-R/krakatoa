package ast;

import java.util.Iterator;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass( String name ) {
      super(name);
      superclass = null;
      instanceVariableList = new InstanceVariableList();
      publicMethodList = new MethodList();
      privateMethodList = new MethodList();
   }
   
   /*public void addSuperClass(KraClass superclass) {
	   this.superclass = superclass;
   }*/
   
   public String getCname() {
      return getName();
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
   
   public void genKra(PW pw) {
	   pw.print("class " + getCname());
	   if(superclass != null) pw.print(" extends " + superclass.getCname());
	   pw.println(" {");
	   pw.add();
	   
	   // printa variaveis de instancia
	   Iterator<InstanceVariable> e = instanceVariableList.elements();
	   while(e.hasNext()) {
		   e.next().genKra(pw);
	   }
	   
	   // printa metodos privados
	   Iterator<Method> m = publicMethodList.elements();
	   while(m.hasNext()) {
		   m.next().genKra(pw);
	   }
	   
	   pw.sub();
	   pw.println("}");
   }
   
   //private String name;
   private KraClass superclass;
   private InstanceVariableList instanceVariableList;
   private MethodList publicMethodList, privateMethodList;
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}
