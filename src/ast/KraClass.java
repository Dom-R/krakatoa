package ast;
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
   
   private String name;
   private KraClass superclass;
   private InstanceVariableList instanceVariableList;
   private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
